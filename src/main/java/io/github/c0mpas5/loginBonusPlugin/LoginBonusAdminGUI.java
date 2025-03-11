package io.github.c0mpas5.loginBonusPlugin;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.AnvilGui;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;


import com.github.stefvanschie.inventoryframework.pane.component.Slider;
import com.github.stefvanschie.inventoryframework.pane.util.Mask;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

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
    private ChestGui adminTimeSettingGui;
    private AnvilGui adminPeriodSettingGui;
    private AnvilGui adminDailyResetTimeSettingGui;
    private AnvilGui adminNameSettingGui;
    private ChestGui adminSubAccountSettingGui;

    private ChestGui adminTestGui;
    
    private String currentLoginBonusName;

    public LoginBonusAdminGUI() {
        adminHomeGui();
        adminFirstNameSettingGui();
        adminCreateGui();
        adminRewardSettingGui();
        adminNormalRewardSettingGui();
        adminSpecialRewardSettingGui();
        adminBonusRewardSettingGui();
        adminContinuousRewardSettingGui();
        adminBonusRewardConditionGui();
        adminTimeSettingGui();
        adminPeriodSettingGui();
        adminDailyResetTimeSettingGui();
        adminNameSettingGui();
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
        adminFirstNameSettingGui = new AnvilGui("新規ログボ内部名設定");
        adminFirstNameSettingGui.setOnGlobalClick(event -> event.setCancelled(true));

        // 左端の適当なアイテム（キャンセル兼用）
        StaticPane cancelPane = new StaticPane(0, 0, 1, 1);
        ItemStack cancel = LBItems.cancelRedGlassIS();
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName("作成したいログボの名前を入力");
        cancelMeta.setLore(List.of(
                ChatColor.RED + "[左クリック] ログボ作成を中止します。"
        ));
        cancel.setItemMeta(cancelMeta);
        cancelPane.addItem(new GuiItem(cancel, event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("キャンセルがクリックされました");
            player.closeInventory();
        }), 0, 0);
        adminFirstNameSettingGui.getFirstItemComponent().addPane(cancelPane);

        // チュートリアル
        StaticPane tutorialPane = new StaticPane(0, 0, 1, 1);
        tutorialPane.addItem(new GuiItem(LBItems.firstNameSettingTutorialBookIS(), event -> {
        }), 0, 0);
        adminFirstNameSettingGui.getSecondItemComponent().addPane(tutorialPane);

        // 保存
        StaticPane saveItemPane = new StaticPane(0, 0, 1, 1);
        saveItemPane.addItem(new GuiItem(LBItems.saveLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            String inputText = adminFirstNameSettingGui.getRenameText();
            // 空白でないかつ、入力されたログボ名が既存のものと重複していない場合
            if (!inputText.isEmpty() && RewardManager.setNewLoginBonusName(inputText, player)) {
                player.sendMessage("§a新しいログボが作成されました");
                currentLoginBonusName = inputText;
                player.playSound(player.getLocation(), "minecraft:block.note_block.harp", 1.0f, 1.0f);
                getAdminCreateGui().show(player);
            } else {
                player.sendMessage("入力が空白です。1文字以上入力してください。");
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
            }
        }), 0, 0);
        adminFirstNameSettingGui.getResultComponent().addPane(saveItemPane);
    }

    public void adminCreateGui() {
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
                "011101111"
        );
        background.applyMask(mask);
        background.addItem(new GuiItem(LBItems.backgroundBlackGlassIS()));
        background.setRepeat(true);
        adminCreateGui.addPane(background);

        // 保存ボタン
        StaticPane saveItemPane = new StaticPane(4, 5, 1, 1);
        saveItemPane.addItem(new GuiItem(LBItems.saveLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("保存がクリックされました");
            player.closeInventory();
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

        // 時間系設定
        StaticPane timePane = new StaticPane(4, 2, 1, 1);
        timePane.addItem(new GuiItem(LBItems.timeSettingClockIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("時間系設定がクリックされました");
            getAdminTimeSettingGui().show(player);
        }), 0, 0);
        adminCreateGui.addPane(timePane);

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
            getAdminNameSettingGui().show(player);
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

        StaticPane cancelItemPane = new StaticPane(0, 5, 1, 1);
        cancelItemPane.addItem(new GuiItem(LBItems.cancelRedGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("キャンセルがクリックされました");
            player.closeInventory();
        }), 0, 0);
        adminCreateGui.addPane(cancelItemPane);
    }


