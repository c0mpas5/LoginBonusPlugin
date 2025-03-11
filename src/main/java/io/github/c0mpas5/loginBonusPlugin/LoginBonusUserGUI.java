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
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class LoginBonusUserGUI implements Listener {
    private ChestGui userAccumulatedLoginBonusClaimGui;

    private ChestGui adminTestGui;
    private LoginBonusData loginBonusData;
    private UUID playerUUID;

    public LoginBonusUserGUI(LoginBonusData loginBonusData, UUID playerUUID){
        this.loginBonusData = loginBonusData;
        this.playerUUID = playerUUID;
        userAccumulatedLoginBonusClaimGui();

        adminTestGui();
    }



    public void userAccumulatedLoginBonusClaimGui(){
        String currentLoginBonusName = RewardManager.getCurrentBonusName();
        userAccumulatedLoginBonusClaimGui = new ChestGui(6, "累積ログボ受取");
        userAccumulatedLoginBonusClaimGui.setOnGlobalClick(event -> event.setCancelled(true));

        //全ページ
        PaginatedPane paginatedPane = new PaginatedPane(0, 0, 9, 6);
        LocalDate startDate = RewardManager.getPeriodStartDate(currentLoginBonusName);
        LocalDate endDate = RewardManager.getPeriodEndDate(currentLoginBonusName);
        int daysBetween;
        if(startDate == null || endDate == null){
            daysBetween = 10; // temp:仮置き
        }else{
            daysBetween = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
        }
        ItemStack[] chests = new ItemStack[daysBetween];

        int canClaimRewardDay = loginBonusData.getLoginCount(playerUUID, currentLoginBonusName) - 1; // ログインボーナスを受け取れる日（インデックス番号）

        // 表示するplayerHeadの配列作る
        for(int i = 0; i < daysBetween; i++){
            if(i < canClaimRewardDay || i == canClaimRewardDay && hasClaimedToday(playerUUID)){
                chests[i] = LBItems.alreadyClaimedRewardForUserGlassIS(i + 1);
            }else if(i % 10 == 9 && i == canClaimRewardDay) {
                chests[i] = LBItems.specialRewardForUserPlayerHeadIS(i + 1, true);
            }else if(i % 10 == 9){
                chests[i] = LBItems.specialRewardForUserPlayerHeadIS(i + 1, false);
            }else if(i == canClaimRewardDay){
                chests[i] = LBItems.normalRewardForUserPlayerHeadIS(i + 1, true);
            }else{
                chests[i] = LBItems.normalRewardForUserPlayerHeadIS(i + 1, false);
            }
        }

        int pageCount = (daysBetween / 28) + 1; //28...1ページに表示するアイテム数

        // 外周背景
        OutlinePane background = new OutlinePane(0, 0, 9, 6, Pane.Priority.LOWEST);
        Mask mask = new Mask(
                "111111111",
                "100000001",
                "100000001",
                "100000001",
                "100000001",
                "011101110"
        );
        background.applyMask(mask);
        background.addItem(new GuiItem(LBItems.backgroundBlackGlassIS()));
        background.setRepeat(true);
        userAccumulatedLoginBonusClaimGui.addPane(background);

        //
        StaticPane nextPagePane = new StaticPane(8, 5, 1, 1);
        nextPagePane.addItem(new GuiItem(LBItems.nextPageAquaPlayerHeadIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            if (paginatedPane.getPage() < pageCount - 1) {
                paginatedPane.setPage(paginatedPane.getPage() + 1);
                userAccumulatedLoginBonusClaimGui.update();
            } else {
                player.sendMessage("§cこのページが末尾のページであるため、次ページを開けません。");
            }
        }), 0, 0);

        StaticPane prevPagePane = new StaticPane(0, 5, 1, 1);
        prevPagePane.addItem(new GuiItem(LBItems.prevPageAquaPlayerHeadIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            if (paginatedPane.getPage() > 0) {
                paginatedPane.setPage(paginatedPane.getPage() - 1);
                userAccumulatedLoginBonusClaimGui.update();
            } else {
                player.sendMessage("§cこのページが先頭のページであるため、前ページを開けません。");
            }
        }), 0, 0);

        // 表示するplayerHeadを各ページに追加
        OutlinePane[] rewardPanes = new OutlinePane[pageCount];
        for(int i = 0; i < pageCount; i++){
            paginatedPane.addPane(i, background);
            paginatedPane.addPane(i, nextPagePane);
            paginatedPane.addPane(i, prevPagePane);
            rewardPanes[i] = new OutlinePane(1, 1, 7, 4);
            for(int j = 0; j < 28; j++){
                int index = i * 28 + j;
                if(index >= daysBetween){
                    break;
                }
                if (index == canClaimRewardDay) {
                    rewardPanes[i].addItem(new GuiItem(chests[index], event -> {
                        Player player = (Player) event.getWhoClicked();
                        if (isInventoryFull(player)) {
                            player.sendMessage("§cインベントリに空きがないため、報酬を受け取れません");
                            player.closeInventory();
                        } else if(!(canClaimRewardDay == loginBonusData.getLoginCount(playerUUID, currentLoginBonusName) - 1)) {
                            player.sendMessage("§c日付が変わりました。もう一度ログインボーナス画面を開きなおしてください");
                            player.closeInventory();
                        }else{
                            String poolType;
                            if(index % 10 == 9){
                                poolType = "special";
                            }else{
                                poolType = "normal";
                            }
                            player.closeInventory();
                            //TODO: 連打して受け取れたりしないかの確認
                            ItemStack item = RewardManager.getRandomRewards(currentLoginBonusName, poolType);
                            player.getInventory().addItem(item);
                            loginBonusData.setClaimedItemStack(playerUUID, item.toString(), LocalDateTime.now());
                            player.sendMessage(item.getType().name() + "×" + item.getAmount() + " §aを受け取りました！");
                        }
                    }));
                } else {
                    rewardPanes[i].addItem(new GuiItem(chests[index], event -> {
                        Player player = (Player) event.getWhoClicked();
                        player.sendMessage("§aこの報酬は受取済か、本日が受取可能な日ではないため受け取れません");
                        event.setCancelled(true);
                    }));
                }
            }
            paginatedPane.addPane(i, rewardPanes[i]);
        }
        userAccumulatedLoginBonusClaimGui.addPane(paginatedPane);
    }



    public void adminTestGui(){
        adminTestGui = new ChestGui(6, "テスト");
        adminTestGui.setOnGlobalClick(event -> event.setCancelled(true));

        Slider slider = new Slider(0, 0, 9, 6);
        slider.setValue(0.5f);
        adminTestGui.addPane(slider);
    }

    public ChestGui getUserAccumulatedLoginBonusClaimGui(){
        return userAccumulatedLoginBonusClaimGui;
    }

    public void updateUserAccumulatedLoginBonusClaimGui(){
        String currentLoginBonusName = RewardManager.getCurrentBonusName();
        // 一旦全部再生成。絶対重いので改善すべきではある
        userAccumulatedLoginBonusClaimGui.getPanes().clear();

        //全ページ
        PaginatedPane paginatedPane = new PaginatedPane(0, 0, 9, 6);
        LocalDate startDate = RewardManager.getPeriodStartDate(currentLoginBonusName);
        LocalDate endDate = RewardManager.getPeriodEndDate(currentLoginBonusName);
        int daysBetween;
        if(startDate == null || endDate == null){
            daysBetween = 10; // temp:仮置き
        }else{
            daysBetween = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
        }
        ItemStack[] chests = new ItemStack[daysBetween];

        int canClaimRewardDay = loginBonusData.getLoginCount(playerUUID, currentLoginBonusName) - 1; // ログインボーナスを受け取れる日（インデックス番号）

        // 表示するplayerHeadの配列作る
        for(int i = 0; i < daysBetween; i++){
            if(i < canClaimRewardDay || i == canClaimRewardDay && hasClaimedToday(playerUUID)){
                chests[i] = LBItems.alreadyClaimedRewardForUserGlassIS(i + 1);
            }else if(i % 10 == 9 && i == canClaimRewardDay) {
                chests[i] = LBItems.specialRewardForUserPlayerHeadIS(i + 1, true);
            }else if(i % 10 == 9){
                chests[i] = LBItems.specialRewardForUserPlayerHeadIS(i + 1, false);
            }else if(i == canClaimRewardDay){
                chests[i] = LBItems.normalRewardForUserPlayerHeadIS(i + 1, true);
            }else{
                chests[i] = LBItems.normalRewardForUserPlayerHeadIS(i + 1, false);
            }
        }

        int pageCount = (daysBetween / 28) + 1; //28...1ページに表示するアイテム数

        // 外周背景
        OutlinePane background = new OutlinePane(0, 0, 9, 6, Pane.Priority.LOWEST);
        Mask mask = new Mask(
                "111111111",
                "100000001",
                "100000001",
                "100000001",
                "100000001",
                "011101110"
        );
        background.applyMask(mask);
        background.addItem(new GuiItem(LBItems.backgroundBlackGlassIS()));
        background.setRepeat(true);
        userAccumulatedLoginBonusClaimGui.addPane(background);

        //
        StaticPane nextPagePane = new StaticPane(8, 5, 1, 1);
        nextPagePane.addItem(new GuiItem(LBItems.nextPageAquaPlayerHeadIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            if (paginatedPane.getPage() < pageCount - 1) {
                paginatedPane.setPage(paginatedPane.getPage() + 1);
                userAccumulatedLoginBonusClaimGui.update();
            } else {
                player.sendMessage("§cこのページが末尾のページであるため、次ページを開けません。");
            }
        }), 0, 0);

        StaticPane prevPagePane = new StaticPane(0, 5, 1, 1);
        prevPagePane.addItem(new GuiItem(LBItems.prevPageAquaPlayerHeadIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            if (paginatedPane.getPage() > 0) {
                paginatedPane.setPage(paginatedPane.getPage() - 1);
                userAccumulatedLoginBonusClaimGui.update();
            } else {
                player.sendMessage("§cこのページが先頭のページであるため、前ページを開けません。");
            }
        }), 0, 0);

        // 表示するplayerHeadを各ページに追加
        OutlinePane[] rewardPanes = new OutlinePane[pageCount];
        for(int i = 0; i < pageCount; i++){
            paginatedPane.addPane(i, background);
            paginatedPane.addPane(i, nextPagePane);
            paginatedPane.addPane(i, prevPagePane);
            rewardPanes[i] = new OutlinePane(1, 1, 7, 4);
            for(int j = 0; j < 28; j++){
                int index = i * 28 + j;
                if(index >= daysBetween){
                    break;
                }
                if (index == canClaimRewardDay && !hasClaimedToday(playerUUID)) {
                    rewardPanes[i].addItem(new GuiItem(chests[index], event -> {
                        Player player = (Player) event.getWhoClicked();
                        if (isInventoryFull(player)) {
                            player.sendMessage("§cインベントリに空きがないため、報酬を受け取れません");
                            player.closeInventory();
                        } else if(!(canClaimRewardDay == loginBonusData.getLoginCount(playerUUID, currentLoginBonusName) - 1)) {
                            player.sendMessage("§c日付が変わりました。もう一度ログインボーナス画面を開きなおしてください");
                            player.closeInventory();
                        }else{
                            String poolType;
                            if(index % 10 == 9){
                                poolType = "special";
                            }else{
                                poolType = "normal";
                            }
                            player.closeInventory();
                            //TODO: 連打して受け取れたりしないかの確認
                            ItemStack item = RewardManager.getRandomRewards(currentLoginBonusName, poolType);
                            player.getInventory().addItem(item);
                            loginBonusData.setClaimedItemStack(playerUUID, item.toString(), LocalDateTime.now());
                            player.sendMessage(item.getType().name() + "×" + item.getAmount() + " §aを受け取りました！");
                        }
                    }));
                } else {
                    rewardPanes[i].addItem(new GuiItem(chests[index], event -> {
                        Player player = (Player) event.getWhoClicked();
                        player.sendMessage("§aこの報酬は受取済か、本日が受取可能な日ではないため受け取れません");
                        event.setCancelled(true);
                    }));
                }
            }
            paginatedPane.addPane(i, rewardPanes[i]);
        }
        userAccumulatedLoginBonusClaimGui.addPane(paginatedPane);
    }

    public ChestGui getAdminTestGui(){
        return adminTestGui;
    }

    private boolean isInventoryFull(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType().isAir()) {
                return false;
            }
        }
        return true;
    }

    public boolean hasClaimedToday(UUID playerUUID) {
        LocalDateTime lastClaimedDate = loginBonusData.getLastClaimedDate(playerUUID);
        if (lastClaimedDate == null) {
            return false;
        }

        String currentBonusName = RewardManager.getCurrentBonusName();
        int resetHour = RewardManager.getDailyResetTime(currentBonusName);
        LocalDateTime resetDateTime = LocalDate.now().atTime(LocalTime.of(resetHour, 0));
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(resetDateTime)) {
            resetDateTime = resetDateTime.minusDays(1);
        }

        return lastClaimedDate.isAfter(resetDateTime);
    }
}