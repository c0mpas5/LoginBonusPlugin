// LoginBonusData.java
package io.github.c0mpas5.loginBonusPlugin;

import org.bukkit.Bukkit;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.ArrayList;

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

//    public void setAllData(UUID playerUUID, int loginCount, int claimedCount, LocalDateTime lastLoginDateTime) {
////        mysqlManager.execute("INSERT INTO loginbonus_info (uuid, loginCount) VALUES ('" + playerUUID.toString() + "', " + loginCount + ") ON DUPLICATE KEY UPDATE loginCount = " + loginCount, 0);
//        this.MySQL = new MySQLFunc(mysqlManager.HOST0, mysqlManager.DB0, mysqlManager.USER0, mysqlManager.PASS0, mysqlManager.PORT0);
//        this.con = this.MySQL.open();
//        if(this.con == null){
//            Bukkit.getLogger().info("failed to open MYSQL");
//            return;
//        }
//        try {
//            PreparedStatement ps = this.con.prepareStatement("INSERT INTO loginbonus_info (uuid, login_count, claimed_count, last_login_datetime) VALUES (?, ?, ?, ?)");
//            ps.setString(1, playerUUID.toString());
//            ps.setInt(2, loginCount);
//            ps.setInt(3, claimedCount);
//            ps.setTimestamp(4, java.sql.Timestamp.valueOf(lastLoginDateTime));
//            ps.executeUpdate();
//            ps.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        this.mysqlManager.close();
//    }

//    public void setLoginCount(UUID playerUUID, int loginCount) {
//        this.MySQL = new MySQLFunc(mysqlManager.HOST0, mysqlManager.DB0, mysqlManager.USER0, mysqlManager.PASS0, mysqlManager.PORT0);
//        this.con = this.MySQL.open();
//        if(this.con == null){
//            Bukkit.getLogger().info("failed to open MYSQL");
//            return;
//        }
//        try {
//            PreparedStatement ps = this.con.prepareStatement("INSERT INTO loginbonus_info (uuid, login_count) VALUES (?, ?) ON DUPLICATE KEY UPDATE login_count = ?");
//            ps.setString(1, playerUUID.toString());
//            ps.setInt(2, loginCount);
//            ps.setInt(3, loginCount);
//            ps.executeUpdate();
//            ps.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        this.mysqlManager.close();
//    }

