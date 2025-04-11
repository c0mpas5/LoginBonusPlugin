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
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

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
    private ChestGui adminLoadGui;

    private ChestGui adminEditGui;
    private ChestGui adminDeleteConfirmGui;

    private ChestGui adminGlobalSettingGui;
    private AnvilGui adminRecoverDateSettingGui;
    private AnvilGui adminSubAccountSettingGui;

    private ChestGui adminTestGui;

    private String currentLoginBonusName;
    private String messagePrefix;

    public LoginBonusAdminGUI() {
        messagePrefix = "§6§l[LoginBonusPlugin] §r";

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
        adminLoadGui();


        adminEditGui();
        adminDeleteConfirmGui();

        adminGlobalSettingGui();
        adminRecoverDateSettingGui();
        adminSubAccountSettingGui();

        //adminTestGui();
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
            updateAdminCreateGui(false, null);
            getAdminFirstNameSettingGui().show(player);
        }), 0, 0);
        adminHomeGui.addPane(createItemPane);

        // ログボ編集ボタン
        StaticPane editItemPane = new StaticPane(4, 1, 1, 1);
        editItemPane.addItem(new GuiItem(LBItems.editAnvilIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            updateAdminEditGui();
            getAdminEditGui().show(player);
        }), 0, 0);
        adminHomeGui.addPane(editItemPane);

        // グローバル設定
        StaticPane globalSettingPane = new StaticPane(6, 1, 1, 1);
        globalSettingPane.addItem(new GuiItem(LBItems.globalSettingEnderEyeIS(), event -> {
            Player player = (Player) event.getWhoClicked();
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
            if (inputText.isEmpty()) {
                player.sendMessage(messagePrefix + "§c入力が空白です。1文字以上入力してください。");
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
            } else if (RewardManager.setNewLoginBonusName(inputText, player)) {
                player.sendMessage(messagePrefix + "§a新しいログボが作成されました");
                currentLoginBonusName = inputText;
                player.playSound(player.getLocation(), "minecraft:block.note_block.harp", 1.0f, 1.0f);
                getAdminCreateGui().show(player);
            } else {
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
                "111101111"
        );
        background.applyMask(mask);
        background.addItem(new GuiItem(LBItems.backgroundBlackGlassIS()));
        background.setRepeat(true);
        adminCreateGui.addPane(background);

        // 保存ボタン
        StaticPane saveItemPane = new StaticPane(4, 5, 1, 1);
        saveItemPane.addItem(new GuiItem(LBItems.saveLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.closeInventory();
        }), 0, 0);
        adminCreateGui.addPane(saveItemPane);

        // 報酬設定
        StaticPane rewardPane = new StaticPane(2, 2, 1, 1);
        rewardPane.addItem(new GuiItem(LBItems.rewardSettingChestIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            getAdminRewardSettingGui().show(player);
        }), 0, 0);
        adminCreateGui.addPane(rewardPane);

        // 時間系設定
        StaticPane timePane = new StaticPane(4, 2, 1, 1, Pane.Priority.HIGH);
        timePane.addItem(new GuiItem(LBItems.timeSettingClockIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            getAdminTimeSettingGui().show(player);
        }), 0, 0);
        adminCreateGui.addPane(timePane);

        // バナー設定
        StaticPane bannerPane = new StaticPane(6, 2, 1, 1);
        bannerPane.addItem(new GuiItem(LBItems.bannerSettingPaintingIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage(messagePrefix + "§c当該機能は未実装です");
            player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
        }), 0, 0);
        adminCreateGui.addPane(bannerPane);

        // 内部名設定
        StaticPane namePane = new StaticPane(3, 3, 1, 1);
        namePane.addItem(new GuiItem(LBItems.nameSettingNametagIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            updateAdminNameSettingGui();
            getAdminNameSettingGui().show(player);
        }), 0, 0);
        adminCreateGui.addPane(namePane);

        // その他の設定
        StaticPane otherPane = new StaticPane(5, 3, 1, 1);
        otherPane.addItem(new GuiItem(LBItems.otherSettingBookIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            updateAndShowAdminOtherSettingGui(player);
        }), 0, 0);
        adminCreateGui.addPane(otherPane);

        // 読込
        StaticPane loadPane = new StaticPane(7, 4, 1, 1);
        loadPane.addItem(new GuiItem(LBItems.loadBookShelfIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            updateAdminLoadGui();
            getAdminLoadGui().show(player);
            //player.sendMessage(messagePrefix + "§c当該機能は未実装です");
            //player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
        }), 0, 0);
        adminCreateGui.addPane(loadPane);
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
                player.sendMessage(messagePrefix + "§c現在、当該ログインボーナスが開催中のため、時間系設定は無効化されています");
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
            }), 0, 0);
        }else{
            timePane.addItem(new GuiItem(LBItems.timeSettingClockIS(), event -> {
                Player player = (Player) event.getWhoClicked();
                getAdminTimeSettingGui().show(player);
            }), 0, 0);
        }
        adminCreateGui.addPane(timePane);
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
            updateAdminRewardSettingGui("normal");
            getAdminNormalRewardSettingGui().show(player);
        }), 0, 0);
        adminRewardSettingGui.addPane(normalRewardPane);

        // 特別枠設定
        StaticPane specialRewardPane = new StaticPane(1, 1, 1, 1);
        specialRewardPane.addItem(new GuiItem(LBItems.specialRewardPoolPlayerHeadIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            updateAdminRewardSettingGui("special");
            getAdminSpecialRewardSettingGui().show(player);
        }), 0, 0);
        adminRewardSettingGui.addPane(specialRewardPane);

        // ボーナス枠設定
        StaticPane bonusRewardPane = new StaticPane(2, 1, 1, 1);
        bonusRewardPane.addItem(new GuiItem(LBItems.bonusRewardPoolPlayerHeadIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            updateAdminRewardSettingGui("bonus");
            getAdminBonusRewardSettingGui().show(player);
        }), 0, 0);
        adminRewardSettingGui.addPane(bonusRewardPane);

        // 連続ログインボーナス設定
        StaticPane continuousRewardPane = new StaticPane(3, 1, 1, 1);
        continuousRewardPane.addItem(new GuiItem(LBItems.continuousRewardPoolPlayerHeadIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            updateAdminRewardSettingGui("continuous");
            getAdminContinuousRewardSettingGui().show(player);
        }), 0, 0);
        adminRewardSettingGui.addPane(continuousRewardPane);

        // ボーナス枠条件設定
        StaticPane bonusRewardConditionPane = new StaticPane(4, 1, 1, 1);
        bonusRewardConditionPane.addItem(new GuiItem(LBItems.bonusRewardConditionCommandBlockIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            updateAdminBonusRewardConditionGui();
            getAdminBonusRewardConditionGui().show(player);
        }), 0, 0);
        adminRewardSettingGui.addPane(bonusRewardConditionPane);

        // チュートリアル
        StaticPane tutorialPane = new StaticPane(8, 1, 1, 1);
        tutorialPane.addItem(new GuiItem(LBItems.rewardSettingTutorialBookIS(), event -> {
        }), 0, 0);
        adminRewardSettingGui.addPane(tutorialPane);

        //保存ボタン
        StaticPane saveItemPane = new StaticPane(4, 2, 1, 1);
        saveItemPane.addItem(new GuiItem(LBItems.returnLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            getAdminCreateGui().show(player);
        }), 0, 0);
        adminRewardSettingGui.addPane(saveItemPane);
    }



    public void adminNormalRewardSettingGui(){
        adminNormalRewardSettingGui = new ChestGui(6, "通常枠報酬設定");

        // 外周背景
        backgroundPaneForRewardSettingGui("normal");

        // 保存ボタン
        saveItemPaneForRewardSettingGui("normal");

        //キャンセルボタン
        cancelItemPaneForRewardSettingGui("normal");

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
        backgroundPaneForRewardSettingGui("special");

        // 保存ボタン
        saveItemPaneForRewardSettingGui("special");

        //キャンセルボタン
        cancelItemPaneForRewardSettingGui("special");

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
        backgroundPaneForRewardSettingGui("bonus");

        // 保存ボタン
        saveItemPaneForRewardSettingGui("bonus");

        //キャンセルボタン
        cancelItemPaneForRewardSettingGui("bonus");

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
        backgroundPaneForRewardSettingGui("continuous");

        // 保存ボタン
        saveItemPaneForRewardSettingGui("continuous");

        //キャンセルボタン
        cancelItemPaneForRewardSettingGui("continuous");

        // チュートリアル
        StaticPane tutorialPane = new StaticPane(8, 5, 1, 1);
        tutorialPane.addItem(new GuiItem(LBItems.continuousRewardSettingTutorialBookIS(), event -> {
            event.setCancelled(true);
        }), 0, 0);
        adminContinuousRewardSettingGui.addPane(tutorialPane);
    }

    public void updateAdminRewardSettingGui(String poolType){
        // 新しいrewardItemPaneを追加
        OutlinePane rewardItemPane = new OutlinePane(0, 0, 9, 5, Pane.Priority.NORMAL);
        ArrayList<ItemStack> rewardItems = null;

        if(poolType.equals("normal")){
            rewardItems = RewardManager.getAllRewards(currentLoginBonusName, "normal");
        } else if(poolType.equals("special")){
            rewardItems = RewardManager.getAllRewards(currentLoginBonusName, "special");
        } else if (poolType.equals("bonus")){
            rewardItems = RewardManager.getAllRewards(currentLoginBonusName, "bonus");
        } else if (poolType.equals("continuous")){
            rewardItems = RewardManager.getAllRewards(currentLoginBonusName, "continuous");
        }

        for (ItemStack item : rewardItems) {
            // GuiItemを作成
            GuiItem guiItem = new GuiItem(item);
            rewardItemPane.addItem(guiItem);
        }

        if(poolType.equals("normal")){
            adminNormalRewardSettingGui.getPanes().removeIf(pane -> pane instanceof OutlinePane && pane.getPriority() == Pane.Priority.NORMAL);
            adminNormalRewardSettingGui.addPane(rewardItemPane);
        } else if(poolType.equals("special")){
            adminSpecialRewardSettingGui.getPanes().removeIf(pane -> pane instanceof OutlinePane && pane.getPriority() == Pane.Priority.NORMAL);
            adminSpecialRewardSettingGui.addPane(rewardItemPane);
        } else if (poolType.equals("bonus")){
            adminBonusRewardSettingGui.getPanes().removeIf(pane -> pane instanceof OutlinePane && pane.getPriority() == Pane.Priority.NORMAL);
            adminBonusRewardSettingGui.addPane(rewardItemPane);
        } else if (poolType.equals("continuous")){
            adminContinuousRewardSettingGui.getPanes().removeIf(pane -> pane instanceof OutlinePane && pane.getPriority() == Pane.Priority.NORMAL);
            adminContinuousRewardSettingGui.addPane(rewardItemPane);
        }
    }

    public void backgroundPaneForRewardSettingGui(String poolType){
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
        if(poolType.equals("normal")){
            adminNormalRewardSettingGui.addPane(background);
        } else if(poolType.equals("special")){
            adminSpecialRewardSettingGui.addPane(background);
        } else if (poolType.equals("bonus")){
            adminBonusRewardSettingGui.addPane(background);
        } else if (poolType.equals("continuous")){
            adminContinuousRewardSettingGui.addPane(background);
        }
    }

    public void saveItemPaneForRewardSettingGui(String poolType){
        // 保存ボタン
        StaticPane saveItemPane = new StaticPane(4, 5, 1, 1);
        saveItemPane.addItem(new GuiItem(LBItems.saveLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            RewardManager.deleteRewardsInfo(currentLoginBonusName, poolType);

            ArrayList<ItemStack> items = new ArrayList<>();
            for (int i = 0; i <= 44; i++){
                if(adminNormalRewardSettingGui.getInventory().getItem(i) == null){
                    break;
                }
                ItemStack item = removeCustomNBT(adminNormalRewardSettingGui.getInventory().getItem(i), "loginbonusplugin", "if-uuid");
                items.add(item);
            }
            RewardManager.saveRewards(currentLoginBonusName, poolType, items);
            player.sendMessage(messagePrefix + "§a報酬が保存されました");
            player.playSound(player.getLocation(), "minecraft:block.note_block.harp", 1.0f, 1.0f);

            getAdminRewardSettingGui().show(player);
        }), 0, 0);
        if(poolType.equals("normal")){
            adminNormalRewardSettingGui.addPane(saveItemPane);
        } else if(poolType.equals("special")){
            adminSpecialRewardSettingGui.addPane(saveItemPane);
        } else if (poolType.equals("bonus")){
            adminBonusRewardSettingGui.addPane(saveItemPane);
        } else if (poolType.equals("continuous")){
            adminContinuousRewardSettingGui.addPane(saveItemPane);
        }
    }

    public void cancelItemPaneForRewardSettingGui(String poolType){
        //キャンセルボタン
        StaticPane cancelItemPane = new StaticPane(0, 5, 1, 1);
        cancelItemPane.addItem(new GuiItem(LBItems.cancelRedGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            getAdminRewardSettingGui().show(player);
        }), 0, 0);
        if(poolType.equals("normal")){
            adminNormalRewardSettingGui.addPane(cancelItemPane);
        } else if(poolType.equals("special")){
            adminSpecialRewardSettingGui.addPane(cancelItemPane);
        } else if (poolType.equals("bonus")){
            adminBonusRewardSettingGui.addPane(cancelItemPane);
        } else if (poolType.equals("continuous")){
            adminContinuousRewardSettingGui.addPane(cancelItemPane);
        }
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
                player.sendMessage(messagePrefix + "§a設定が保存されました");
                player.playSound(player.getLocation(), "minecraft:block.note_block.harp", 1.0f, 1.0f);
                getAdminRewardSettingGui().show(player);
            } else {
                player.sendMessage(messagePrefix + "§c入力が無効です。0~100の整数を入力してください。");
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
            }
        }), 0, 0);
        adminBonusRewardConditionGui.getResultComponent().addPane(saveItemPane);
    }

    public void updateAdminBonusRewardConditionGui() {
        adminBonusRewardConditionGui.getFirstItemComponent().getPanes().clear();

        // 左端の適当なアイテム（キャンセル兼用）
        StaticPane cancelPane = new StaticPane(0, 0, 1, 1, Pane.Priority.HIGH);
        ItemStack cancel = LBItems.cancelRedGlassIS();
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName(RewardManager.getBonusRewardCondition(currentLoginBonusName) + " / 0~100（整数）");
        cancel.setItemMeta(cancelMeta);

        cancelPane.addItem(new GuiItem(cancel, event -> {
            Player player = (Player) event.getWhoClicked();
            getAdminRewardSettingGui().show(player);
        }), 0, 0);
        adminBonusRewardConditionGui.getFirstItemComponent().addPane(cancelPane);
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
            updateAdminPeriodSettingGui();
            getAdminPeriodSettingGui().show(player);
        }), 0, 0);
        adminTimeSettingGui.addPane(createItemPane);

        // 日付変更時刻設定
        StaticPane editItemPane = new StaticPane(5, 1, 1, 1);
        editItemPane.addItem(new GuiItem(LBItems.dailyResetTimeSettingCompassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            updateAdminDailyResetTimeSettingGui();
            getAdminDailyResetTimeSettingGui().show(player);
        }), 0, 0);
        adminTimeSettingGui.addPane(editItemPane);

        //キャンセルボタン
        StaticPane returnItemPane = new StaticPane(0, 2, 1, 1);
        returnItemPane.addItem(new GuiItem(LBItems.returnLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
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
                player.playSound(player.getLocation(), "minecraft:block.note_block.harp", 1.0f, 1.0f);
                getAdminCreateGui().show(player);
            } else {
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
            }
        }), 0, 0);
        adminPeriodSettingGui.getResultComponent().addPane(saveItemPane);
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
            getAdminTimeSettingGui().show(player);
        }), 0, 0);
        adminPeriodSettingGui.getFirstItemComponent().addPane(cancelPane);

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
            if ((inputText.matches("\\D+")) || inputText.isEmpty()) {
                player.sendMessage(messagePrefix + "§c入力が空白か無効です。数字のみを入力してください。");
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
            } else if (RewardManager.setDailyResetTime(currentLoginBonusName, Integer.parseInt(inputText), player)) {
                player.playSound(player.getLocation(), "minecraft:block.note_block.harp", 1.0f, 1.0f);
                getAdminCreateGui().show(player);
            } else {
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
            }
        }), 0, 0);
        adminDailyResetTimeSettingGui.getResultComponent().addPane(saveItemPane);
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
            getAdminTimeSettingGui().show(player);
        }), 0, 0);
        adminDailyResetTimeSettingGui.getFirstItemComponent().addPane(cancelPane);
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
            if (inputText.isEmpty()) {
                player.sendMessage(messagePrefix + "§c入力が空白です。1文字以上入力してください。");
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
            } else if(RewardManager.updateLoginBonusName(currentLoginBonusName, inputText, player)){
                currentLoginBonusName = inputText;
                player.sendMessage(messagePrefix + "§a設定が保存されました");
                player.playSound(player.getLocation(), "minecraft:block.note_block.harp", 1.0f, 1.0f);
                getAdminCreateGui().show(player);
            } else {
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
            }
        }), 0, 0);
        adminNameSettingGui.getResultComponent().addPane(saveItemPane);
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
            getAdminCreateGui().show(player);
        }), 0, 0);
        adminNameSettingGui.getFirstItemComponent().addPane(cancelPane);
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
                    player.sendMessage(messagePrefix + "§aログボが無効化されました");
                    player.playSound(player.getLocation(), "minecraft:block.note_block.harp", 1.0f, 1.0f);
                    updateAndShowAdminOtherSettingGui(player);
                } else {
                    player.sendMessage(messagePrefix + "§c以下の設定を先に終える必要があります");
                    player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
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
                    player.sendMessage(messagePrefix + "§aログボが有効化されました");
                    player.playSound(player.getLocation(), "minecraft:block.note_block.harp", 1.0f, 1.0f);
                    updateAndShowAdminOtherSettingGui(player);
                } else {
                    player.sendMessage(messagePrefix + "§c以下の設定を先に終える必要があります");
                    player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
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

    // 報酬プールロード画面
    public void adminLoadGui(){
        ArrayList<String> bonusNames = RewardManager.getAllBonusNames();
        int bonusNameCount = bonusNames.size();
        adminLoadGui = new ChestGui(6, "報酬プール設定を読み込む");
        adminLoadGui.setOnGlobalClick(event -> event.setCancelled(true));

        // ログボが存在してない時
        if(bonusNames.isEmpty()){
            StaticPane errorPane = new StaticPane(3, 2, 1, 1);
            errorPane.addItem(new GuiItem(LBItems.noLoginBonusNamePaperIS(), event -> {
            }), 0, 0);
            adminLoadGui.addPane(errorPane);

            StaticPane returnPane = new StaticPane(5, 2, 1, 1);
            returnPane.addItem(new GuiItem(LBItems.returnLimeGlassIS(), event -> {
                Player player = (Player) event.getWhoClicked();
                getAdminCreateGui().show(player);
            }), 0, 0);
            adminLoadGui.addPane(returnPane);

            return;
        }

        //全ページ
        PaginatedPane paginatedPane = new PaginatedPane(0, 0, 9, 6);

        ItemStack[] chests = new ItemStack[bonusNameCount];

        // 表示するchestの配列作る
        for(int i = 0; i < bonusNameCount; i++){
            String bonusName = bonusNames.get(i);
            chests[i] = LBItems.loginBonusNameForLoadChestIS(bonusName, RewardManager.getOriginalPeriod(bonusName));
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
                adminLoadGui.update();
            } else {
                player.sendMessage(messagePrefix + "§cこのページが末尾のページであるため、次ページを開けません。");
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
            }
        }), 0, 0);

        // 前のページ
        StaticPane prevPagePane = new StaticPane(0, 5, 1, 1);
        prevPagePane.addItem(new GuiItem(LBItems.prevPageAquaPlayerHeadIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            if (paginatedPane.getPage() > 0) {
                paginatedPane.setPage(paginatedPane.getPage() - 1);
                adminLoadGui.update();
            } else {
                player.sendMessage(messagePrefix + "§cこのページが先頭のページであるため、前ページを開けません。");
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
            }
        }), 0, 0);

        StaticPane returnPane = new StaticPane(2, 5, 1, 1);
        returnPane.addItem(new GuiItem(LBItems.returnLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            getAdminCreateGui().show(player);
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
                    String refBonusName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
                    if (RewardManager.loadAllPool(refBonusName, currentLoginBonusName)){
                        player.sendMessage(messagePrefix + "§a" + refBonusName + "から" + currentLoginBonusName + "への報酬プールの読み込みが完了しました");
                        player.playSound(player.getLocation(), "minecraft:block.note_block.harp", 1.0f, 1.0f);
                        getAdminCreateGui().show(player);
                    } else {
                        player.sendMessage(messagePrefix + "§c" + refBonusName + "から" + currentLoginBonusName + "への報酬プールの読み込みに失敗しました");
                        player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
                    }
                }));
            }
            paginatedPane.addPane(i, chestsPanes[i]);
        }
        adminLoadGui.addPane(paginatedPane);
    }

    public void updateAdminLoadGui(){
        adminLoadGui.getPanes().clear();

        ArrayList<String> bonusNames = RewardManager.getAllBonusNames();
        int bonusNameCount = bonusNames.size();

        // ログボが存在してない時
        if(bonusNames.isEmpty()){
            StaticPane errorPane = new StaticPane(3, 2, 1, 1);
            errorPane.addItem(new GuiItem(LBItems.noLoginBonusNamePaperIS(), event -> {
            }), 0, 0);
            adminLoadGui.addPane(errorPane);

            StaticPane returnPane = new StaticPane(5, 2, 1, 1);
            returnPane.addItem(new GuiItem(LBItems.returnLimeGlassIS(), event -> {
                Player player = (Player) event.getWhoClicked();
                getAdminCreateGui().show(player);
            }), 0, 0);
            adminLoadGui.addPane(returnPane);

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
                adminLoadGui.update();
            } else {
                player.sendMessage(messagePrefix + "§cこのページが末尾のページであるため、次ページを開けません。");
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
            }
        }), 0, 0);

        // 前のページ
        StaticPane prevPagePane = new StaticPane(0, 5, 1, 1);
        prevPagePane.addItem(new GuiItem(LBItems.prevPageAquaPlayerHeadIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            if (paginatedPane.getPage() > 0) {
                paginatedPane.setPage(paginatedPane.getPage() - 1);
                adminLoadGui.update();
            } else {
                player.sendMessage(messagePrefix + "§cこのページが先頭のページであるため、前ページを開けません。");
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
            }
        }), 0, 0);

        StaticPane returnPane = new StaticPane(2, 5, 1, 1);
        returnPane.addItem(new GuiItem(LBItems.returnLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            getAdminCreateGui().show(player);
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
                    String refBonusName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
                    if (RewardManager.loadAllPool(refBonusName, currentLoginBonusName)){
                        player.sendMessage(messagePrefix + "§a" + refBonusName + "から" + currentLoginBonusName + "への報酬プールの読み込みが完了しました");
                        player.playSound(player.getLocation(), "minecraft:block.note_block.harp", 1.0f, 1.0f);
                        getAdminCreateGui().show(player);
                    } else {
                        player.sendMessage(messagePrefix + "§c" + refBonusName + "から" + currentLoginBonusName + "への報酬プールの読み込みに失敗しました");
                        player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
                    }
                }));
            }
            paginatedPane.addPane(i, chestsPanes[i]);
        }
        adminLoadGui.addPane(paginatedPane);
    }

    ///////////////// ログボ編集系 /////////////////
    public void adminEditGui(){
        ArrayList<String> bonusNames = RewardManager.getAllBonusNames();
        int bonusNameCount = bonusNames.size();
        adminEditGui = new ChestGui(6, "ログボ編集");
        adminEditGui.setOnGlobalClick(event -> event.setCancelled(true));

        // ログボが存在してない時
        if(bonusNames.isEmpty()){
            StaticPane errorPane = new StaticPane(3, 2, 1, 1);
            errorPane.addItem(new GuiItem(LBItems.noLoginBonusNamePaperIS(), event -> {
            }), 0, 0);
            adminEditGui.addPane(errorPane);

            StaticPane returnPane = new StaticPane(5, 2, 1, 1);
            returnPane.addItem(new GuiItem(LBItems.returnLimeGlassIS(), event -> {
                Player player = (Player) event.getWhoClicked();
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
                player.sendMessage(messagePrefix + "§cこのページが末尾のページであるため、次ページを開けません。");
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
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
                player.sendMessage(messagePrefix + "§cこのページが先頭のページであるため、前ページを開けません。");
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
            }
        }), 0, 0);

        StaticPane returnPane = new StaticPane(2, 5, 1, 1);
        returnPane.addItem(new GuiItem(LBItems.returnLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
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
                player.sendMessage(messagePrefix + "§cこのページが末尾のページであるため、次ページを開けません。");
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
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
                player.sendMessage(messagePrefix + "§cこのページが先頭のページであるため、前ページを開けません。");
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
            }
        }), 0, 0);

        StaticPane returnPane = new StaticPane(2, 5, 1, 1);
        returnPane.addItem(new GuiItem(LBItems.returnLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
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
            // こいつには機能持たせてない
        }), 0, 0);
        adminDeleteConfirmGui.addPane(deletePane);
    }

    public void updateAdminDeleteConfirmGui(String bonusName) {
        adminDeleteConfirmGui.getPanes().removeIf(pane -> pane.getPriority() == Pane.Priority.HIGH);

        // 削除する
        StaticPane yesPane = new StaticPane(5, 1, 1, 1, Pane.Priority.HIGH);
        yesPane.addItem(new GuiItem(LBItems.confirmDeleteRedGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            if(RewardManager.deleteLoginBonus(bonusName)){
                player.sendMessage(messagePrefix + "§aログインボーナス「" + bonusName + "」を削除しました");
                player.playSound(player.getLocation(), "minecraft:block.grindstone.use", 1.0f, 1.0f);
                updateAdminEditGui();
                getAdminEditGui().show(player);
            }else{
                player.sendMessage(messagePrefix + "§cログインボーナス「" + bonusName + "」の削除に失敗しました");
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
                updateAdminEditGui();
                getAdminEditGui().show(player);
            }
        }), 0, 0);
        adminDeleteConfirmGui.addPane(yesPane);
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
            updateAdminRecoverDateSettingGui();
            getAdminRecoverDateSettingGui().show(player);
        }), 0, 0);
        adminGlobalSettingGui.addPane(recoverDatePane);

        // サブ垢設定
        StaticPane subAccountPane = new StaticPane(1, 1, 1, 1);
        subAccountPane.addItem(new GuiItem(LBItems.subAccountSettingPlayerHeadIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            updateAdminSubAccountSettingGui();
            getAdminSubAccountSettingGui().show(player);
        }), 0, 0);
        adminGlobalSettingGui.addPane(subAccountPane);

        //保存ボタン
        StaticPane saveItemPane = new StaticPane(4, 2, 1, 1);
        saveItemPane.addItem(new GuiItem(LBItems.returnLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
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
            if (inputText.isEmpty()) {
                player.sendMessage(messagePrefix + "§c入力が空白です。1文字以上入力してください。");
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
            } else if (RewardManager.setRecoverLoginMissedDate(currentLoginBonusName, inputText, player)){
                player.playSound(player.getLocation(), "minecraft:block.note_block.harp", 1.0f, 1.0f);
                getAdminGlobalSettingGui().show(player);
            } else {
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
            }
        }), 0, 0);
        adminRecoverDateSettingGui.getResultComponent().addPane(saveItemPane);
    }

    public void updateAdminRecoverDateSettingGui() {
        adminRecoverDateSettingGui.getSecondItemComponent().getPanes().removeIf(pane -> pane instanceof StaticPane && pane.getPriority() == Pane.Priority.HIGH);

        // チュートリアルパネルを動的に
        StaticPane tutorialPane = new StaticPane(0, 0, 1, 1, Pane.Priority.HIGH);
        tutorialPane.addItem(new GuiItem(LBItems.recoverDateTutorialBookIS(), event -> {
        }), 0, 0);
        adminRecoverDateSettingGui.getSecondItemComponent().addPane(tutorialPane);
    }

    public void adminSubAccountSettingGui(){
        adminSubAccountSettingGui = new AnvilGui("サブアカウント設定");
        adminSubAccountSettingGui.setOnGlobalClick(event -> event.setCancelled(true));

        // 左端の適当なアイテム（キャンセル兼用）
        StaticPane cancelPane = new StaticPane(0, 0, 1, 1, Pane.Priority.HIGH);
        ItemStack cancel = LBItems.cancelRedGlassIS();
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName(RewardManager.getAccountRewardLimit() + " / 0以上の整数");
        cancel.setItemMeta(cancelMeta);

        cancelPane.addItem(new GuiItem(cancel, event -> {
            Player player = (Player) event.getWhoClicked();
            getAdminTimeSettingGui().show(player);
        }), 0, 0);
        adminSubAccountSettingGui.getFirstItemComponent().addPane(cancelPane);

        // チュートリアル
        StaticPane tutorialPane = new StaticPane(0, 0, 1, 1);
        tutorialPane.addItem(new GuiItem(LBItems.subAccountSettingTutorialBookIS(), event -> {
        }), 0, 0);
        adminSubAccountSettingGui.getSecondItemComponent().addPane(tutorialPane);

        // 保存
        StaticPane saveItemPane = new StaticPane(0, 0, 1, 1);
        saveItemPane.addItem(new GuiItem(LBItems.saveLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            String inputText = adminSubAccountSettingGui.getRenameText();
            if ((inputText.matches("\\D+")) || inputText.isEmpty()) {
                player.sendMessage(messagePrefix + "§c入力が空白か無効です。数字のみを入力してください。");
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
            } else if (RewardManager.setAccountRewardLimit(Integer.parseInt(inputText), player)) {
                player.playSound(player.getLocation(), "minecraft:block.note_block.harp", 1.0f, 1.0f);
                getAdminGlobalSettingGui().show(player);
            } else {
                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
            }
        }), 0, 0);
        adminSubAccountSettingGui.getResultComponent().addPane(saveItemPane);
    }

    public void updateAdminSubAccountSettingGui(){
        adminSubAccountSettingGui.getFirstItemComponent().getPanes().clear();

        // 左端の適当なアイテム（キャンセル兼用）
        StaticPane cancelPane = new StaticPane(0, 0, 1, 1, Pane.Priority.HIGH);
        ItemStack cancel = LBItems.cancelRedGlassIS();
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName(RewardManager.getAccountRewardLimit() + " / 0以上の整数");
        cancel.setItemMeta(cancelMeta);

        cancelPane.addItem(new GuiItem(cancel, event -> {
            Player player = (Player) event.getWhoClicked();
            getAdminTimeSettingGui().show(player);
        }), 0, 0);
        adminSubAccountSettingGui.getFirstItemComponent().addPane(cancelPane);
    }

