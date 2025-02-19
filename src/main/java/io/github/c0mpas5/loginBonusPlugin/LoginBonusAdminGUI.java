package io.github.c0mpas5.loginBonusPlugin;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.AnvilGui;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;


import com.github.stefvanschie.inventoryframework.pane.component.Slider;
import com.github.stefvanschie.inventoryframework.pane.util.Mask;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class LoginBonusAdminGUI implements Listener {
    private ChestGui adminHomeGui;
    private AnvilGui adminFirstNameSettingGui;
    private ChestGui adminCreateGui;
    private ChestGui adminRewardSettingGui;
    private ChestGui adminNormalRewardSettingGui;
    private ChestGui adminSpecialRewardSettingGui;
    private ChestGui adminBonusRewardSettingGui;
    private ChestGui adminContinuousRewardSettingGui;
    private AnvilGui adminBonusRewardConditionGui;
    private ChestGui adminSubAccountSettingGui;

    private ChestGui adminTestGui;

    public LoginBonusAdminGUI(){
        adminHomeGui();
        adminFirstNameSettingGui();
        adminCreateGui();
        adminRewardSettingGui();
        adminNormalRewardSettingGui();
        adminSpecialRewardSettingGui();
        adminBonusRewardSettingGui();
        adminContinuousRewardSettingGui();
        adminBonusRewardConditionGui();
        //adminSubAccountSettingGui();

        adminTestGui();
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
            getAdminFirstNameSettingGui().show(player);
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

    public void adminFirstNameSettingGui(){
        adminFirstNameSettingGui = new AnvilGui("新規ログボ名設定");
        adminFirstNameSettingGui.setOnGlobalClick(event -> event.setCancelled(true));

        // 左端の適当なアイテム
        StaticPane paperPane = new StaticPane(0, 0, 1, 1);

        paperPane.addItem(new GuiItem(LBItems.firstNamePaperIS(), event -> {
        }), 0, 0);
        adminFirstNameSettingGui.getFirstItemComponent().addPane(paperPane);

        // チュートリアル
        StaticPane tutorialPane = new StaticPane(0, 0, 1, 1);
        tutorialPane.addItem(new GuiItem(LBItems.firstNameSettingTutorialBookIS(), event -> {
        }), 0, 0);
        adminFirstNameSettingGui.getSecondItemComponent().addPane(tutorialPane);

        // 保存
        StaticPane saveItemPane = new StaticPane(0, 0, 1, 1);
        saveItemPane.addItem(new GuiItem(LBItems.backgroundLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            String inputText = adminFirstNameSettingGui.getRenameText();
            if (!inputText.isEmpty()) {
                //inputTextをreward.ymlに保存する処理

                player.sendMessage("設定が保存されました");
                player.playSound(player.getLocation(), "minecraft:block.note_block.harp", 1.0f, 1.0f);
                getAdminCreateGui().show(player);
            } else {
                player.sendMessage("入力が空白です。1文字以上入力してください。");
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
            }
        }), 0, 0);
        adminFirstNameSettingGui.getResultComponent().addPane(saveItemPane);
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
            getAdminSpecialRewardSettingGui().show(player);
        }), 0, 0);
        adminRewardSettingGui.addPane(specialRewardPane);

        // ボーナス枠設定
        StaticPane bonusRewardPane = new StaticPane(2, 1, 1, 1);
        bonusRewardPane.addItem(new GuiItem(LBItems.bonusRewardPlayerHeadIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("ボーナス報酬がクリックされました");
            getAdminBonusRewardSettingGui().show(player);
        }), 0, 0);
        adminRewardSettingGui.addPane(bonusRewardPane);

        // 連続ログインボーナス設定
        StaticPane continuousRewardPane = new StaticPane(3, 1, 1, 1);
        continuousRewardPane.addItem(new GuiItem(LBItems.continuousRewardPlayerHeadIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("連続ログインボーナスがクリックされました");
            getAdminContinuousRewardSettingGui().show(player);
        }), 0, 0);
        adminRewardSettingGui.addPane(continuousRewardPane);

        // ボーナス枠条件設定
        StaticPane bonusRewardConditionPane = new StaticPane(4, 1, 1, 1);
        bonusRewardConditionPane.addItem(new GuiItem(LBItems.bonusRewardConditionCommandBlockIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("ボーナス枠条件設定がクリックされました");
            getAdminBonusRewardConditionGui().show(player);
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
            ItemStack item = RewardManager.getRandomRewards("normal");
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
        tutorialPane.addItem(new GuiItem(LBItems.normalRewardSettingTutorialBookIS(), event -> {
            event.setCancelled(true);
        }), 0, 0);
        adminNormalRewardSettingGui.addPane(tutorialPane);
    }

    public void adminSpecialRewardSettingGui(){
        adminSpecialRewardSettingGui = new ChestGui(6, "特別枠報酬設定");

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
        adminSpecialRewardSettingGui.addPane(background);

        // 保存ボタン
        StaticPane saveItemPane = new StaticPane(4, 5, 1, 1);
        saveItemPane.addItem(new GuiItem(LBItems.backgroundLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("保存がクリックされました");
            RewardManager.deleteRewardsInfo("special");

            ArrayList<ItemStack> items = new ArrayList<>();
            for (int i = 0; i <= 44; i++){
                if(adminSpecialRewardSettingGui.getInventory().getItem(i) == null){
                    break;
                }
                items.add(adminSpecialRewardSettingGui.getInventory().getItem(i));
            }
            RewardManager.saveRewards("special", items);
            player.sendMessage("報酬が保存されました");

            getAdminRewardSettingGui().show(player);
        }), 0, 0);
        adminSpecialRewardSettingGui.addPane(saveItemPane);

        // チュートリアル
        StaticPane tutorialPane = new StaticPane(8, 5, 1, 1);
        tutorialPane.addItem(new GuiItem(LBItems.specialRewardSettingTutorialBookIS(), event -> {
            event.setCancelled(true);
        }), 0, 0);
        adminSpecialRewardSettingGui.addPane(tutorialPane);
    }

    public void adminBonusRewardSettingGui(){
        adminBonusRewardSettingGui = new ChestGui(6, "ボーナス枠報酬設定");

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
        adminBonusRewardSettingGui.addPane(background);

        // 保存ボタン
        StaticPane saveItemPane = new StaticPane(4, 5, 1, 1);
        saveItemPane.addItem(new GuiItem(LBItems.backgroundLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("保存がクリックされました");
            RewardManager.deleteRewardsInfo("bonus");

            ArrayList<ItemStack> items = new ArrayList<>();
            for (int i = 0; i <= 44; i++){
                if(adminBonusRewardSettingGui.getInventory().getItem(i) == null){
                    break;
                }
                items.add(adminBonusRewardSettingGui.getInventory().getItem(i));
            }
            RewardManager.saveRewards("bonus", items);
            player.sendMessage("報酬が保存されました");

            getAdminRewardSettingGui().show(player);
        }), 0, 0);
        adminBonusRewardSettingGui.addPane(saveItemPane);

        // チュートリアル
        StaticPane tutorialPane = new StaticPane(8, 5, 1, 1);
        tutorialPane.addItem(new GuiItem(LBItems.bonusRewardSettingTutorialBookIS(), event -> {
            event.setCancelled(true);
        }), 0, 0);
        adminBonusRewardSettingGui.addPane(tutorialPane);
    }

    public void adminContinuousRewardSettingGui(){
        adminContinuousRewardSettingGui = new ChestGui(6, "連続ログボ報酬設定");

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
        adminContinuousRewardSettingGui.addPane(background);

        // 保存ボタン
        StaticPane saveItemPane = new StaticPane(4, 5, 1, 1);
        saveItemPane.addItem(new GuiItem(LBItems.backgroundLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("保存がクリックされました");
            RewardManager.deleteRewardsInfo("continuous");

            ArrayList<ItemStack> items = new ArrayList<>();
            for (int i = 0; i <= 44; i++){
                if(adminContinuousRewardSettingGui.getInventory().getItem(i) == null){
                    break;
                }
                items.add(adminContinuousRewardSettingGui.getInventory().getItem(i));
            }
            RewardManager.saveRewards("continuous", items);
            player.sendMessage("報酬が保存されました");

            getAdminRewardSettingGui().show(player);
        }), 0, 0);
        adminContinuousRewardSettingGui.addPane(saveItemPane);

        // チュートリアル
        StaticPane tutorialPane = new StaticPane(8, 5, 1, 1);
        tutorialPane.addItem(new GuiItem(LBItems.continuousRewardSettingTutorialBookIS(), event -> {
            event.setCancelled(true);
        }), 0, 0);
        adminContinuousRewardSettingGui.addPane(tutorialPane);
    }

    public void adminBonusRewardConditionGui(){
        adminBonusRewardConditionGui = new AnvilGui("ボーナス枠条件設定");
        adminBonusRewardConditionGui.setOnGlobalClick(event -> event.setCancelled(true));

        // 左端の適当なアイテム
        StaticPane paperPane = new StaticPane(0, 0, 1, 1);
        ItemStack paper = LBItems.defaultNamePaperIS();
        ItemMeta paperMeta = paper.getItemMeta();
        paperMeta.setDisplayName(String.valueOf(RewardManager.getBonusRewardCondition()) + " / 0~100（整数）");
        paper.setItemMeta(paperMeta);

        paperPane.addItem(new GuiItem(paper, event -> {
        }), 0, 0);
        adminBonusRewardConditionGui.getFirstItemComponent().addPane(paperPane);

        // チュートリアル
        StaticPane tutorialPane = new StaticPane(0, 0, 1, 1);
        tutorialPane.addItem(new GuiItem(LBItems.bonusRewardConditionTutorialBookIS(), event -> {
        }), 0, 0);
        adminBonusRewardConditionGui.getSecondItemComponent().addPane(tutorialPane);

        // 保存
        StaticPane saveItemPane = new StaticPane(0, 0, 1, 1);
        saveItemPane.addItem(new GuiItem(LBItems.backgroundLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            String inputText = adminBonusRewardConditionGui.getRenameText();
            if (inputText.matches("\\d+") && Integer.parseInt(inputText) >= 0 && Integer.parseInt(inputText) <= 100) {
                int percentage = Integer.parseInt(inputText);
                RewardManager.setBonusRewardCondition(percentage);
                player.sendMessage("設定が保存されました");
                player.playSound(player.getLocation(), "minecraft:block.note_block.harp", 1.0f, 1.0f);
                getAdminRewardSettingGui().show(player);
            } else {
                player.sendMessage("入力が無効です。0~100の整数を入力してください。");
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
            }
        }), 0, 0);
        adminBonusRewardConditionGui.getResultComponent().addPane(saveItemPane);

