// LoginBonusData.java
package io.github.c0mpas5.loginBonusPlugin;

import org.bukkit.Bukkit;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class LoginBonusData {

    private final LoginBonusPlugin plugin;
    private final MySQLManager mysqlManager;
    private Connection con = null;
    private MySQLFunc MySQL;

    private String serverName = "man10";

    public LoginBonusData(LoginBonusPlugin plugin, MySQLManager mysqlManager) {
        this.plugin = plugin;
        this.mysqlManager = mysqlManager;
        createTableIfNotExists();
    }

        public int getLoginStreak(UUID uuid, String currentBonusName) {
            // スレッドから値を受け取るためにAtomicIntegerを使用
            final AtomicInteger result = new AtomicInteger(0);

            try {
                // スレッドを作成して実行
                Thread th = new Thread(() -> {
                    Set<LocalDate> loginDates = fetchLoginDates(uuid, currentBonusName);
                    List<LocalDate> sortedLoginDates = new ArrayList<>(loginDates);
                    sortedLoginDates.sort(Collections.reverseOrder());
                    int streak = calculateStreak(sortedLoginDates);

                    // 結果をAtomicIntegerに保存
                    if(streak != 0 && streak % 10 == 0) {
                        result.set(10);
                    } else {
                        result.set(streak % 10);
                    }
                });

                th.start();
                th.join(); // スレッドの終了を待機

                return result.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return 0;
            }
        }

    public int getLoginCount(UUID uuid, String currentBonusName) {
        // スレッドから値を受け取るためにAtomicIntegerを使用
        final AtomicInteger result = new AtomicInteger(0);

        try {
            // スレッドを作成して実行
            Thread th = new Thread(() -> {
                Set<LocalDate> loginDates = fetchLoginDates(uuid, currentBonusName);
                result.set(loginDates.size());
            });

            th.start();
            th.join(); // スレッドの終了を待機

            return result.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 呼び出し先でサブスレッドで処理する
    public Set<LocalDate> fetchLoginDates(UUID uuid, String currentBonusName) {
        int dailyResetHour = RewardManager.getDailyResetTime(currentBonusName);
        LocalDate startDate = RewardManager.getPeriodStartDate(currentBonusName);
        LocalDate endDate = RewardManager.getPeriodEndDate(currentBonusName);
        ArrayList<LocalDate> recoverDates = RewardManager.getRecoverLoginMissedDate();

        Set<LocalDate> loginDates = new HashSet<>();

        try (Connection con = new MySQLFunc(mysqlManager.HOST1, mysqlManager.DB1, mysqlManager.USER1,
                mysqlManager.PASS1, mysqlManager.PORT1).open()) {

            if (con == null) {
                Bukkit.getLogger().info("failed to open MYSQL");
                return loginDates;
            }

            String sql = """
            SELECT DISTINCT DATE_FORMAT(DATE_SUB(connected_time, INTERVAL ? HOUR), '%Y-%m-%d') AS login_date
            FROM connection_log
            WHERE uuid = ? AND server = ?
                AND DATE_FORMAT(DATE_SUB(connected_time, INTERVAL ? HOUR), '%Y-%m-%d') BETWEEN ? AND ?
            ORDER BY login_date DESC
        """;

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, dailyResetHour);
                ps.setString(2, uuid.toString());
                ps.setString(3, serverName);
                ps.setInt(4, dailyResetHour);
                ps.setString(5, startDate.toString());
                ps.setString(6, endDate.toString());

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        loginDates.add(LocalDate.parse(rs.getString("login_date")));
                    }
                }
            }

            // 今日の日付を dailyResetHour を考慮して計算
            LocalDate today = LocalDate.now(ZoneId.systemDefault());
            if (LocalTime.now(ZoneId.systemDefault()).isBefore(LocalTime.of(dailyResetHour, 0))) {
                today = today.minusDays(1);
            }

            recoverDates.add(today); // 今日を recoverDates に追加

            // recoverDates を startDate から endDate の範囲内のみに制限
            recoverDates.removeIf(date -> date.isBefore(startDate) || date.isAfter(endDate));
            loginDates.addAll(recoverDates); // メンテナンス日の追加

            // startDate から endDate の範囲内にある日付のみ抽出
            loginDates.removeIf(date -> date.isBefore(startDate) || date.isAfter(endDate));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return loginDates;
    }

    private static int calculateStreak(List<LocalDate> loginDates) {
        if (loginDates.isEmpty()) return 0;

        int streak = 1;
        LocalDate prevDate = loginDates.get(0);

        for (int i = 1; i < loginDates.size(); i++) {
            LocalDate currentDate = loginDates.get(i);
            if (prevDate.minusDays(1).equals(currentDate)) {
                streak++;
            } else {
                break;
            }
            prevDate = currentDate;
        }
        return streak;
    }

    public void setClaimedCount(UUID playerUUID, int claimedCount) {
        try (Connection con = new MySQLFunc(mysqlManager.HOST0, mysqlManager.DB0, mysqlManager.USER0, mysqlManager.PASS0, mysqlManager.PORT0).open()) {
            if (con == null) {
                Bukkit.getLogger().info("failed to open MYSQL");
                return;
            }

            try (PreparedStatement ps = con.prepareStatement("INSERT INTO loginbonus_info (uuid, claimed_count) VALUES (?, ?) ON DUPLICATE KEY UPDATE claimed_count = ?")) {
                ps.setString(1, playerUUID.toString());
                ps.setInt(2, claimedCount);
                ps.setInt(3, claimedCount);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getClaimedCount(UUID playerUUID) {
        try (Connection con = new MySQLFunc(mysqlManager.HOST0, mysqlManager.DB0, mysqlManager.USER0, mysqlManager.PASS0, mysqlManager.PORT0).open()) {
            if (con == null) {
                Bukkit.getLogger().info("failed to open MYSQL");
                return 0;
            }

            try (PreparedStatement ps = con.prepareStatement("SELECT claimed_count FROM loginbonus_info WHERE uuid = ?")) {
                ps.setString(1, playerUUID.toString());

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("claimed_count");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void setClaimedItemStack(UUID playerUUID, String bonusName, int day, String claimedItemPoolType, String claimedItemStack, LocalDateTime claimedDateTime) {
        Thread th = new Thread(() -> {
            try (Connection con = new MySQLFunc(mysqlManager.HOST0, mysqlManager.DB0, mysqlManager.USER0, mysqlManager.PASS0, mysqlManager.PORT0).open()) {
                if (con == null) {
                    Bukkit.getLogger().info("failed to open MYSQL");
                    return;
                }

                try (PreparedStatement ps = con.prepareStatement("INSERT INTO loginbonus_reward_log (uuid, login_bonus_name, day, claimed_item_pool_type, claimed_item_stack, claimed_datetime) VALUES (?, ?, ?, ?, ?, ?)")) {
                    ps.setString(1, playerUUID.toString());
                    ps.setString(2, bonusName);
                    ps.setInt(3, day);
                    ps.setString(4, claimedItemPoolType);
                    ps.setString(5, claimedItemStack);
                    ps.setTimestamp(6, java.sql.Timestamp.valueOf(claimedDateTime));
                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        th.start();
    }

    public LocalDateTime getLastRewardClaimedDate(UUID playerUUID, String bonusType, String bonusName) {
        // スレッドから値を受け取るためにAtomicReferenceを使用
        final AtomicReference<LocalDateTime> result = new AtomicReference<>(null);

        try {
            // スレッドを作成して実行
            Thread th = new Thread(() -> {
                try (Connection con = new MySQLFunc(mysqlManager.HOST0, mysqlManager.DB0, mysqlManager.USER0, mysqlManager.PASS0, mysqlManager.PORT0).open()) {
                    if (con == null) {
                        Bukkit.getLogger().info("failed to open MYSQL");
                        return;
                    }

                    String sql;
                    if (bonusType.equals("accumulated")) {
                        sql = "SELECT claimed_datetime FROM loginbonus_reward_log " +
                                "WHERE uuid = ? AND claimed_item_pool_type IN ('normal', 'special', 'bonus') AND login_bonus_name = ? " +
                                "ORDER BY claimed_datetime DESC LIMIT 1";
                    } else {
                        sql = "SELECT claimed_datetime FROM loginbonus_reward_log " +
                                "WHERE uuid = ? AND claimed_item_pool_type = 'continuous' AND login_bonus_name = ? " +
                                "ORDER BY claimed_datetime DESC LIMIT 1";
                    }

                    try (PreparedStatement ps = con.prepareStatement(sql)) {
                        ps.setString(1, playerUUID.toString());
                        ps.setString(2, bonusName);

                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                result.set(rs.getTimestamp("claimed_datetime").toLocalDateTime());
                            }
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            th.start();
            th.join(); // スレッドの終了を待機

            return result.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getNumOfClaimedContinuousReward(UUID playerUUID, String bonusName) {
        // スレッドから値を受け取るためにAtomicIntegerを使用
        final AtomicInteger result = new AtomicInteger(0);

        try {
            // スレッドを作成して実行
            Thread th = new Thread(() -> {
                try (Connection con = new MySQLFunc(mysqlManager.HOST0, mysqlManager.DB0, mysqlManager.USER0, mysqlManager.PASS0, mysqlManager.PORT0).open()) {
                    if (con == null) {
                        Bukkit.getLogger().info("failed to open MYSQL");
                        return;
                    }

                    int latestClaimedDay = 0; // 受取履歴がない時は0を返す
                    try (PreparedStatement ps = con.prepareStatement("SELECT day FROM loginbonus_reward_log WHERE uuid = ? AND claimed_item_pool_type = 'continuous' AND login_bonus_name = ? ORDER BY claimed_datetime DESC LIMIT 1")) {
                        ps.setString(1, playerUUID.toString());
                        ps.setString(2, bonusName);

                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                latestClaimedDay = rs.getInt("day");
                            }
                        }
                    }

                    result.set(latestClaimedDay);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            th.start();
            th.join(); // スレッドの終了を待機

            return result.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean hasPlayerClaimedBonusForDayAndPoolType(UUID playerUUID, int day, String poolType, String bonusName) {
        // スレッドから値を受け取るためにAtomicBooleanを使用
        final AtomicBoolean result = new AtomicBoolean(false);

        try {
            // スレッドを作成して実行
            Thread th = new Thread(() -> {
                try (Connection con = new MySQLFunc(mysqlManager.HOST0, mysqlManager.DB0, mysqlManager.USER0, mysqlManager.PASS0, mysqlManager.PORT0).open()) {
                    if (con == null) {
                        Bukkit.getLogger().info("failed to open MYSQL");
                        return;
                    }

                    String sql = "SELECT 1 FROM loginbonus_reward_log " +
                            "WHERE uuid = ? AND day = ? AND login_bonus_name = ? AND claimed_item_pool_type = ? " +
                            "LIMIT 1";

                    try (PreparedStatement ps = con.prepareStatement(sql)) {
                        ps.setString(1, playerUUID.toString());
                        ps.setInt(2, day);
                        ps.setString(3, bonusName);
                        ps.setString(4, poolType);

                        try (ResultSet rs = ps.executeQuery()) {
                            result.set(rs.next());
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            th.start();
            th.join(); // スレッドの終了を待機

            return result.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ほぼbonus枠用
    public boolean hasPlayerClaimedBonusForPoolType(UUID playerUUID, String poolType, String bonusName) {
        // スレッドから値を受け取るためにAtomicBooleanを使用
        final AtomicBoolean result = new AtomicBoolean(false);

        try {
            // スレッドを作成して実行
            Thread th = new Thread(() -> {
                try (Connection con = new MySQLFunc(mysqlManager.HOST0, mysqlManager.DB0, mysqlManager.USER0, mysqlManager.PASS0, mysqlManager.PORT0).open()) {
                    if (con == null) {
                        Bukkit.getLogger().info("failed to open MYSQL");
                        return;
                    }

                    String sql = "SELECT 1 FROM loginbonus_reward_log " +
                            "WHERE uuid = ? AND claimed_item_pool_type = ? AND login_bonus_name = ? " +
                            "LIMIT 1";

                    try (PreparedStatement ps = con.prepareStatement(sql)) {
                        ps.setString(1, playerUUID.toString());
                        ps.setString(2, poolType);
                        ps.setString(3, bonusName);

                        try (ResultSet rs = ps.executeQuery()) {
                            result.set(rs.next());
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            th.start();
            th.join(); // スレッドの終了を待機

            return result.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void createTableIfNotExists() {
        Thread th = new Thread(() -> {
            String query = "CREATE TABLE IF NOT EXISTS connection_log (uuid VARCHAR(36), connected_time DATETIME, server VARCHAR(16))";
            mysqlManager.execute(query, 1);
            query = "CREATE TABLE IF NOT EXISTS loginbonus_reward_log (uuid VARCHAR(36), login_bonus_name VARCHAR(48), day INT, claimed_item_pool_type VARCHAR(16), claimed_item_stack TEXT, claimed_datetime DATETIME)";
            mysqlManager.execute(query, 0);

            //デバッグ：DBに適当なログイン日時を入力
            //setLastClaimedDate(UUID.fromString("7f05cd38-a2fa-44fc-8c9e-825e86c86efe"), LocalDateTime.of(2025,1,7,10,0), "Paper");
        });
        th.start();
    }

    public LoginBonusPlugin getPlugin() {
        return plugin;
    }
}