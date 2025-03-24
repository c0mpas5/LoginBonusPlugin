package io.github.c0mpas5.loginBonusPlugin;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.AnvilGui;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
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

import java.lang.reflect.Array;
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
    private ChestGui adminOtherSettingGui;
    private ChestGui adminSubAccountSettingGui;

    private ChestGui adminEditGui;
    private ChestGui adminDeleteConfirmGui;

    private ChestGui adminGlobalSettingGui;
    private AnvilGui adminRecoverDateSettingGui;

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
        adminOtherSettingGui();
        //adminSubAccountSettingGui();

        adminEditGui();
        adminDeleteConfirmGui();

        adminGlobalSettingGui();
        adminRecoverDateSettingGui();

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
        StaticPane createItemPane = new StaticPane(2, 1, 1, 1);
        createItemPane.addItem(new GuiItem(LBItems.createPickaxeIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("ログボ作成ボタンがクリックされました");
            updateAdminCreateGui(false, null);
            getAdminFirstNameSettingGui().show(player);
        }), 0, 0);
        adminHomeGui.addPane(createItemPane);

        // ログボ編集ボタン
        StaticPane editItemPane = new StaticPane(4, 1, 1, 1);
        editItemPane.addItem(new GuiItem(LBItems.editAnvilIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("ログボ編集ボタンがクリックされました");
            updateAdminEditGui();
            getAdminEditGui().show(player);
        }), 0, 0);
        adminHomeGui.addPane(editItemPane);

        // グローバル設定
        StaticPane globalSettingPane = new StaticPane(6, 1, 1, 1);
        globalSettingPane.addItem(new GuiItem(LBItems.globalSettingEnderEyeIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("グローバル設定がクリックされました");
            getAdminGlobalSettingGui().show(player);
        }), 0, 0);
        adminHomeGui.addPane(globalSettingPane);
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
        StaticPane timePane = new StaticPane(4, 2, 1, 1, Pane.Priority.HIGH);
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
            updateAdminNameSettingGui();
            getAdminNameSettingGui().show(player);
        }), 0, 0);
        adminCreateGui.addPane(namePane);

        // その他の設定
        StaticPane otherPane = new StaticPane(5, 3, 1, 1);
        otherPane.addItem(new GuiItem(LBItems.otherSettingBookIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("その他の設定がクリックされました");
            updateAndShowAdminOtherSettingGui(player);
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
            updateAdminSpecialRewardSettingGui();
            getAdminSpecialRewardSettingGui().show(player);
        }), 0, 0);
        adminRewardSettingGui.addPane(specialRewardPane);

        // ボーナス枠設定
        StaticPane bonusRewardPane = new StaticPane(2, 1, 1, 1);
        bonusRewardPane.addItem(new GuiItem(LBItems.bonusRewardPoolPlayerHeadIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("ボーナス報酬がクリックされました");
            updateAdminBonusRewardSettingGui();
            getAdminBonusRewardSettingGui().show(player);
        }), 0, 0);
        adminRewardSettingGui.addPane(bonusRewardPane);

        // 連続ログインボーナス設定
        StaticPane continuousRewardPane = new StaticPane(3, 1, 1, 1);
        continuousRewardPane.addItem(new GuiItem(LBItems.continuousRewardPoolPlayerHeadIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("連続ログインボーナスがクリックされました");
            updateAdminContinuousRewardSettingGui();
            getAdminContinuousRewardSettingGui().show(player);
        }), 0, 0);
        adminRewardSettingGui.addPane(continuousRewardPane);

        // ボーナス枠条件設定
        StaticPane bonusRewardConditionPane = new StaticPane(4, 1, 1, 1);
        bonusRewardConditionPane.addItem(new GuiItem(LBItems.bonusRewardConditionCommandBlockIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("ボーナス枠条件設定がクリックされました");
            updateAdminBonusRewardConditionGui();
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
        StaticPane saveItemPane = new StaticPane(4, 2, 1, 1);
        saveItemPane.addItem(new GuiItem(LBItems.returnLimeGlassIS(), event -> {
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

        // 登録中のアイテムを並べる
        OutlinePane rewardItemPane = new OutlinePane(0, 0, 9, 5, Pane.Priority.NORMAL);
        ArrayList<ItemStack> rewardItems = RewardManager.getAllRewards(currentLoginBonusName, "reward");
        for (int i = 0; i < rewardItems.size(); i++) {
            ItemStack item = rewardItems.get(i);
            rewardItemPane.addItem(new GuiItem(item));
        }
        adminSpecialRewardSettingGui.addPane(rewardItemPane);
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

        // 登録中のアイテムを並べる
        OutlinePane rewardItemPane = new OutlinePane(0, 0, 9, 5, Pane.Priority.NORMAL);
        ArrayList<ItemStack> rewardItems = RewardManager.getAllRewards(currentLoginBonusName, "bonus");
        for (int i = 0; i < rewardItems.size(); i++) {
            ItemStack item = rewardItems.get(i);
            rewardItemPane.addItem(new GuiItem(item));
        }
        adminBonusRewardSettingGui.addPane(rewardItemPane);
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

        // 登録中のアイテムを並べる
        OutlinePane rewardItemPane = new OutlinePane(0, 0, 9, 5, Pane.Priority.NORMAL);
        ArrayList<ItemStack> rewardItems = RewardManager.getAllRewards(currentLoginBonusName, "continuous");
        for (int i = 0; i < rewardItems.size(); i++) {
            ItemStack item = rewardItems.get(i);
            rewardItemPane.addItem(new GuiItem(item));
        }
        adminContinuousRewardSettingGui.addPane(rewardItemPane);
    }

    public void adminBonusRewardConditionGui(){
        adminBonusRewardConditionGui = new AnvilGui("ボーナス枠条件設定");
        adminBonusRewardConditionGui.setOnGlobalClick(event -> event.setCancelled(true));

        // 左端の適当なアイテム（キャンセル兼用）
        StaticPane cancelPane = new StaticPane(0, 0, 1, 1, Pane.Priority.HIGH);
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
            updateAdminPeriodSettingGui();
            getAdminPeriodSettingGui().show(player);
        }), 0, 0);
        adminTimeSettingGui.addPane(createItemPane);

        // 日付変更時刻設定
        StaticPane editItemPane = new StaticPane(5, 1, 1, 1);
        editItemPane.addItem(new GuiItem(LBItems.dailyResetTimeSettingCompassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("日付変更時刻設定がクリックされました");
            updateAdminDailyResetTimeSettingGui();
            getAdminDailyResetTimeSettingGui().show(player);
        }), 0, 0);
        adminTimeSettingGui.addPane(editItemPane);

        //キャンセルボタン
        StaticPane returnItemPane = new StaticPane(0, 2, 1, 1);
        returnItemPane.addItem(new GuiItem(LBItems.returnLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("前に戻るがクリックされました");
            getAdminCreateGui().show(player);
        }), 0, 0);
        adminTimeSettingGui.addPane(returnItemPane);
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

    public void adminOtherSettingGui(){
        adminOtherSettingGui = new ChestGui(3, "その他の設定");
        adminOtherSettingGui.setOnGlobalClick(event -> event.setCancelled(true));

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
        adminOtherSettingGui.addPane(background);

        //保存ボタン
        StaticPane saveItemPane = new StaticPane(4, 2, 1, 1);
        saveItemPane.addItem(new GuiItem(LBItems.returnLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("保存がクリックされました");
            getAdminCreateGui().show(player);
        }), 0, 0);
        adminOtherSettingGui.addPane(saveItemPane);
    }

    public void updateAndShowAdminOtherSettingGui(Player targetPlayer){
        adminOtherSettingGui.getPanes().removeIf(pane -> pane.getPriority() == Pane.Priority.HIGH);

        // status設定
        StaticPane statusPane = new StaticPane(0, 1, 1, 1, Pane.Priority.HIGH);
        if(RewardManager.isLBEnabled(currentLoginBonusName)){
            statusPane.addItem(new GuiItem(LBItems.enabledEmeraldBlockIS(), event -> {
                Player player = (Player) event.getWhoClicked();
                if(RewardManager.setStatus(currentLoginBonusName, "enabled")){
                    player.sendMessage("§aログボが無効化されました");
                    updateAndShowAdminOtherSettingGui(player);
                } else {
                    player.sendMessage("§c以下の設定を先に終える必要があります");
                    ArrayList<String> lackedSettings = RewardManager.getLackingSettings(currentLoginBonusName);
                    for (String setting : lackedSettings) {
                        player.sendMessage("§c・" + setting);
                    }
                }
            }), 0, 0);

        }else{
            statusPane.addItem(new GuiItem(LBItems.disabledRedStoneBlockIS(), event -> {
                Player player = (Player) event.getWhoClicked();
                if(RewardManager.setStatus(currentLoginBonusName, "disabled")){
                    player.sendMessage("§aログボが有効化されました");
                    updateAndShowAdminOtherSettingGui(player);
                } else {
                    player.sendMessage("§c以下の設定を先に終える必要があります");
                    ArrayList<String> lackedSettings = RewardManager.getLackingSettings(currentLoginBonusName);
                    for (String setting : lackedSettings) {
                        player.sendMessage("§c・" + setting);
                    }
                }
            }), 0, 0);
        }
        adminOtherSettingGui.addPane(statusPane);
        adminOtherSettingGui.update(); //要るんか知らん
        targetPlayer.closeInventory(); //一回消さないと更新入らないので
        adminOtherSettingGui.show(targetPlayer);
    }

    ///////////////// ログボ編集系 /////////////////
    public void adminEditGui(){
        ArrayList<String> bonusNames = RewardManager.getAllBonusNames();
        int bonusNameCount = bonusNames.size();
        adminEditGui = new ChestGui(6, "ログボ編集");
        adminEditGui.setOnGlobalClick(event -> event.setCancelled(true));

        if(bonusNames.isEmpty()){
            StaticPane errorPane = new StaticPane(3, 2, 1, 1);
            errorPane.addItem(new GuiItem(LBItems.noLoginBonusNamePaperIS(), event -> {
            }), 0, 0);
            adminEditGui.addPane(errorPane);

            StaticPane returnPane = new StaticPane(5, 2, 1, 1);
            returnPane.addItem(new GuiItem(LBItems.returnLimeGlassIS(), event -> {
                Player player = (Player) event.getWhoClicked();
                player.sendMessage("前に戻るがクリックされました");
                getAdminHomeGui().show(player);
            }), 0, 0);
            adminEditGui.addPane(returnPane);

            return;
        }

        //全ページ
        PaginatedPane paginatedPane = new PaginatedPane(0, 0, 9, 6);

        ItemStack[] chests = new ItemStack[bonusNameCount];

        // 表示するchestの配列作る
        for(int i = 0; i < bonusNameCount; i++){
            String bonusName = bonusNames.get(i);
            chests[i] = LBItems.loginBonusNameChestIS(bonusName, RewardManager.getOriginalPeriod(bonusName));
        }

        int pageCount = (bonusNameCount / 28) + 1; //28=1ページに表示するアイテム数

        // 外周背景
        OutlinePane background = new OutlinePane(0, 0, 9, 6, Pane.Priority.LOWEST);
        Mask mask = new Mask(
                "111111111",
                "100000001",
                "100000001",
                "100000001",
                "100000001",
                "010101110"
        );
        background.applyMask(mask);
        background.addItem(new GuiItem(LBItems.backgroundBlackGlassIS()));
        background.setRepeat(true);

        // 次のページ
        StaticPane nextPagePane = new StaticPane(8, 5, 1, 1);
        nextPagePane.addItem(new GuiItem(LBItems.nextPageAquaPlayerHeadIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            if (paginatedPane.getPage() < pageCount - 1) {
                paginatedPane.setPage(paginatedPane.getPage() + 1);
                adminEditGui.update();
            } else {
                player.sendMessage("§cこのページが末尾のページであるため、次ページを開けません。");
            }
        }), 0, 0);

        // 前のページ
        StaticPane prevPagePane = new StaticPane(0, 5, 1, 1);
        prevPagePane.addItem(new GuiItem(LBItems.prevPageAquaPlayerHeadIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            if (paginatedPane.getPage() > 0) {
                paginatedPane.setPage(paginatedPane.getPage() - 1);
                adminEditGui.update();
            } else {
                player.sendMessage("§cこのページが先頭のページであるため、前ページを開けません。");
            }
        }), 0, 0);

        StaticPane returnPane = new StaticPane(2, 5, 1, 1);
        returnPane.addItem(new GuiItem(LBItems.returnLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("前に戻るがクリックされました");
            getAdminHomeGui().show(player);
        }), 0, 0);

        // 表示するplayerHead、現在のページ数の表示を各ページに追加
        OutlinePane[] chestsPanes = new OutlinePane[pageCount];
        StaticPane[] currentPageCountPane = new StaticPane[pageCount];
        for(int i = 0; i < pageCount; i++){
            // 各ページで表示が変わらんところは同じように表示
            paginatedPane.addPane(i, background);
            paginatedPane.addPane(i, nextPagePane);
            paginatedPane.addPane(i, prevPagePane);
            paginatedPane.addPane(i, returnPane);

            // 現在のページ数の表示
            currentPageCountPane[i] = new StaticPane(4, 5, 1, 1);
            currentPageCountPane[i].addItem(new GuiItem(LBItems.displayPageCountGlassIS(i + 1, pageCount), event -> {
            }), 0, 0);
            paginatedPane.addPane(i, currentPageCountPane[i]);

            chestsPanes[i] = new OutlinePane(1, 1, 7, 4);
            for (int j = 0; j < 28; j++) {
                int index = i * 28 + j;

                // playerHeadを表示し終えたらbreak
                if (index >= bonusNameCount) {
                    break;
                }

                chestsPanes[i].addItem(new GuiItem(chests[index], event -> {
                    Player player = (Player) event.getWhoClicked();
                    if (event.isLeftClick()) {
                        String editingBonusName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());;
                        currentLoginBonusName = editingBonusName;
                        updateAdminCreateGui(true, editingBonusName);
                        getAdminCreateGui().show(player);
                    } else if (event.isRightClick()) {
                        //削除処理するgui
                        updateAdminDeleteConfirmGui(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
                        getAdminDeleteConfirmGui().show(player);
                    }
                }));
            }
            paginatedPane.addPane(i, chestsPanes[i]);
        }
        adminEditGui.addPane(paginatedPane);
    }

    public void updateAdminEditGui(){
        adminEditGui.getPanes().clear();

        ArrayList<String> bonusNames = RewardManager.getAllBonusNames();
        int bonusNameCount = bonusNames.size();

        if(bonusNames.isEmpty()){
            StaticPane errorPane = new StaticPane(3, 2, 1, 1);
            errorPane.addItem(new GuiItem(LBItems.noLoginBonusNamePaperIS(), event -> {
            }), 0, 0);
            adminEditGui.addPane(errorPane);

            StaticPane returnPane = new StaticPane(5, 2, 1, 1);
            returnPane.addItem(new GuiItem(LBItems.returnLimeGlassIS(), event -> {
                Player player = (Player) event.getWhoClicked();
                player.sendMessage("前に戻るがクリックされました");
                getAdminHomeGui().show(player);
            }), 0, 0);
            adminEditGui.addPane(returnPane);

            return;
        }

        //全ページ
        PaginatedPane paginatedPane = new PaginatedPane(0, 0, 9, 6);

        ItemStack[] chests = new ItemStack[bonusNameCount];

        // 表示するchestの配列作る
        for(int i = 0; i < bonusNameCount; i++){
            String bonusName = bonusNames.get(i);
            chests[i] = LBItems.loginBonusNameChestIS(bonusName, RewardManager.getOriginalPeriod(bonusName));
        }

        int pageCount = (bonusNameCount / 28) + 1; //28=1ページに表示するアイテム数

        // 外周背景
        OutlinePane background = new OutlinePane(0, 0, 9, 6, Pane.Priority.LOWEST);
        Mask mask = new Mask(
                "111111111",
                "100000001",
                "100000001",
                "100000001",
                "100000001",
                "010101110"
        );
        background.applyMask(mask);
        background.addItem(new GuiItem(LBItems.backgroundBlackGlassIS()));
        background.setRepeat(true);

        // 次のページ
        StaticPane nextPagePane = new StaticPane(8, 5, 1, 1);
        nextPagePane.addItem(new GuiItem(LBItems.nextPageAquaPlayerHeadIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            if (paginatedPane.getPage() < pageCount - 1) {
                paginatedPane.setPage(paginatedPane.getPage() + 1);
                adminEditGui.update();
            } else {
                player.sendMessage("§cこのページが末尾のページであるため、次ページを開けません。");
            }
        }), 0, 0);

        // 前のページ
        StaticPane prevPagePane = new StaticPane(0, 5, 1, 1);
        prevPagePane.addItem(new GuiItem(LBItems.prevPageAquaPlayerHeadIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            if (paginatedPane.getPage() > 0) {
                paginatedPane.setPage(paginatedPane.getPage() - 1);
                adminEditGui.update();
            } else {
                player.sendMessage("§cこのページが先頭のページであるため、前ページを開けません。");
            }
        }), 0, 0);

        StaticPane returnPane = new StaticPane(2, 5, 1, 1);
        returnPane.addItem(new GuiItem(LBItems.returnLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("前に戻るがクリックされました");
            getAdminHomeGui().show(player);
        }), 0, 0);

        // 表示するplayerHead、現在のページ数の表示を各ページに追加
        OutlinePane[] chestsPanes = new OutlinePane[pageCount];
        StaticPane[] currentPageCountPane = new StaticPane[pageCount];
        for(int i = 0; i < pageCount; i++){
            // 各ページで表示が変わらんところは同じように表示
            paginatedPane.addPane(i, background);
            paginatedPane.addPane(i, nextPagePane);
            paginatedPane.addPane(i, prevPagePane);
            paginatedPane.addPane(i, returnPane);

            // 現在のページ数の表示
            currentPageCountPane[i] = new StaticPane(4, 5, 1, 1);
            currentPageCountPane[i].addItem(new GuiItem(LBItems.displayPageCountGlassIS(i + 1, pageCount), event -> {
            }), 0, 0);
            paginatedPane.addPane(i, currentPageCountPane[i]);

            chestsPanes[i] = new OutlinePane(1, 1, 7, 4);
            for (int j = 0; j < 28; j++) {
                int index = i * 28 + j;

                // playerHeadを表示し終えたらbreak
                if (index >= bonusNameCount) {
                    break;
                }

                chestsPanes[i].addItem(new GuiItem(chests[index], event -> {
                    Player player = (Player) event.getWhoClicked();
                    if (event.isLeftClick()) {
                        String editingBonusName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
                        currentLoginBonusName = editingBonusName;
                        updateAdminCreateGui(true, editingBonusName);
                        getAdminCreateGui().show(player);
                    } else if (event.isRightClick()) {
                        //削除処理するgui
                        updateAdminDeleteConfirmGui(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
                        getAdminDeleteConfirmGui().show(player);
                    }
                }));
            }
            paginatedPane.addPane(i, chestsPanes[i]);
        }
        adminEditGui.addPane(paginatedPane);
    }

    public void adminDeleteConfirmGui() {
        adminDeleteConfirmGui = new ChestGui(3, "本当に削除しますか？");
        adminDeleteConfirmGui.setOnGlobalClick(event -> event.setCancelled(true));

        // 削除しない
        StaticPane cancelPane = new StaticPane(3, 1, 1, 1);
        cancelPane.addItem(new GuiItem(LBItems.cancelDeleteGreenGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            updateAdminEditGui();
            getAdminEditGui().show(player);
        }), 0, 0);
        adminDeleteConfirmGui.addPane(cancelPane);

        // 削除する
        StaticPane deletePane = new StaticPane(5, 1, 1, 1, Pane.Priority.HIGH);
        deletePane.addItem(new GuiItem(LBItems.confirmDeleteRedGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
        }), 0, 0);
        adminDeleteConfirmGui.addPane(deletePane);
    }

    ///////////////// globalSetting系 /////////////////
    public void adminGlobalSettingGui(){
        adminGlobalSettingGui = new ChestGui(3, "グローバル設定");
        adminGlobalSettingGui.setOnGlobalClick(event -> event.setCancelled(true));

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
        adminGlobalSettingGui.addPane(background);

        // ログイン情報修正
        StaticPane recoverDatePane = new StaticPane(0, 1, 1, 1);
        recoverDatePane.addItem(new GuiItem(LBItems.recoverDateBrushIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("通常報酬がクリックされました");
            updateAdminRecoverDateSettingGui();
            getAdminRecoverDateSettingGui().show(player);
        }), 0, 0);
        adminGlobalSettingGui.addPane(recoverDatePane);

        //保存ボタン
        StaticPane saveItemPane = new StaticPane(4, 2, 1, 1);
        saveItemPane.addItem(new GuiItem(LBItems.returnLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("保存がクリックされました");
            getAdminHomeGui().show(player);
        }), 0, 0);
        adminGlobalSettingGui.addPane(saveItemPane);
    }

    public void adminRecoverDateSettingGui(){
        adminRecoverDateSettingGui = new AnvilGui("ログイン情報修正");
        adminRecoverDateSettingGui.setOnGlobalClick(event -> event.setCancelled(true));

        // 左端の適当なアイテム（キャンセル兼用）
        StaticPane cancelPane = new StaticPane(0, 0, 1, 1);
        ItemStack cancel = LBItems.cancelRedGlassIS();
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName("日付を入力");
        cancel.setItemMeta(cancelMeta);

        cancelPane.addItem(new GuiItem(cancel, event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage("キャンセルがクリックされました");
            getAdminGlobalSettingGui().show(player);
        }), 0, 0);
        adminRecoverDateSettingGui.getFirstItemComponent().addPane(cancelPane);

        // チュートリアル
        StaticPane tutorialPane = new StaticPane(0, 0, 1, 1, Pane.Priority.HIGH);
        tutorialPane.addItem(new GuiItem(LBItems.recoverDateTutorialBookIS(), event -> {
        }), 0, 0);
        adminRecoverDateSettingGui.getSecondItemComponent().addPane(tutorialPane);

        // 保存
        StaticPane saveItemPane = new StaticPane(0, 0, 1, 1);
        saveItemPane.addItem(new GuiItem(LBItems.saveLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            String inputText = adminRecoverDateSettingGui.getRenameText();
            if (!inputText.isEmpty() && RewardManager.setRecoverLoginMissedDate(currentLoginBonusName, inputText, player)) {
                //player.sendMessage("設定が保存されました");
                player.playSound(player.getLocation(), "minecraft:block.note_block.harp", 1.0f, 1.0f);
                getAdminGlobalSettingGui().show(player);
            } else {
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
            }
        }), 0, 0);
        adminRecoverDateSettingGui.getResultComponent().addPane(saveItemPane);
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

    public void updateAdminCreateGui(boolean isEditMode, String editingBonusName) {
        // 編集時、ログインボーナス開催中の時に無効化
        adminCreateGui.getPanes().removeIf(pane -> pane.getPriority() == Pane.Priority.HIGH);
        String currentHoldBonusName = RewardManager.getCurrentBonusName();

        StaticPane timePane = new StaticPane(4, 2, 1, 1, Pane.Priority.HIGH);
        // 編集中であり、編集対象のログインボーナスが開催期間中の時、時間系設定を無効化
        if(isEditMode && (currentHoldBonusName == null || currentHoldBonusName.equals(editingBonusName))){
            timePane.addItem(new GuiItem(LBItems.invalidTimeSettingClockIS(), event -> {
                Player player = (Player) event.getWhoClicked();
                player.sendMessage("§c現在、ログインボーナスが開催中のため、時間系設定は無効化されています");
            }), 0, 0);
        }else{
            timePane.addItem(new GuiItem(LBItems.timeSettingClockIS(), event -> {
                Player player = (Player) event.getWhoClicked();
                player.sendMessage("時間系設定がクリックされました");
                getAdminTimeSettingGui().show(player);
            }), 0, 0);
        }
        adminCreateGui.addPane(timePane);
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

    public void updateAdminSpecialRewardSettingGui() {
        adminSpecialRewardSettingGui.getPanes().removeIf(pane -> pane instanceof OutlinePane && pane.getPriority() == Pane.Priority.NORMAL);

        // 新しいrewardItemPaneを追加
        OutlinePane rewardItemPane = new OutlinePane(0, 0, 9, 5, Pane.Priority.NORMAL);
        ArrayList<ItemStack> rewardItems = RewardManager.getAllRewards(currentLoginBonusName, "special");
        for (int i = 0; i < rewardItems.size(); i++) {
            ItemStack item = rewardItems.get(i);
            // GuiItemを作成
            GuiItem guiItem = new GuiItem(item);
            rewardItemPane.addItem(guiItem);
        }
        adminSpecialRewardSettingGui.addPane(rewardItemPane);
    }

    public ChestGui getAdminBonusRewardSettingGui(){
        return adminBonusRewardSettingGui;
    }

    public void updateAdminBonusRewardSettingGui() {
        adminBonusRewardSettingGui.getPanes().removeIf(pane -> pane instanceof OutlinePane && pane.getPriority() == Pane.Priority.NORMAL);

        // 新しいrewardItemPaneを追加
        OutlinePane rewardItemPane = new OutlinePane(0, 0, 9, 5, Pane.Priority.NORMAL);
        ArrayList<ItemStack> rewardItems = RewardManager.getAllRewards(currentLoginBonusName, "bonus");
        for (int i = 0; i < rewardItems.size(); i++) {
            ItemStack item = rewardItems.get(i);
            // GuiItemを作成
            GuiItem guiItem = new GuiItem(item);
            rewardItemPane.addItem(guiItem);
        }
        adminBonusRewardSettingGui.addPane(rewardItemPane);
    }

    public ChestGui getAdminContinuousRewardSettingGui(){
        return adminContinuousRewardSettingGui;
    }

    public void updateAdminContinuousRewardSettingGui() {
        adminContinuousRewardSettingGui.getPanes().removeIf(pane -> pane instanceof OutlinePane && pane.getPriority() == Pane.Priority.NORMAL);

        // 新しいrewardItemPaneを追加
        OutlinePane rewardItemPane = new OutlinePane(0, 0, 9, 5, Pane.Priority.NORMAL);
        ArrayList<ItemStack> rewardItems = RewardManager.getAllRewards(currentLoginBonusName, "continuous");
        for (int i = 0; i < rewardItems.size(); i++) {
            ItemStack item = rewardItems.get(i);
            // GuiItemを作成
            GuiItem guiItem = new GuiItem(item);
            rewardItemPane.addItem(guiItem);
        }
        adminContinuousRewardSettingGui.addPane(rewardItemPane);
    }

    public AnvilGui getAdminBonusRewardConditionGui(){
        return adminBonusRewardConditionGui;
    }

    public void updateAdminBonusRewardConditionGui() {
        adminBonusRewardConditionGui.getFirstItemComponent().getPanes().clear();

        // 左端の適当なアイテム（キャンセル兼用）
        StaticPane cancelPane = new StaticPane(0, 0, 1, 1, Pane.Priority.HIGH);
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
    }

    public ChestGui getAdminTimeSettingGui(){
        return adminTimeSettingGui;
    }

    public AnvilGui getAdminPeriodSettingGui(){
        return adminPeriodSettingGui;
    }

    public void updateAdminPeriodSettingGui() {
        adminPeriodSettingGui.getFirstItemComponent().getPanes().clear();

        // 左端の適当なアイテム（キャンセル兼用）
        StaticPane cancelPane = new StaticPane(0, 0, 1, 1, Pane.Priority.HIGH);
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

    }

    public AnvilGui getAdminDailyResetTimeSettingGui(){
        return adminDailyResetTimeSettingGui;
    }

    public void updateAdminDailyResetTimeSettingGui() {
        adminDailyResetTimeSettingGui.getFirstItemComponent().getPanes().clear();

        // 左端の適当なアイテム（キャンセル兼用）
        StaticPane cancelPane = new StaticPane(0, 0, 1, 1, Pane.Priority.HIGH);
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
    }

    public AnvilGui getAdminNameSettingGui(){
        return adminNameSettingGui;
    }

    public void updateAdminNameSettingGui() {
        adminNameSettingGui.getFirstItemComponent().getPanes().clear();

        // 左端の適当なアイテム（キャンセル兼用）
        StaticPane cancelPane = new StaticPane(0, 0, 1, 1, Pane.Priority.HIGH);
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
    }

    public ChestGui getAdminEditGui(){
        return adminEditGui;
    }

    public ChestGui getAdminDeleteConfirmGui(){
        return adminDeleteConfirmGui;
    }

    public void updateAdminDeleteConfirmGui(String bonusName) {
        adminDeleteConfirmGui.getPanes().removeIf(pane -> pane.getPriority() == Pane.Priority.HIGH);

        // 削除する
        StaticPane yesPane = new StaticPane(5, 1, 1, 1, Pane.Priority.HIGH);
        yesPane.addItem(new GuiItem(LBItems.confirmDeleteRedGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            if(RewardManager.deleteLoginBonus(bonusName)){
                player.sendMessage("§aログインボーナス「" + bonusName + "」を削除しました");
                updateAdminEditGui();
                getAdminEditGui().show(player);
            }else{
                player.sendMessage("§cログインボーナス「" + bonusName + "」の削除に失敗しました");
                updateAdminEditGui();
                getAdminEditGui().show(player);
            }
        }), 0, 0);
        adminDeleteConfirmGui.addPane(yesPane);
    }

    public ChestGui getAdminGlobalSettingGui(){
        return adminGlobalSettingGui;
    }

    public AnvilGui getAdminRecoverDateSettingGui(){
        return adminRecoverDateSettingGui;
    }

    public void updateAdminRecoverDateSettingGui() {
        adminRecoverDateSettingGui.getFirstItemComponent().getPanes().removeIf(pane -> pane instanceof StaticPane && pane.getPriority() == Pane.Priority.HIGH);

        // チュートリアルパネルを動的に
        StaticPane tutorialPane = new StaticPane(0, 0, 1, 1);
        tutorialPane.addItem(new GuiItem(LBItems.recoverDateTutorialBookIS(), event -> {
        }), 0, 0);
        adminRecoverDateSettingGui.getSecondItemComponent().addPane(tutorialPane);
    }

    public ChestGui getAdminTestGui(){
        return adminTestGui;
    }
}