////////////////////// 報酬系設定（「ログボ作成orログボ編集」>「報酬設定」>... 部分） //////////////////////
    public void adminRewardSettingGui(){
        adminRewardSettingGui = new ChestGui(3, "報酬設定");
        adminRewardSettingGui.setOnGlobalClick(event -> event.setCancelled(true));

        // 外周背景
        OutlinePane background = new OutlinePane(0, 0, 9, 3, Pane.Priority.LOWEST);
        Mask mask = new Mask(
                "111111111",
                "000000000",
                "111101111"
        );
        background.applyMask(mask);
        background.addItem(new GuiItem(LBItems.backgroundBlackGlassIS()));
        background.setRepeat(true);
        adminRewardSettingGui.addPane(background);

        // 通常枠設定
        StaticPane normalRewardPane = new StaticPane(0, 1, 1, 1);
        normalRewardPane.addItem(new GuiItem(LBItems.normalRewardPoolPlayerHeadIS(), event -> {
            adminNormalRewardSettingGui.update();
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("通常報酬がクリックされました");
            updateAdminNormalRewardSettingGui();
            getAdminNormalRewardSettingGui().show(player);
        }), 0, 0);
        adminRewardSettingGui.addPane(normalRewardPane);

        // 特別枠設定
        StaticPane specialRewardPane = new StaticPane(1, 1, 1, 1);
        specialRewardPane.addItem(new GuiItem(LBItems.specialRewardPoolPlayerHeadIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("特別報酬がクリックされました");
            getAdminSpecialRewardSettingGui().show(player);
        }), 0, 0);
        adminRewardSettingGui.addPane(specialRewardPane);

        // ボーナス枠設定
        StaticPane bonusRewardPane = new StaticPane(2, 1, 1, 1);
        bonusRewardPane.addItem(new GuiItem(LBItems.bonusRewardPoolPlayerHeadIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("ボーナス報酬がクリックされました");
            getAdminBonusRewardSettingGui().show(player);
        }), 0, 0);
        adminRewardSettingGui.addPane(bonusRewardPane);

        // 連続ログインボーナス設定
        StaticPane continuousRewardPane = new StaticPane(3, 1, 1, 1);
        continuousRewardPane.addItem(new GuiItem(LBItems.continuousRewardPoolPlayerHeadIS(), event -> {
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
        }), 0, 0);
        adminRewardSettingGui.addPane(tutorialPane);

        //保存ボタン
        StaticPane saveItemPane = new StaticPane(0, 2, 1, 1);
        saveItemPane.addItem(new GuiItem(LBItems.saveLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("保存がクリックされました");
            getAdminCreateGui().show(player);
        }), 0, 0);
        adminRewardSettingGui.addPane(saveItemPane);
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
                "011101110"
        );
        background.applyMask(mask);
        background.addItem(new GuiItem(LBItems.backgroundBlackGlassIS(), event -> {
            event.setCancelled(true);
        }));
        background.setRepeat(true);
        adminNormalRewardSettingGui.addPane(background);

        // 保存ボタン
        StaticPane saveItemPane = new StaticPane(4, 5, 1, 1);
        saveItemPane.addItem(new GuiItem(LBItems.saveLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("保存がクリックされました");
            RewardManager.deleteRewardsInfo(currentLoginBonusName, "normal");

            ArrayList<ItemStack> items = new ArrayList<>();
            for (int i = 0; i <= 44; i++){ // 45スロットまで
                if(adminNormalRewardSettingGui.getInventory().getItem(i) == null){
                    break;
                }
                items.add(adminNormalRewardSettingGui.getInventory().getItem(i));
            }
            RewardManager.saveRewards(currentLoginBonusName, "normal", items);
            player.sendMessage("報酬が保存されました");

            getAdminRewardSettingGui().show(player);
        }), 0, 0);
        adminNormalRewardSettingGui.addPane(saveItemPane);

        //キャンセルボタン
        StaticPane cancelItemPane = new StaticPane(0, 5, 1, 1);
        cancelItemPane.addItem(new GuiItem(LBItems.cancelRedGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("キャンセルがクリックされました");
            getAdminRewardSettingGui().show(player);
        }), 0, 0);
        adminNormalRewardSettingGui.addPane(cancelItemPane);

        // チュートリアル
        StaticPane tutorialPane = new StaticPane(8, 5, 1, 1);
        tutorialPane.addItem(new GuiItem(LBItems.normalRewardSettingTutorialBookIS(), event -> {
            event.setCancelled(true);
        }), 0, 0);
        adminNormalRewardSettingGui.addPane(tutorialPane);

        // 登録中のアイテムを並べる
        OutlinePane rewardItemPane = new OutlinePane(0, 0, 9, 5, Pane.Priority.NORMAL);
        ArrayList<ItemStack> rewardItems = RewardManager.getAllRewards(currentLoginBonusName, "normal");
        for (int i = 0; i < rewardItems.size(); i++) {
            ItemStack item = rewardItems.get(i);
            rewardItemPane.addItem(new GuiItem(item));
       }
        adminNormalRewardSettingGui.addPane(rewardItemPane);
        Bukkit.broadcastMessage("normalの報酬リストを更新しました");
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
                "011101110"
        );
        background.applyMask(mask);
        background.addItem(new GuiItem(LBItems.backgroundBlackGlassIS(), event -> {
            event.setCancelled(true);
        }));
        background.setRepeat(true);
        adminSpecialRewardSettingGui.addPane(background);

        // 保存ボタン
        StaticPane saveItemPane = new StaticPane(4, 5, 1, 1);
        saveItemPane.addItem(new GuiItem(LBItems.saveLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("保存がクリックされました");
            RewardManager.deleteRewardsInfo(currentLoginBonusName, "special");

            ArrayList<ItemStack> items = new ArrayList<>();
            for (int i = 0; i <= 44; i++){
                if(adminSpecialRewardSettingGui.getInventory().getItem(i) == null){
                    break;
                }
                items.add(adminSpecialRewardSettingGui.getInventory().getItem(i));
            }
            RewardManager.saveRewards(currentLoginBonusName, "special", items);
            player.sendMessage("報酬が保存されました");

            getAdminRewardSettingGui().show(player);
        }), 0, 0);
        adminSpecialRewardSettingGui.addPane(saveItemPane);

        //キャンセルボタン
        StaticPane cancelItemPane = new StaticPane(0, 5, 1, 1);
        cancelItemPane.addItem(new GuiItem(LBItems.cancelRedGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("キャンセルがクリックされました");
            getAdminRewardSettingGui().show(player);
        }), 0, 0);
        adminSpecialRewardSettingGui.addPane(cancelItemPane);

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
                "011101110"
        );
        background.applyMask(mask);
        background.addItem(new GuiItem(LBItems.backgroundBlackGlassIS(), event -> {
            event.setCancelled(true);
        }));
        background.setRepeat(true);
        adminBonusRewardSettingGui.addPane(background);

        // 保存ボタン
        StaticPane saveItemPane = new StaticPane(4, 5, 1, 1);
        saveItemPane.addItem(new GuiItem(LBItems.saveLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("保存がクリックされました");
            RewardManager.deleteRewardsInfo(currentLoginBonusName, "bonus");

            ArrayList<ItemStack> items = new ArrayList<>();
            for (int i = 0; i <= 44; i++){
                if(adminBonusRewardSettingGui.getInventory().getItem(i) == null){
                    break;
                }
                items.add(adminBonusRewardSettingGui.getInventory().getItem(i));
            }
            RewardManager.saveRewards(currentLoginBonusName, "bonus", items);
            player.sendMessage("報酬が保存されました");

            getAdminRewardSettingGui().show(player);
        }), 0, 0);
        adminBonusRewardSettingGui.addPane(saveItemPane);

        //キャンセルボタン
        StaticPane cancelItemPane = new StaticPane(0, 5, 1, 1);
        cancelItemPane.addItem(new GuiItem(LBItems.cancelRedGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("キャンセルがクリックされました");
            getAdminRewardSettingGui().show(player);
        }), 0, 0);
        adminBonusRewardSettingGui.addPane(cancelItemPane);

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
                "011101110"
        );
        background.applyMask(mask);
        background.addItem(new GuiItem(LBItems.backgroundBlackGlassIS(), event -> {
            event.setCancelled(true);
        }));
        background.setRepeat(true);
        adminContinuousRewardSettingGui.addPane(background);

        // 保存ボタン
        StaticPane saveItemPane = new StaticPane(4, 5, 1, 1);
        saveItemPane.addItem(new GuiItem(LBItems.saveLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("保存がクリックされました");
            RewardManager.deleteRewardsInfo(currentLoginBonusName, "continuous");

            ArrayList<ItemStack> items = new ArrayList<>();
            for (int i = 0; i <= 44; i++){
                if(adminContinuousRewardSettingGui.getInventory().getItem(i) == null){
                    break;
                }
                items.add(adminContinuousRewardSettingGui.getInventory().getItem(i));
            }
            RewardManager.saveRewards(currentLoginBonusName, "continuous", items);
            player.sendMessage("報酬が保存されました");

            getAdminRewardSettingGui().show(player);
        }), 0, 0);
        adminContinuousRewardSettingGui.addPane(saveItemPane);

        //キャンセルボタン
        StaticPane cancelItemPane = new StaticPane(0, 5, 1, 1);
        cancelItemPane.addItem(new GuiItem(LBItems.cancelRedGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("キャンセルがクリックされました");
            getAdminRewardSettingGui().show(player);
        }), 0, 0);
        adminContinuousRewardSettingGui.addPane(cancelItemPane);

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

        // 左端の適当なアイテム（キャンセル兼用）
        StaticPane cancelPane = new StaticPane(0, 0, 1, 1);
        ItemStack cancel = LBItems.cancelRedGlassIS();
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName(String.valueOf(RewardManager.getBonusRewardCondition(currentLoginBonusName)) + " / 0~100（整数）");
        cancel.setItemMeta(cancelMeta);

        cancelPane.addItem(new GuiItem(cancel, event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("キャンセルがクリックされました");
            getAdminRewardSettingGui().show(player);
        }), 0, 0);
        adminBonusRewardConditionGui.getFirstItemComponent().addPane(cancelPane);

        // チュートリアル
        StaticPane tutorialPane = new StaticPane(0, 0, 1, 1);
        tutorialPane.addItem(new GuiItem(LBItems.bonusRewardConditionSettingTutorialBookIS(), event -> {
        }), 0, 0);
        adminBonusRewardConditionGui.getSecondItemComponent().addPane(tutorialPane);

        // 保存
        StaticPane saveItemPane = new StaticPane(0, 0, 1, 1);
        saveItemPane.addItem(new GuiItem(LBItems.saveLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            String inputText = adminBonusRewardConditionGui.getRenameText();
            if (inputText.matches("\\d+") && Integer.parseInt(inputText) >= 0 && Integer.parseInt(inputText) <= 100) {
                int percentage = Integer.parseInt(inputText);
                RewardManager.setBonusRewardCondition(currentLoginBonusName, percentage);
                player.sendMessage("設定が保存されました");
                player.playSound(player.getLocation(), "minecraft:block.note_block.harp", 1.0f, 1.0f);
                getAdminRewardSettingGui().show(player);
            } else {
                player.sendMessage("入力が無効です。0~100の整数を入力してください。");
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
            }
        }), 0, 0);
        adminBonusRewardConditionGui.getResultComponent().addPane(saveItemPane);
    }

