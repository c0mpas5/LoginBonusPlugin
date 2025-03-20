package io.github.c0mpas5.loginBonusPlugin;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RewardManager {

    private static final File rewardFile = new File("plugins/LoginBonusPlugin/reward.yml");
    private static final YamlConfiguration rewardConfig = YamlConfiguration.loadConfiguration(rewardFile);

    static {
        createFileIfNotExists();
    }

    private static void createFileIfNotExists() {
        if (!rewardFile.exists()) {
            try {
                Files.createDirectories(Paths.get("plugins/LoginBonusPlugin"));
                rewardFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /////////////////////////// プールの保存・読込・サイズカウント ///////////////////////////
    // 保存
    public static void saveRewards(String bonusName, String poolType, ArrayList<ItemStack> items) {
        for (int i = 0; i < items.size(); i++) {
            rewardConfig.set("loginBonuses." + bonusName + ".pool." + poolType + ".rewards.slot_" + i, items.get(i));
        }
        rewardConfig.set("loginBonuses." + bonusName + ".pool." + poolType + ".poolSize", items.size());
        try {
            rewardConfig.save(rewardFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 指定したプールの情報を削除
    public static void deleteRewardsInfo(String bonusName, String poolType) {
        rewardConfig.set("loginBonuses." + bonusName + ".pool." + poolType, null);
        try {
            rewardConfig.save(rewardFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // プールサイズ読込
    public static int getPoolSize(String bonusName, String poolType) {
        return rewardConfig.getInt("loginBonuses." + bonusName + ".pool." + poolType + ".poolSize");
    }

    // ランダムに報酬を取得
    public static ItemStack getRandomRewards(String bonusName, String poolType) {
        int poolSize = getPoolSize(bonusName, poolType);
        Random random = new Random();
        ItemStack item = rewardConfig.getItemStack("loginBonuses." + bonusName + ".pool." + poolType + ".rewards.slot_" + random.nextInt(poolSize));
        return item;
    }

    // すべての報酬を取得
    public static ArrayList<ItemStack> getAllRewards(String bonusName, String poolType) {
        int poolSize = getPoolSize(bonusName, poolType);
        ArrayList<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < poolSize; i++) {
            items.add(rewardConfig.getItemStack("loginBonuses." + bonusName + ".pool." + poolType + ".rewards.slot_" + i));
        }
        return items;
    }

    /////////////////////////// 報酬関連設定 ///////////////////////////
    // ボーナス枠条件設定
    public static void setBonusRewardCondition(String bonusName, int percentage) {
        rewardConfig.set("loginBonuses." + bonusName + ".bonusRewardCondition", percentage);
        try {
            rewardConfig.save(rewardFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getBonusRewardCondition(String bonusName) {
        if (rewardConfig.contains("loginBonuses." + bonusName + ".bonusRewardCondition")) {
            return rewardConfig.getInt("loginBonuses." + bonusName + ".bonusRewardCondition");
        } else {
            return 80; // デフォルト値を返す
        }
    }

    /////////////////////////// ログボ名設定 ///////////////////////////
    // 新しいログインボーナスの名前を追加
    public static boolean setNewLoginBonusName(String bonusName, Player player) {
        if (!rewardConfig.contains("loginBonuses." + bonusName)) {
            rewardConfig.createSection("loginBonuses." + bonusName);

            //以下デフォルト値
            rewardConfig.set("loginBonuses." + bonusName + ".bonusRewardCondition", 80);
            rewardConfig.set("loginBonuses." + bonusName + ".dailyResetTime", 5);

            try {
                rewardConfig.save(rewardFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }else{
            player.sendMessage("§c既に存在するログボ内部名です。ログボを編集したい場合は、前の画面の「ログボ編集」から行ってください。");
            player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
            return false;
        }
    }

    // 作成中のログボ名を再度変更
    public static boolean updateLoginBonusName(String oldName, String newName, Player player) {
        if (rewardConfig.contains("loginBonuses." + oldName) && !rewardConfig.contains("loginBonuses." + newName)) {
            Object data = rewardConfig.get("loginBonuses." + oldName);
            rewardConfig.set("loginBonuses." + oldName, null);
            rewardConfig.set("loginBonuses." + newName, data);
            try {
                rewardConfig.save(rewardFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }else{
            player.sendMessage("§c既に存在するログボ内部名です。ログボを編集したい場合は、前の画面の「ログボ編集」から行ってください。");
            player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
            return false;
        }
    }

    /////////////////////////// 時間関連設定 ///////////////////////////

    // 期間設定（開始日時と終了日時に分けてymlに保存）
    public static boolean setPeriod(String bonusName, String period, Player player) {
        try {
            // periodをハイフンで分割して開始日時と終了日時を取得
            String[] dates = period.split("-");
            if (dates.length != 2) {
                player.sendMessage("§c入力された期間の形式が正しくありません。開始日時と終了日時でハイフンを挟む必要があります。（例: 2025/03/01-2025/03/31）");
                return false;
            }

            String startDateStr = dates[0];
            String endDateStr = dates[1];

            // 日付フォーマットを定義
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

            // 日付文字列が規定の形式に従っているかを判別し、存在する日付であることを確認
            LocalDate startDate;
            LocalDate endDate;
            try {
                startDate = LocalDate.parse(startDateStr, formatter);
                endDate = LocalDate.parse(endDateStr, formatter);
            } catch (DateTimeParseException e) {
                player.sendMessage("§c日付の形式が正しくないか、存在しない日付です。（例: 2025/03/01）");
                return false;
            }

            // リセット時間を取得
            int resetHour = getDailyResetTime(bonusName);

            // 期間の重複チェック
            if (!isPeriodAvailable(bonusName, startDate, endDate, resetHour)) {
                player.sendMessage("§c指定された期間は他のログインボーナスと重複しています。別の期間を設定してください。");
                return false;
            }

            // 開始日時と終了日時をreward.ymlに保存
            rewardConfig.set("loginBonuses." + bonusName + ".startDate", startDateStr);
            rewardConfig.set("loginBonuses." + bonusName + ".endDate", endDateStr);
            rewardConfig.save(rewardFile);

            player.sendMessage("§a期間が正常に設定されました。");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage("§c期間の設定中にエラーが発生しました。");
            return false;
        }
    }

    public static LocalDate getPeriodStartDate(String bonusName) {
        String startDateStr = rewardConfig.getString("loginBonuses." + bonusName + ".startDate");
        if(startDateStr == null){
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return LocalDate.parse(startDateStr, formatter);
    }

    public static LocalDate getPeriodEndDate(String bonusName) {
        String endDateStr = rewardConfig.getString("loginBonuses." + bonusName + ".endDate");
        if(endDateStr == null){
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return LocalDate.parse(endDateStr, formatter);
    }

    public static String getOriginalPeriod(String bonusName) {
        if (rewardConfig.contains("loginBonuses." + bonusName + ".startDate") && rewardConfig.contains("loginBonuses." + bonusName + ".endDate")) {
            return rewardConfig.getString("loginBonuses." + bonusName + ".startDate") + "-" + rewardConfig.getString("loginBonuses." + bonusName + ".endDate");
        } else {
            return "期間を下の本を参考に入力";
        }
    }

    public static boolean setDailyResetTime(String bonusName, int hour, Player player) {
        if (hour < 0 || hour > 23) {
            player.sendMessage("§c時間は0~23の範囲で指定してください。");
            return false;
        }

        LocalDate startDate = getPeriodStartDate(bonusName);
        LocalDate endDate = getPeriodEndDate(bonusName);

        // 期間が設定されている場合、重複チェックを行う
        if(!(startDate == null || endDate == null)){
            if (!isPeriodAvailable(bonusName, startDate, endDate, hour)) {
                player.sendMessage("§c指定された時間は、他のログインボーナスとの期間の重複を生むため設定できません。別の時間を設定してください。");
                return false;
            }
        }


        rewardConfig.set("loginBonuses." + bonusName + ".dailyResetTime", hour);
        try {
            rewardConfig.save(rewardFile);
            player.sendMessage("§aリセット時間が正常に設定されました。");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage("§cリセット時間の設定中にエラーが発生しました。");
            return false;
        }
    }

    public static int getDailyResetTime(String bonusName) {
        if (rewardConfig.contains("loginBonuses." + bonusName + ".dailyResetTime")) {
            return rewardConfig.getInt("loginBonuses." + bonusName + ".dailyResetTime");
        } else {
            return 0; // デフォルト値を返す
        }
    }

    public static boolean isPeriodAvailable(String bonusName, LocalDate startDate, LocalDate endDate, int dailyResetHour) {
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            return false; // 開始日が終了日より後の場合は不正
        }

        // 登録済みのすべてのログインボーナスを取得
        if (rewardConfig.getConfigurationSection("loginBonuses") == null) {
            return true; // 登録されているログインボーナスがない場合は重複なし
        }

        Set<String> existingBonusNames = rewardConfig.getConfigurationSection("loginBonuses").getKeys(false);

        for (String existingName : existingBonusNames) {
            // 自分自身との比較はスキップ
            if (existingName.equals(bonusName)) {
                continue;
            }

            // 既存のログインボーナスの期間を取得
            LocalDate existingStartDate = getPeriodStartDate(existingName);
            LocalDate existingEndDate = getPeriodEndDate(existingName);
            int existingResetHour = getDailyResetTime(existingName);

            // 期間が設定されていない場合はスキップ
            if (existingStartDate == null || existingEndDate == null) {
                continue;
            }

            // 日付と時間を考慮した厳密な比較
            // 新規ボーナスの実際の開始時間
            LocalDateTime newStartDateTime = startDate.atTime(dailyResetHour, 0);
            // 新規ボーナスの実際の終了時間
            LocalDateTime newEndDateTime = endDate.plusDays(1).atTime(dailyResetHour, 0);

            // 既存ボーナスの実際の開始時間
            LocalDateTime existingStartDateTime = existingStartDate.atTime(existingResetHour, 0);
            // 既存ボーナスの実際の終了時間
            LocalDateTime existingEndDateTime = existingEndDate.plusDays(1).atTime(existingResetHour, 0);

            // 期間重複チェック
            boolean overlap = !(newEndDateTime.isBefore(existingStartDateTime) || newStartDateTime.isAfter(existingEndDateTime));

            if (overlap) {
                // 重複がある場合は常にfalseを返す（リセット時間に関係なく）
                return false;
            }
        }

        return true; // 重複なし
    }

    public static String getCurrentBonusName() {
        if(rewardConfig.getConfigurationSection("loginBonuses") == null){
            return null;
        }

        Set<String> bonusNames = rewardConfig.getConfigurationSection("loginBonuses").getKeys(false);
        LocalDate currentDate = LocalDate.now();

        for (String bonusName : bonusNames) {
            LocalDate startDate = getPeriodStartDate(bonusName);
            LocalDate endDate = getPeriodEndDate(bonusName);

            if (startDate != null && endDate != null && (currentDate.isEqual(startDate) || currentDate.isEqual(endDate) || (currentDate.isAfter(startDate) && currentDate.isBefore(endDate)))) {
                return bonusName;
            }
        }
        return null;
    }


    ////////////////////////////// 全体設定 ///////////////////////////
    ///
    public static boolean setRecoverLoginMissedDate(String bonusName, String dateStr, Player player) {
        try {
            // 日付フォーマットを定義
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

            // 日付文字列が規定の形式に従っているかを判別し、存在する日付であることを確認
            LocalDate date;
            try {
                date = LocalDate.parse(dateStr, formatter);
            } catch (DateTimeParseException e) {
                player.sendMessage("§c日付の形式が正しくないか、存在しない日付です。（例: 2025/03/01）");
                return false;
            }

            // 既存の日付リストを取得
            List<String> dateStrings = rewardConfig.getStringList("recoverLoginMissedDates");

            // 既に同じ日付が存在するかチェック
            if (dateStrings.contains(dateStr)) {
                player.sendMessage("§cこの日付は既に回復対象リストに追加されています。");
                return false;
            }

            // 新しい日付をリストに追加
            dateStrings.add(dateStr);

            // 更新したリストを保存
            rewardConfig.set("recoverLoginMissedDates", dateStrings);
            rewardConfig.save(rewardFile);

            player.sendMessage("§a日付が正常に追加されました。");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage("§c日付の設定中にエラーが発生しました。");
            return false;
        }
    }

    public static ArrayList<LocalDate> getRecoverLoginMissedDate() {
        ArrayList<LocalDate> recoverDates = new ArrayList<>();
        if (rewardConfig.contains("recoverLoginMissedDates")) {
            List<String> dateStrings = rewardConfig.getStringList("recoverLoginMissedDates");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            for (String dateString : dateStrings) {
                try {
                    LocalDate date = LocalDate.parse(dateString, formatter);
                    recoverDates.add(date);
                } catch (DateTimeParseException e) {
                    // 不正な形式の日付はスキップ
                    e.printStackTrace();
                }
            }
        }
        return recoverDates;
    }

}