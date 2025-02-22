package io.github.c0mpas5.loginBonusPlugin;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
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

    // 新しいログインボーナスを追加
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
}