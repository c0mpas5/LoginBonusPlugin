package io.github.c0mpas5.loginBonusPlugin;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
    private static String messagePrefix = "§6§l[LoginBonusPlugin] §r";

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

            // デフォルトの設定を保存
            rewardConfig.set("accountRewardLimit", 2);

            try {
                rewardConfig.save(rewardFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /////////////////////////// プールの保存・読込・サイズカウント ///////////////////////////
    // 保存
    public static void saveRewards(String bonusName, String poolType, ArrayList<ItemStack> items) {
        for (int i = 0; i < items.size(); i++) {
            String itemStr = itemToBase64(items.get(i));
            rewardConfig.set("loginBonuses." + bonusName + ".pool." + poolType + ".rewards.slot_" + i, itemStr);
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
        String itemStr = rewardConfig.getString("loginBonuses." + bonusName + ".pool." + poolType + ".rewards.slot_" + random.nextInt(poolSize));
        return itemFromBase64(itemStr);
    }

    // すべての報酬を取得
    public static ArrayList<ItemStack> getAllRewards(String bonusName, String poolType) {
        int poolSize = getPoolSize(bonusName, poolType);
        ArrayList<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < poolSize; i++) {
            String itemStr = rewardConfig.getString("loginBonuses." + bonusName + ".pool." + poolType + ".rewards.slot_" + i);
            items.add(itemFromBase64(itemStr));
        }
        return items;
    }

    public static boolean loadAllPool(String refBonusName, String targetBonusName) {
        // 参照元のプールが存在するか確認
        if (!rewardConfig.contains("loginBonuses." + refBonusName + ".pool")) {
            return false;
        }

        try {
            // 参照元のプール設定全体を取得
            Object poolData = rewardConfig.get("loginBonuses." + refBonusName + ".pool");

            // 対象のログインボーナスにプール設定をコピー
            rewardConfig.set("loginBonuses." + targetBonusName + ".pool", poolData);

            // 変更を保存
            rewardConfig.save(rewardFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ItemStackをBase64に変換
    private static String itemToBase64(ItemStack item) throws IllegalStateException {
        if (item == null || item.getType() == Material.AIR) return null;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            ItemStack[] items = new ItemStack[1];
            items[0] = item;
            dataOutput.writeInt(items.length);

            for (int i = 0; i < items.length; i++) {
                dataOutput.writeObject(items[i]);
            }

            dataOutput.close();

            return Base64Coder.encodeLines(outputStream.toByteArray());

        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    // Base64からItemStackを取得
    private static ItemStack itemFromBase64(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            // シリアライズされたインベントリを読み込む
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items[0];
        } catch (Exception e) {
            return null;
        }
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
        if (!rewardConfig.contains("loginBonuses." + bonusName) && bonusName.getBytes().length <= 48) {
            rewardConfig.createSection("loginBonuses." + bonusName);

            //以下デフォルト値
            rewardConfig.set("loginBonuses." + bonusName + ".bonusRewardCondition", 80);
            rewardConfig.set("loginBonuses." + bonusName + ".dailyResetTime", 5);
            rewardConfig.set("loginBonuses." + bonusName + ".status", "disabled");

            try {
                rewardConfig.save(rewardFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }else{
            player.sendMessage(messagePrefix + "§c既に存在するログボ内部名です。ログボを編集したい場合は、前の画面の「ログボ編集」から行ってください。");
            return false;
        }
    }

    // 作成中のログボ名を再度変更
    public static boolean updateLoginBonusName(String oldName, String newName, Player player) {
        if (rewardConfig.contains("loginBonuses." + oldName) && !rewardConfig.contains("loginBonuses." + newName) && newName.getBytes().length <= 48) {
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
            player.sendMessage(messagePrefix + "§c既に存在するログボ内部名です。ログボを編集したい場合は、前の画面の「ログボ編集」から行ってください。");
            return false;
        }
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

            if (startDate != null && endDate != null && (currentDate.isEqual(startDate) || currentDate.isEqual(endDate) || (currentDate.isAfter(startDate) && currentDate.isBefore(endDate))) && isLBEnabled(bonusName)) {
                return bonusName;
            }
        }
        return null;
    }

    public static ArrayList<String> getAllBonusNames() {
        ArrayList<String> bonusNames = new ArrayList<>();
        if (rewardConfig.getConfigurationSection("loginBonuses") != null) {
            Set<String> keys = rewardConfig.getConfigurationSection("loginBonuses").getKeys(false);
            for (String key : keys) {
                bonusNames.add(key);
            }
        }
        return bonusNames;
    }

    // ログインボーナスの削除（指定したボーナス名とその関連データを全て削除）
    public static boolean deleteLoginBonus(String bonusName) {
        if (rewardConfig.contains("loginBonuses." + bonusName)) {
            rewardConfig.set("loginBonuses." + bonusName, null);
            try {
                rewardConfig.save(rewardFile);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }


    /////////////////////////// 時間関連設定 ///////////////////////////

    // 期間設定（開始日時と終了日時に分けてymlに保存）
    public static boolean setPeriod(String bonusName, String period, Player player) {
        try {
            // periodをハイフンで分割して開始日時と終了日時を取得
            String[] dates = period.split("-");
            if (dates.length != 2) {
                player.sendMessage(messagePrefix + "§c入力された期間の形式が正しくありません。開始日時と終了日時でハイフンを挟む必要があります。（例: 2025/03/01-2025/03/31）");
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
                player.sendMessage(messagePrefix + "§c日付の形式が正しくないか、存在しない日付です。（例: 2025/03/01）");
                return false;
            }

            // リセット時間を取得
            int resetHour = getDailyResetTime(bonusName);

            // 期間の重複チェック
            if (!isPeriodAvailable(bonusName, startDate, endDate, resetHour)) {
                player.sendMessage(messagePrefix + "§c指定された期間は他のログインボーナスと重複しています。別の期間を設定してください。");
                return false;
            }

            // 開始日時と終了日時をreward.ymlに保存
            rewardConfig.set("loginBonuses." + bonusName + ".startDate", startDateStr);
            rewardConfig.set("loginBonuses." + bonusName + ".endDate", endDateStr);
            rewardConfig.save(rewardFile);

            player.sendMessage(messagePrefix + "§a期間が正常に設定されました。");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage(messagePrefix + "§c期間の設定中にエラーが発生しました。");
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
            player.sendMessage(messagePrefix + "§c時間は0~23の範囲で指定してください。");
            return false;
        }

        LocalDate startDate = getPeriodStartDate(bonusName);
        LocalDate endDate = getPeriodEndDate(bonusName);

        // 期間が設定されている場合、重複チェックを行う
        if(!(startDate == null || endDate == null)){
            if (!isPeriodAvailable(bonusName, startDate, endDate, hour)) {
                player.sendMessage(messagePrefix + "§c指定された時間は、他のログインボーナスとの期間の重複を生むため設定できません。別の時間を設定してください。");
                return false;
            }
        }


        rewardConfig.set("loginBonuses." + bonusName + ".dailyResetTime", hour);
        try {
            rewardConfig.save(rewardFile);
            player.sendMessage(messagePrefix + "§aリセット時間が正常に設定されました。");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage(messagePrefix + "§cリセット時間の設定中にエラーが発生しました。");
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

    // ログボの有効・無効を切り替え
    public static boolean setStatus(String bonusName, String currentStatus) {
        if(!isAllSettingsComplete(bonusName)){
            rewardConfig.set("loginBonuses." + bonusName + ".status", "disabled");
            try {
                rewardConfig.save(rewardFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        if(currentStatus.equals("disabled")) {
            rewardConfig.set("loginBonuses." + bonusName + ".status", "enabled");
        } else if (currentStatus.equals("enabled")) {
            rewardConfig.set("loginBonuses." + bonusName + ".status", "disabled");
        }
        try {
            rewardConfig.save(rewardFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isLBEnabled(String bonusName) {
        return rewardConfig.getString("loginBonuses." + bonusName + ".status").equals("enabled");
    }

    // bonusName以下のすべての項目が設定されているかチェック
    public static boolean isAllSettingsComplete(String bonusName) {
        if (rewardConfig.contains("loginBonuses." + bonusName)) {
            Set<String> upperKeys = rewardConfig.getConfigurationSection("loginBonuses." + bonusName).getKeys(false);

            if (rewardConfig.contains("loginBonuses." + bonusName + ".pool")){
                Set<String> poolKeys = rewardConfig.getConfigurationSection("loginBonuses." + bonusName + ".pool").getKeys(false);
                return upperKeys.contains("bonusRewardCondition") &&
                        upperKeys.contains("dailyResetTime") &&
                        upperKeys.contains("startDate") &&
                        upperKeys.contains("endDate") &&
                        upperKeys.contains("pool") &&
                        poolKeys.contains("normal") &&
                        poolKeys.contains("special") &&
                        poolKeys.contains("bonus") &&
                        poolKeys.contains("continuous");
            } else{
                return false; // poolが存在しない場合はそもそもfalse
            }
        } else {
            return false; // 存在しないボーナス名の場合はfalse
        }
    }

    public static ArrayList<String> getLackingSettings(String bonusName) {
        ArrayList<String> missingSettings = new ArrayList<>();
        if (rewardConfig.contains("loginBonuses." + bonusName)) {
            Set<String> upperKeys = rewardConfig.getConfigurationSection("loginBonuses." + bonusName).getKeys(false);

            if (rewardConfig.contains("loginBonuses." + bonusName + ".pool")){
                Set<String> poolKeys = rewardConfig.getConfigurationSection("loginBonuses." + bonusName + ".pool").getKeys(false);
                if (!upperKeys.contains("bonusRewardCondition")) {
                    missingSettings.add("報酬設定-ボーナス枠条件設定");
                }
                if (!upperKeys.contains("dailyResetTime")) {
                    missingSettings.add("時間系設定-日付変更時刻設定");
                }
                if (!upperKeys.contains("startDate")) {
                    missingSettings.add("時間系設定-開催期間設定");
                }
                if (!poolKeys.contains("normal")) {
                    missingSettings.add("通常枠報酬設定");
                }
                if (!poolKeys.contains("special")) {
                    missingSettings.add("特別枠報酬設定");
                }
                if (!poolKeys.contains("bonus")) {
                    missingSettings.add("ボーナス枠報酬設定");
                }
                if (!poolKeys.contains("continuous")) {
                    missingSettings.add("連続ログボ報酬設定");
                }
            }else{
                if (!upperKeys.contains("bonusRewardCondition")) {
                    missingSettings.add("報酬設定-ボーナス枠条件設定");
                }
                if (!upperKeys.contains("dailyResetTime")) {
                    missingSettings.add("時間系設定-日付変更時刻設定");
                }
                if (!upperKeys.contains("startDate")) {
                    missingSettings.add("時間系設定-開催期間設定");
                }
                // poolが存在しない場合は、すべての報酬設定が不足しているとみなす
                missingSettings.add("通常枠報酬設定");
                missingSettings.add("特別枠報酬設定");
                missingSettings.add("ボーナス枠報酬設定");
                missingSettings.add("連続ログボ報酬設定");
            }
        } else {
            missingSettings.add(messagePrefix + "§c存在しないログインボーナス名です。");
        }
        return missingSettings;
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
                player.sendMessage(messagePrefix + "§c日付の形式が正しくないか、存在しない日付です。（例: 2025/03/01）");
                return false;
            }

            // 既存の日付リストを取得
            List<String> dateStrings = rewardConfig.getStringList("recoverLoginMissedDates");

            // 既に同じ日付が存在するかチェック
            if (dateStrings.contains(dateStr)) {
                dateStrings.remove(dateStr); // 既存の日付を削除
                player.sendMessage(messagePrefix + "§eこの日付（" + dateStr + "は既にログイン情報修正日リストに追加されていたため、リストから削除します");
            } else{
                // 新しい日付をリストに追加
                dateStrings.add(dateStr);
                player.sendMessage(messagePrefix + "§e" + dateStr + "をログイン情報修正日リストに追加します");
            }

            // 更新したリストを保存
            rewardConfig.set("recoverLoginMissedDates", dateStrings);
            rewardConfig.save(rewardFile);

            player.sendMessage(messagePrefix + "§a処理が正常に終了しました。");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage(messagePrefix + "§c日付の設定中にエラーが発生しました。");
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

    public static boolean setAccountRewardLimit(int accountRewardLimit, Player player) {
        if (accountRewardLimit < 0) {
            player.sendMessage(messagePrefix + "§cアカウント数は0以上の整数で指定してください。");
            return false;
        }

        rewardConfig.set("accountRewardLimit", accountRewardLimit);
        try {
            rewardConfig.save(rewardFile);
            player.sendMessage(messagePrefix + "§aアカウント数が正常に設定されました。");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage(messagePrefix + "§cアカウント数の設定中にエラーが発生しました。");
            return false;
        }
    }

    public static int getAccountRewardLimit() {
        if (rewardConfig.contains("accountRewardLimit")) {
            return rewardConfig.getInt("accountRewardLimit");
        } else {
            return 1; // デフォルト値を返す
        }
    }

}