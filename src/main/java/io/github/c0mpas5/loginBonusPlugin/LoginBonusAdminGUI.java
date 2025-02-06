package io.github.c0mpas5.loginBonusPlugin;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;


import com.github.stefvanschie.inventoryframework.pane.util.Mask;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class LoginBonusAdminGUI implements Listener {
    private ChestGui adminHomeGui;
    private ChestGui adminCreateGui;
    private ChestGui adminRewardSettingGui;
    private ChestGui adminNormalRewardSettingGui;

    public LoginBonusAdminGUI(){
        adminHomeGui();
        adminCreateGui();
        adminRewardSettingGui();
        adminNormalRewardSettingGui();
    }

    public void adminHomeGui(){
        adminHomeGui = new ChestGui(3, "ログボ管理");
        adminHomeGui.setOnGlobalClick(event -> event.setCancelled(true));

        // 上部背景
        OutlinePane upperBackground = new OutlinePane(0, 0, 9, 1, Pane.Priority.LOWEST);
        upperBackground.addItem(new GuiItem(LBItems.backgroundBlackGlassIS()));
        upperBackground.setRepeat(true);
        adminHomeGui.addPane(upperBackground);

        // 下部背景
        OutlinePane lowerBackground = new OutlinePane(0, 2, 9, 1, Pane.Priority.LOWEST);
        lowerBackground.addItem(new GuiItem(LBItems.backgroundBlackGlassIS()));
        lowerBackground.setRepeat(true);
        adminHomeGui.addPane(lowerBackground);

        // ログボ作成ボタン
        StaticPane createItemPane = new StaticPane(3, 1, 1, 1);
        createItemPane.addItem(new GuiItem(LBItems.createPickaxeIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("ログボ作成ボタンがクリックされました");
            getAdminCreateGui().show(player);
        }), 0, 0);
        adminHomeGui.addPane(createItemPane);

        // ログボ編集ボタン
        StaticPane editItemPane = new StaticPane(5, 1, 1, 1);
        editItemPane.addItem(new GuiItem(LBItems.editAnvilIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("ログボ編集ボタンがクリックされました");
        }), 0, 0);
        adminHomeGui.addPane(editItemPane);
    }

    public void adminCreateGui(){
        adminCreateGui = new ChestGui(6, "ログボ作成");
        adminCreateGui.setOnGlobalClick(event -> event.setCancelled(true));

        // 外周背景
        OutlinePane background = new OutlinePane(0, 0, 9, 6, Pane.Priority.LOWEST);
        Mask mask = new Mask(
                "111111111",
                "100000001",
                "100000001",
                "100000001",
                "100000001",
                "111101111"
        );
        background.applyMask(mask);
        background.addItem(new GuiItem(LBItems.backgroundBlackGlassIS()));
        background.setRepeat(true);
        adminCreateGui.addPane(background);

        // 保存ボタン
        StaticPane saveItemPane = new StaticPane(4, 5, 1, 1);
        saveItemPane.addItem(new GuiItem(LBItems.backgroundLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("保存がクリックされました");
        }), 0, 0);
        adminCreateGui.addPane(saveItemPane);

        // 報酬設定
        StaticPane rewardPane = new StaticPane(2, 2, 1, 1);
        rewardPane.addItem(new GuiItem(LBItems.rewardSettingChestIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("報酬設定がクリックされました");
            getAdminRewardSettingGui().show(player);
        }), 0, 0);
        adminCreateGui.addPane(rewardPane);

        // 開催期間設定
        StaticPane periodPane = new StaticPane(4, 2, 1, 1);
        periodPane.addItem(new GuiItem(LBItems.periodSettingClockIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("開催期間設定がクリックされました");
        }), 0, 0);
        adminCreateGui.addPane(periodPane);

        // バナー設定
        StaticPane bannerPane = new StaticPane(6, 2, 1, 1);
        bannerPane.addItem(new GuiItem(LBItems.bannerSettingPaintingIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("バナー設定がクリックされました");
        }), 0, 0);
        adminCreateGui.addPane(bannerPane);

        // 内部名設定
        StaticPane namePane = new StaticPane(3, 3, 1, 1);
        namePane.addItem(new GuiItem(LBItems.nameSettingNametagIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("内部名設定がクリックされました");
        }), 0, 0);
        adminCreateGui.addPane(namePane);

        // その他の設定
        StaticPane otherPane = new StaticPane(5, 3, 1, 1);
        otherPane.addItem(new GuiItem(LBItems.otherSettingBookIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("その他の設定がクリックされました");
        }), 0, 0);
        adminCreateGui.addPane(otherPane);

        // 読込
        StaticPane loadPane = new StaticPane(7, 4, 1, 1);
        loadPane.addItem(new GuiItem(LBItems.loadBookShelfIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("読込がクリックされました");
        }), 0, 0);
        adminCreateGui.addPane(loadPane);
    }

    public void adminRewardSettingGui(){
        adminRewardSettingGui = new ChestGui(3, "報酬設定");
        adminRewardSettingGui.setOnGlobalClick(event -> event.setCancelled(true));

        // 外周背景
        OutlinePane background = new OutlinePane(0, 0, 9, 3, Pane.Priority.LOWEST);
        Mask mask = new Mask(
                "111111111",
                "000000000",
                "111111111"
        );
        background.applyMask(mask);
        background.addItem(new GuiItem(LBItems.backgroundBlackGlassIS()));
        background.setRepeat(true);
        adminRewardSettingGui.addPane(background);

        // 通常枠設定
        StaticPane normalRewardPane = new StaticPane(0, 1, 1, 1);
        normalRewardPane.addItem(new GuiItem(LBItems.normalRewardPlayerHeadIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("通常報酬がクリックされました");
            getAdminNormalRewardSettingGui().show(player);
        }), 0, 0);
        adminRewardSettingGui.addPane(normalRewardPane);

        // 特別枠設定
        StaticPane specialRewardPane = new StaticPane(1, 1, 1, 1);
        specialRewardPane.addItem(new GuiItem(LBItems.specialRewardPlayerHeadIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("特別報酬がクリックされました");
        }), 0, 0);
        adminRewardSettingGui.addPane(specialRewardPane);

        // ボーナス枠設定
        StaticPane bonusRewardPane = new StaticPane(2, 1, 1, 1);
        bonusRewardPane.addItem(new GuiItem(LBItems.bonusRewardPlayerHeadIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("ボーナス報酬がクリックされました");
        }), 0, 0);
        adminRewardSettingGui.addPane(bonusRewardPane);

        // 連続ログインボーナス設定
        StaticPane continuousRewardPane = new StaticPane(3, 1, 1, 1);
        continuousRewardPane.addItem(new GuiItem(LBItems.continuousRewardPlayerHeadIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("連続ログインボーナスがクリックされました");
        }), 0, 0);
        adminRewardSettingGui.addPane(continuousRewardPane);

        // ボーナス枠条件設定
        StaticPane bonusRewardConditionPane = new StaticPane(4, 1, 1, 1);
        bonusRewardConditionPane.addItem(new GuiItem(LBItems.bonusRewardConditionCommandBlockIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("ボーナス枠条件設定がクリックされました");
        }), 0, 0);
        adminRewardSettingGui.addPane(bonusRewardConditionPane);

        // サブ垢設定
        StaticPane subAccountPane = new StaticPane(5, 1, 1, 1);
        subAccountPane.addItem(new GuiItem(LBItems.subAccountSettingPaperIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("サブ垢設定がクリックされました");
        }), 0, 0);
        adminRewardSettingGui.addPane(subAccountPane);

        // チュートリアル
        StaticPane tutorialPane = new StaticPane(8, 1, 1, 1);
        tutorialPane.addItem(new GuiItem(LBItems.rewardSettingTutorialBookIS(), event -> {
            // 仮置き
            Player player = (Player) event.getWhoClicked();
            ItemStack item = RewardManager.loadRandomRewards("normal");
            player.getInventory().addItem(item);
            player.sendMessage("報酬が付与されました");
        }), 0, 0);
        adminRewardSettingGui.addPane(tutorialPane);
    }

    public void adminNormalRewardSettingGui(){
        adminNormalRewardSettingGui = new ChestGui(6, "通常枠報酬設定");

        // 外周背景
        OutlinePane background = new OutlinePane(0, 0, 9, 6, Pane.Priority.LOWEST);
        Mask mask = new Mask(
                "000000000",
                "000000000",
                "000000000",
                "000000000",
                "000000000",
                "111101110"
        );
        background.applyMask(mask);
        background.addItem(new GuiItem(LBItems.backgroundBlackGlassIS(), event -> {
            event.setCancelled(true);
        }));
        background.setRepeat(true);
        adminNormalRewardSettingGui.addPane(background);

        // 保存ボタン
        StaticPane saveItemPane = new StaticPane(4, 5, 1, 1);
        saveItemPane.addItem(new GuiItem(LBItems.backgroundLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("保存がクリックされました");
            RewardManager.deleteRewardsInfo("normal");

            ArrayList<ItemStack> items = new ArrayList<>();
            for (int i = 0; i <= 44; i++){
                if(adminNormalRewardSettingGui.getInventory().getItem(i) == null){
                    break;
                }
                items.add(adminNormalRewardSettingGui.getInventory().getItem(i));
            }
            RewardManager.saveRewards("normal", items);
            player.sendMessage("報酬が保存されました");

            getAdminRewardSettingGui().show(player);
        }), 0, 0);
        adminNormalRewardSettingGui.addPane(saveItemPane);

        // チュートリアル
        StaticPane tutorialPane = new StaticPane(8, 5, 1, 1);
        tutorialPane.addItem(new GuiItem(LBItems.rewardSettingTutorialBookIS(), event -> {
            event.setCancelled(true);
        }), 0, 0);
        adminNormalRewardSettingGui.addPane(tutorialPane);
    }

    public ChestGui getAdminHomeGui(){
        return adminHomeGui;
    }

    public ChestGui getAdminCreateGui(){
        return adminCreateGui;
    }

    public ChestGui getAdminRewardSettingGui(){
        return adminRewardSettingGui;
    }

    public ChestGui getAdminNormalRewardSettingGui(){
        return adminNormalRewardSettingGui;
    }
}