package io.github.c0mpas5.loginBonusPlugin;

import org.bukkit.configuration.file.YamlConfiguration;
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
    public static void saveRewards(String poolType, ArrayList<ItemStack> items) {
        for (int i = 0; i < items.size(); i++) {
            rewardConfig.set("pool." + poolType + ".rewards.slot_" + i, items.get(i));
        }
        rewardConfig.set("pool." + poolType + ".poolSize", items.size());
        try {
            rewardConfig.save(rewardFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 指定したプールの情報を削除
    public static void deleteRewardsInfo(String poolType) {
        rewardConfig.set("pool." + poolType, null);
        try {
            rewardConfig.save(rewardFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // プールサイズ読込
    public static int getPoolSize(String poolType) {
        return rewardConfig.getInt("pool." + poolType + ".poolSize");
    }

    // ランダムに報酬を取得
    public static ItemStack getRandomRewards(String poolType) {
        int poolSize = getPoolSize(poolType);
        Random random = new Random();
        ItemStack item = rewardConfig.getItemStack("pool." + poolType + ".rewards.slot_" + random.nextInt(poolSize));
        return item;
    }

    /////////////////////////// 報酬関連設定 ///////////////////////////
    // ボーナス枠条件設定
    public static void setBonusRewardCondition(int percentage) {
        rewardConfig.set("bonusRewardCondition", percentage);
        try {
            rewardConfig.save(rewardFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getBonusRewardCondition() {
        if (rewardConfig.contains("bonusRewardCondition")) {
            return rewardConfig.getInt("bonusRewardCondition");
        } else {
            return 80; // デフォルト値を返す
        }
    }
}