//        adminBonusRewardConditionGui = new ChestGui(7, "ボーナス枠条件設定");
//        adminRewardSettingGui.setOnGlobalClick(event -> event.setCancelled(true));
//
//        // 外周背景
//        OutlinePane background = new OutlinePane(0, 0, 9, 7, Pane.Priority.LOWEST);
//        Mask mask = new Mask(
//                "000000000",
//                "111111111",
//                "111000111",
//                "111000111",
//                "111000111",
//                "111101111",
//                "111101110"
//        );
//        background.applyMask(mask);
//        background.addItem(new GuiItem(LBItems.backgroundBlackGlassIS()));
//        background.setRepeat(true);
//        adminRewardSettingGui.addPane(background);
//
//        // 数字入力
//        Label input = new Label(3, 2, 3, 4, Font.LIGHT_GRAY);
//        Mask inputMask = new Mask(
//                "111",
//                "111",
//                "111",
//                "010"
//        );
//        background.applyMask(inputMask);
//        background.addItem(new GuiItem(LBItems.backgroundBlackGlassIS()));
//        background.setRepeat(true);
//        int inputValue = 0;
//
//        input.setText("7894561230", (character, item) -> new GuiItem(item, event -> {
//            // event
//        }));
//        // 保存ボタン
//        StaticPane saveItemPane = new StaticPane(4, 5, 1, 1);
//        saveItemPane.addItem(new GuiItem(LBItems.backgroundLimeGlassIS(), event -> {
//            Player player = (Player) event.getWhoClicked();
//            player.sendMessage("保存がクリックされました");
//            // 保存処理
//            player.sendMessage("設定が保存されました");
//
//            getAdminRewardSettingGui().show(player);
//        }), 0, 0);
//        adminBonusRewardConditionGui.addPane(saveItemPane);
//
//        // チュートリアル
//        StaticPane tutorialPane = new StaticPane(8, 5, 1, 1);
//        tutorialPane.addItem(new GuiItem(LBItems.bonusRewardConditionTutorialBookIS(), event -> {
//            event.setCancelled(true);
//        }), 0, 0);
//        adminBonusRewardConditionGui.addPane(tutorialPane);
    }

    public void adminTestGui(){
        adminTestGui = new ChestGui(6, "テスト");
        adminTestGui.setOnGlobalClick(event -> event.setCancelled(true));

        Slider slider = new Slider(0, 0, 9, 6);
        slider.setValue(0.5f);
        adminTestGui.addPane(slider);
    }

    public ChestGui getAdminHomeGui(){
        return adminHomeGui;
    }

    public AnvilGui getAdminFirstNameSettingGui(){
        return adminFirstNameSettingGui;
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

    public ChestGui getAdminSpecialRewardSettingGui(){
        return adminSpecialRewardSettingGui;
    }

    public ChestGui getAdminBonusRewardSettingGui(){
        return adminBonusRewardSettingGui;
    }

    public ChestGui getAdminContinuousRewardSettingGui(){
        return adminContinuousRewardSettingGui;
    }

    public AnvilGui getAdminBonusRewardConditionGui(){
        return adminBonusRewardConditionGui;
    }

    public ChestGui getAdminTestGui(){
        return adminTestGui;
    }
}