//    public int getLoginCountOLD(UUID playerUUID) {
//        this.MySQL = new MySQLFunc(mysqlManager.HOST0, mysqlManager.DB0, mysqlManager.USER0, mysqlManager.PASS0, mysqlManager.PORT0);
//        this.con = this.MySQL.open();
//        if(this.con == null){
//            Bukkit.getLogger().info("failed to open MYSQL");
//            return 0;
//        }
//        try {
//            PreparedStatement ps = this.con.prepareStatement("SELECT login_count FROM loginbonus_info WHERE uuid = ?");
//            ps.setString(1, playerUUID.toString());
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                return rs.getInt("login_count");
//            }
//            ps.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        this.mysqlManager.close();
//        return 0;
//    }

    public int getLoginCount(UUID playerUUID, int dailyResetHour, LocalDate startDate, LocalDate endDate) {
        this.MySQL = new MySQLFunc(mysqlManager.HOST1, mysqlManager.DB1, mysqlManager.USER1, mysqlManager.PASS1, mysqlManager.PORT1);
        this.con = this.MySQL.open();
        if(this.con == null){
            Bukkit.getLogger().info("failed to open MYSQL");
            return 0;
        }
        try {
            String sql = """
                        WITH adjusted_dates AS (
                            SELECT DISTINCT 
                                uuid, 
                                server,
                                DATE(DATE_SUB(connected_time, INTERVAL ? HOUR)) AS adjusted_date
                            FROM connection_log
                            WHERE uuid = ?
                              AND server = ?
                              AND connected_time >= ? 
                              AND connected_time < DATE_ADD(?, INTERVAL 1 DAY)
                        )
                        SELECT COUNT(*) AS login_days
                        FROM adjusted_dates;
                    """;

            PreparedStatement ps = this.con.prepareStatement(sql);
            ps.setInt(1, dailyResetHour); // 基準時刻
            ps.setString(2, playerUUID.toString()); // UUID
            ps.setString(3, serverName); // サーバー名
            ps.setDate(4, Date.valueOf(startDate)); // 開始日
            ps.setDate(5, Date.valueOf(endDate)); // 終了日
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("login_days");
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.mysqlManager.close();
        return 0;
    }

    public int getLoginStreak(UUID playerUUID, int dailyResetHour) {
        this.MySQL = new MySQLFunc(mysqlManager.HOST1, mysqlManager.DB1, mysqlManager.USER1, mysqlManager.PASS1, mysqlManager.PORT1);
        this.con = this.MySQL.open();
        if(this.con == null){
            Bukkit.getLogger().info("failed to open MYSQL");
            return 0;
        }
        try {
            String sql = """
            WITH distinct_dates AS (
                SELECT DISTINCT DATE(DATE_SUB(connected_time, INTERVAL ? HOUR)) AS login_date
                FROM connection_log
                WHERE uuid = ? AND server = ?
                UNION
                SELECT DATE(DATE_SUB(CURDATE(), INTERVAL ? HOUR))
            ),
            ranked_dates AS (
                SELECT
                    login_date,
                    ROW_NUMBER() OVER (ORDER BY login_date DESC) AS rn,
                    DATEDIFF(MAX(login_date) OVER (), login_date) AS diff
                FROM distinct_dates
            ),
            grouped_dates AS (
                SELECT login_date, rn, CAST(diff AS SIGNED) - CAST(rn AS SIGNED) AS grp
                FROM ranked_dates
            )
            SELECT COUNT(*) AS consecutive_days
            FROM grouped_dates
            WHERE grp = (SELECT MIN(grp) FROM grouped_dates);
            """;
            PreparedStatement ps = this.con.prepareStatement(sql);
            ps.setInt(1, dailyResetHour);
            ps.setString(2, playerUUID.toString());
            ps.setString(3, serverName);
            ps.setInt(4, dailyResetHour);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("consecutive_days");
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.mysqlManager.close();
        return 0;
    }

    public void setClaimedCount(UUID playerUUID, int claimedCount) {
        this.MySQL = new MySQLFunc(mysqlManager.HOST0, mysqlManager.DB0, mysqlManager.USER0, mysqlManager.PASS0, mysqlManager.PORT0);
        this.con = this.MySQL.open();
        if(this.con == null){
            Bukkit.getLogger().info("failed to open MYSQL");
            return;
        }
        try {
            PreparedStatement ps = this.con.prepareStatement("INSERT INTO loginbonus_info (uuid, claimed_count) VALUES (?, ?) ON DUPLICATE KEY UPDATE claimed_count = ?");
            ps.setString(1, playerUUID.toString());
            ps.setInt(2, claimedCount);
            ps.setInt(3, claimedCount);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.mysqlManager.close();
    }

    public int getClaimedCount(UUID playerUUID) {
        this.MySQL = new MySQLFunc(mysqlManager.HOST0, mysqlManager.DB0, mysqlManager.USER0, mysqlManager.PASS0, mysqlManager.PORT0);
        this.con = this.MySQL.open();
        if(this.con == null){
            Bukkit.getLogger().info("failed to open MYSQL");
            return 0;
        }
        try {
            PreparedStatement ps = this.con.prepareStatement("SELECT claimed_count FROM loginbonus_info WHERE uuid = ?");
            ps.setString(1, playerUUID.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("claimed_count");
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.mysqlManager.close();

        return 0;
    }

//    public void setLastLoginDateTime(UUID playerUUID, LocalDateTime lastLoginDateTime) {
//        this.MySQL = new MySQLFunc(mysqlManager.HOST0, mysqlManager.DB0, mysqlManager.USER0, mysqlManager.PASS0, mysqlManager.PORT0);
//        this.con = this.MySQL.open();
//        if(this.con == null){
//            Bukkit.getLogger().info("failed to open MYSQL");
//            return;
//        }
//        try {
//            PreparedStatement ps = this.con.prepareStatement("INSERT INTO loginbonus_info (uuid, last_login_datetime) VALUES (?, ?) ON DUPLICATE KEY UPDATE last_login_datetime = ?");
//            ps.setString(1, playerUUID.toString());
//            ps.setTimestamp(2, java.sql.Timestamp.valueOf(lastLoginDateTime));
//            ps.setTimestamp(3, java.sql.Timestamp.valueOf(lastLoginDateTime));
//            ps.executeUpdate();
//            ps.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        this.mysqlManager.close();
//    }

//    public LocalDateTime getLastLoginDateTime(UUID playerUUID) {
//        this.MySQL = new MySQLFunc(mysqlManager.HOST0, mysqlManager.DB0, mysqlManager.USER0, mysqlManager.PASS0, mysqlManager.PORT0);
//        this.con = this.MySQL.open();
//        if(this.con == null){
//            Bukkit.getLogger().info("failed to open MYSQL");
//            return null;
//        }
//        LocalDateTime lastLoginDateTime = null;
//        try {
//            PreparedStatement ps = this.con.prepareStatement("SELECT last_login_datetime FROM loginbonus_info WHERE uuid = ?");
//            ps.setString(1, playerUUID.toString());
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                lastLoginDateTime = rs.getTimestamp("last_login_datetime").toLocalDateTime();
//            }
//            ps.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            this.mysqlManager.close();
//        }
//        return lastLoginDateTime;
//    }

    public void setClaimedItemStack(UUID playerUUID, String claimedItemStack, LocalDateTime claimedDate) {
        this.MySQL = new MySQLFunc(mysqlManager.HOST0, mysqlManager.DB0, mysqlManager.USER0, mysqlManager.PASS0, mysqlManager.PORT0);
        this.con = this.MySQL.open();
        if(this.con == null){
            Bukkit.getLogger().info("failed to open MYSQL");
            return;
        }
        try {
            PreparedStatement ps = this.con.prepareStatement("INSERT INTO loginbonus_reward_log (uuid, claimed_item_stack, claimed_datetime) VALUES (?, ?, ?)");
            ps.setString(1, playerUUID.toString());
            ps.setString(2, claimedItemStack);
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(claimedDate));
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.mysqlManager.close();
    }

    public LocalDateTime getLastClaimedDate(UUID playerUUID) {
        this.MySQL = new MySQLFunc(mysqlManager.HOST0, mysqlManager.DB0, mysqlManager.USER0, mysqlManager.PASS0, mysqlManager.PORT0);
        this.con = this.MySQL.open();
        if(this.con == null){
            Bukkit.getLogger().info("failed to open MYSQL");
            return null;
        }
        LocalDateTime lastClaimedDate = null;
        try {
            PreparedStatement ps = this.con.prepareStatement("SELECT claimed_datetime FROM loginbonus_reward_log WHERE uuid = ? ORDER BY claimed_datetime DESC LIMIT 1");
            ps.setString(1, playerUUID.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                lastClaimedDate = rs.getTimestamp("claimed_datetime").toLocalDateTime();
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.mysqlManager.close();
        }
        return lastClaimedDate;
    }

    private void createTableIfNotExists() {
        String query = "CREATE TABLE IF NOT EXISTS loginbonus_info (uuid VARCHAR(36) PRIMARY KEY, claimed_count INT)";
        mysqlManager.execute(query, 0);
        query = "CREATE TABLE IF NOT EXISTS connection_log (uuid VARCHAR(36), connected_time DATETIME, server VARCHAR(16))";
        mysqlManager.execute(query, 1);
        query = "CREATE TABLE IF NOT EXISTS loginbonus_reward_log (uuid VARCHAR(36), claimed_item_stack TEXT, claimed_datetime DATETIME)";
        mysqlManager.execute(query, 0);

        //デバッグ：DBに適当なログイン日時を入力
        //setLastClaimedDate(UUID.fromString("7f05cd38-a2fa-44fc-8c9e-825e86c86efe"), LocalDateTime.of(2025,1,7,10,0), "Paper");
    }
}