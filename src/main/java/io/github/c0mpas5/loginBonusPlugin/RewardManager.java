package io.github.c0mpas5.loginBonusPlugin;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Random;

public class RewardManager {

    private static final File rewardFile = new File("plugins/LoginBonusPlugin/reward.yml");
    private static final YamlConfiguration rewardConfig = YamlConfiguration.loadConfiguration(rewardFile);

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return LocalDate.parse(startDateStr, formatter);
    }

    public static LocalDate getPeriodEndDate(String bonusName) {
        String endDateStr = rewardConfig.getString("loginBonuses." + bonusName + ".endDate");
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

}