//    public void adminTestGui(){
//        adminTestGui = new ChestGui(3, "test");
//
//        StaticPane diamondPane = new StaticPane(4, 1, 1, 1);
//        diamondPane.addItem(new GuiItem(new ItemStack(Material.DIAMOND)), 0, 0);
//        adminTestGui.addPane(diamondPane);
//    }

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

    public ChestGui getAdminLoadGui(){
        return adminLoadGui;
    }

    public ChestGui getAdminEditGui(){
        return adminEditGui;
    }

    public ChestGui getAdminDeleteConfirmGui(){
        return adminDeleteConfirmGui;
    }

    public ChestGui getAdminGlobalSettingGui(){
        return adminGlobalSettingGui;
    }

    public AnvilGui getAdminRecoverDateSettingGui(){
        return adminRecoverDateSettingGui;
    }

    public AnvilGui getAdminSubAccountSettingGui(){
        return adminSubAccountSettingGui;
    }

    public ChestGui getAdminTestGui(){
        return adminTestGui;
    }

    // アイテムから特定のタグを削除するメソッド
    private ItemStack removeCustomNBT(ItemStack item, String keyNamespace, String keyName) {
        if (item == null || !item.hasItemMeta()) {
            return item; // アイテムがnullまたはメタデータがない場合はそのまま返す
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item; // メタデータがnullの場合はそのまま返す
        }

        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(keyNamespace, keyName);

        // 該当するキーを削除
        if (container.has(key, PersistentDataType.BYTE_ARRAY)) {
            container.remove(key);
        }

        item.setItemMeta(meta); // 更新したメタデータをアイテムに設定
        return item; // 処理済みのItemStackを返す
    }
}