////////////////////// 期間系設定（「ログボ作成orログボ編集」>「報酬設定」>... 部分） //////////////////////
    public void adminTimeSettingGui(){
        adminTimeSettingGui = new ChestGui(3, "時間系設定");
        adminTimeSettingGui.setOnGlobalClick(event -> event.setCancelled(true));

        OutlinePane background = new OutlinePane(0, 0, 9, 3, Pane.Priority.LOWEST);
        Mask mask = new Mask(
                "111111111",
                "000000000",
                "011111111"
        );
        background.applyMask(mask);
        background.addItem(new GuiItem(LBItems.backgroundBlackGlassIS(), event -> {
            event.setCancelled(true);
        }));
        background.setRepeat(true);
        adminTimeSettingGui.addPane(background);

        // 期間設定
        StaticPane createItemPane = new StaticPane(3, 1, 1, 1);
        createItemPane.addItem(new GuiItem(LBItems.periodSettingClockIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("期間設定くりっくされた");
            getAdminPeriodSettingGui().show(player);
        }), 0, 0);
        adminTimeSettingGui.addPane(createItemPane);

        // 日付変更時刻設定
        StaticPane editItemPane = new StaticPane(5, 1, 1, 1);
        editItemPane.addItem(new GuiItem(LBItems.dailyResetTimeSettingCompassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("日付変更時刻設定がクリックされました");
            getAdminDailyResetTimeSettingGui().show(player);
        }), 0, 0);
        adminTimeSettingGui.addPane(editItemPane);

        //キャンセルボタン
        StaticPane cancelItemPane = new StaticPane(0, 2, 1, 1);
        cancelItemPane.addItem(new GuiItem(LBItems.cancelRedGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("キャンセルがクリックされました");
            getAdminCreateGui().show(player);
        }), 0, 0);
        adminTimeSettingGui.addPane(cancelItemPane);
    }

    public void adminPeriodSettingGui(){
        adminPeriodSettingGui = new AnvilGui("ログボ期間設定");
        adminPeriodSettingGui.setOnGlobalClick(event -> event.setCancelled(true));

        // 左端の適当なアイテム（キャンセル兼用）
        StaticPane cancelPane = new StaticPane(0, 0, 1, 1);
        ItemStack cancel = LBItems.cancelRedGlassIS();
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName(RewardManager.getOriginalPeriod(currentLoginBonusName));
        cancel.setItemMeta(cancelMeta);

        cancelPane.addItem(new GuiItem(cancel, event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("キャンセルがクリックされました");
            getAdminTimeSettingGui().show(player);
        }), 0, 0);
        adminPeriodSettingGui.getFirstItemComponent().addPane(cancelPane);

        // チュートリアル
        StaticPane tutorialPane = new StaticPane(0, 0, 1, 1);
        tutorialPane.addItem(new GuiItem(LBItems.periodSettingTutorialBookIS(), event -> {
        }), 0, 0);
        adminPeriodSettingGui.getSecondItemComponent().addPane(tutorialPane);

        // 保存
        StaticPane saveItemPane = new StaticPane(0, 0, 1, 1);
        saveItemPane.addItem(new GuiItem(LBItems.saveLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            String inputText = adminPeriodSettingGui.getRenameText();
            if (!inputText.isEmpty() && RewardManager.setPeriod(currentLoginBonusName, inputText, player)) {
                //player.sendMessage("設定が保存されました");
                player.playSound(player.getLocation(), "minecraft:block.note_block.harp", 1.0f, 1.0f);
                getAdminCreateGui().show(player);
            } else {
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
            }
        }), 0, 0);
        adminPeriodSettingGui.getResultComponent().addPane(saveItemPane);
    }

    public void adminDailyResetTimeSettingGui(){
        adminDailyResetTimeSettingGui = new AnvilGui("ログボ日付変更時刻設定");
        adminDailyResetTimeSettingGui.setOnGlobalClick(event -> event.setCancelled(true));

        // 左端の適当なアイテム（キャンセル兼用）
        StaticPane cancelPane = new StaticPane(0, 0, 1, 1);
        ItemStack cancel = LBItems.cancelRedGlassIS();
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName(RewardManager.getDailyResetTime(currentLoginBonusName) + " / 0~23（整数）");
        cancel.setItemMeta(cancelMeta);

        cancelPane.addItem(new GuiItem(cancel, event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("キャンセルがクリックされました");
            getAdminTimeSettingGui().show(player);
        }), 0, 0);
        adminDailyResetTimeSettingGui.getFirstItemComponent().addPane(cancelPane);

        // チュートリアル
        StaticPane tutorialPane = new StaticPane(0, 0, 1, 1);
        tutorialPane.addItem(new GuiItem(LBItems.dailyResetTimeTutorialBookIS(), event -> {
        }), 0, 0);
        adminDailyResetTimeSettingGui.getSecondItemComponent().addPane(tutorialPane);

        // 保存
        StaticPane saveItemPane = new StaticPane(0, 0, 1, 1);
        saveItemPane.addItem(new GuiItem(LBItems.saveLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            String inputText = adminDailyResetTimeSettingGui.getRenameText();
            if (inputText.matches("\\D+")) {
                player.sendMessage("§c入力が無効です。数字のみを入力してください。");
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
            } else if (!inputText.isEmpty() && RewardManager.setDailyResetTime(currentLoginBonusName, Integer.parseInt(inputText), player)) {
                player.playSound(player.getLocation(), "minecraft:block.note_block.harp", 1.0f, 1.0f);
                getAdminCreateGui().show(player);
            } else {
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
            }
        }), 0, 0);
        adminDailyResetTimeSettingGui.getResultComponent().addPane(saveItemPane);
    }

    public void adminNameSettingGui(){
        adminNameSettingGui = new AnvilGui("ログボ内部名設定");
        adminNameSettingGui.setOnGlobalClick(event -> event.setCancelled(true));

        // 左端の適当なアイテム（キャンセル兼用）
        StaticPane cancelPane = new StaticPane(0, 0, 1, 1);
        ItemStack cancel = LBItems.cancelRedGlassIS();
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName(currentLoginBonusName);
        cancel.setItemMeta(cancelMeta);

        cancelPane.addItem(new GuiItem(cancel, event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("キャンセルがクリックされました");
            getAdminCreateGui().show(player);
        }), 0, 0);
        adminNameSettingGui.getFirstItemComponent().addPane(cancelPane);

        // チュートリアル
        StaticPane tutorialPane = new StaticPane(0, 0, 1, 1);
        tutorialPane.addItem(new GuiItem(LBItems.firstNameSettingTutorialBookIS(), event -> {
        }), 0, 0);
        adminNameSettingGui.getSecondItemComponent().addPane(tutorialPane);

        // 保存
        StaticPane saveItemPane = new StaticPane(0, 0, 1, 1);
        saveItemPane.addItem(new GuiItem(LBItems.saveLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            String inputText = adminNameSettingGui.getRenameText();
            if (!inputText.isEmpty() && RewardManager.updateLoginBonusName(currentLoginBonusName, inputText, player)) {
                currentLoginBonusName = inputText;
                player.sendMessage("設定が保存されました");
                player.playSound(player.getLocation(), "minecraft:block.note_block.harp", 1.0f, 1.0f);
                getAdminCreateGui().show(player);
            } else {
                player.sendMessage("入力が空白です。1文字以上入力してください。");
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
            }
        }), 0, 0);
        adminNameSettingGui.getResultComponent().addPane(saveItemPane);
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

    public void updateAdminNormalRewardSettingGui() {
        adminNormalRewardSettingGui.getPanes().removeIf(pane -> pane instanceof OutlinePane && pane.getPriority() == Pane.Priority.NORMAL);

        // 新しいrewardItemPaneを追加
        OutlinePane rewardItemPane = new OutlinePane(0, 0, 9, 5, Pane.Priority.NORMAL);
        ArrayList<ItemStack> rewardItems = RewardManager.getAllRewards(currentLoginBonusName, "normal");
        for (int i = 0; i < rewardItems.size(); i++) {
            ItemStack item = rewardItems.get(i);
            // GuiItemを作成
            GuiItem guiItem = new GuiItem(item);
            rewardItemPane.addItem(guiItem);
        }
        adminNormalRewardSettingGui.addPane(rewardItemPane);
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

    public ChestGui getAdminTimeSettingGui(){
        return adminTimeSettingGui;
    }

    public AnvilGui getAdminPeriodSettingGui(){
        return adminPeriodSettingGui;
    }

    public AnvilGui getAdminDailyResetTimeSettingGui(){
        return adminDailyResetTimeSettingGui;
    }

    public AnvilGui getAdminNameSettingGui(){
        return adminNameSettingGui;
    }

    public ChestGui getAdminTestGui(){
        return adminTestGui;
    }
}