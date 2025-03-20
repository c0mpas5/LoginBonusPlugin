package io.github.c0mpas5.loginBonusPlugin;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.component.Slider;
import com.github.stefvanschie.inventoryframework.pane.util.Mask;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
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

public class LoginBonusUserGUI implements Listener {
    private ChestGui userAccumulatedLoginBonusClaimGui;
    private ChestGui userRewardListGui;
    private ChestGui userContinuousLoginBonusClaimGui;

    private ChestGui adminTestGui;
    private LoginBonusData loginBonusData;
    private UUID playerUUID;

    public LoginBonusUserGUI(LoginBonusData loginBonusData, UUID playerUUID){
        this.loginBonusData = loginBonusData;
        this.playerUUID = playerUUID;
        userAccumulatedLoginBonusClaimGui();
        userRewardListGui();
        userContinuousLoginBonusClaimGui();

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
            if(i < canClaimRewardDay || i == canClaimRewardDay && hasClaimedToday(playerUUID, "accumulative")){
                chests[i] = LBItems.alreadyClaimedRewardForUserGlassIS(i + 1);
            }else if(i == canClaimRewardDay && i == daysBetween - 1){
                chests[i] = LBItems.bonusRewardForUserPlayerHeadIS(i + 1, true, daysBetween, loginBonusData.getLoginCount(playerUUID, currentLoginBonusName), currentLoginBonusName);
            }else if(i == daysBetween - 1){
                chests[i] = LBItems.bonusRewardForUserPlayerHeadIS(i + 1, false, daysBetween, loginBonusData.getLoginCount(playerUUID, currentLoginBonusName), currentLoginBonusName);
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

        int pageCount = (daysBetween / 28) + 1; //28=1ページに表示するアイテム数

        // 外周背景
        OutlinePane background = new OutlinePane(0, 0, 9, 6, Pane.Priority.LOWEST);
        Mask mask = new Mask(
                "111111111",
                "100000001",
                "100000001",
                "100000001",
                "100000001",
                "011101010"
        );
        background.applyMask(mask);
        background.addItem(new GuiItem(LBItems.backgroundBlackGlassIS()));
        background.setRepeat(true);
        userAccumulatedLoginBonusClaimGui.addPane(background);

        // 次のページ
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

        // 前のページ
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

        // 連続ログイン日数
        StaticPane loginStreakPane = new StaticPane(6, 5, 1, 1);
        loginStreakPane.addItem(new GuiItem(LBItems.loginStreakShowingPaperIS(loginBonusData.getLoginStreak(playerUUID, currentLoginBonusName)), event -> {
            Player player = (Player) event.getWhoClicked();
            updateUserContinuousLoginBonusClaimGui();
            getUserContinuousLoginBonusClaimGui().show(player);
        }), 0, 0);
        userAccumulatedLoginBonusClaimGui.addPane(loginStreakPane);

        // 表示するplayerHead、現在のページ数の表示を各ページに追加
        OutlinePane[] rewardPanes = new OutlinePane[pageCount];
        StaticPane[] currentPageCountPane = new StaticPane[pageCount];
        for(int i = 0; i < pageCount; i++){
            // 各ページで表示が変わらんところは同じように表示
            paginatedPane.addPane(i, background);
            paginatedPane.addPane(i, nextPagePane);
            paginatedPane.addPane(i, prevPagePane);

            // 現在のページ数の表示
            currentPageCountPane[i] = new StaticPane(4, 5, 1, 1);
            currentPageCountPane[i].addItem(new GuiItem(LBItems.displayPageCountGlassIS(i + 1, pageCount), event -> {
            }), 0, 0);
            paginatedPane.addPane(i, currentPageCountPane[i]);

            rewardPanes[i] = new OutlinePane(1, 1, 7, 4);
            for(int j = 0; j < 28; j++){
                int index = i * 28 + j;

                // playerHeadを表示し終えたらbreak
                if(index >= daysBetween){
                    break;
                }

                // 受取可能な日の報酬についての設定
                if (index == canClaimRewardDay) {
                    rewardPanes[i].addItem(new GuiItem(chests[index], event -> {
                        Player player = (Player) event.getWhoClicked();

                        // クリック時、抽選する報酬プールを変更するためにindexごとにpoolTypeを設定
                        String poolType;
                        if(index == daysBetween - 1){
                            poolType = "bonus";
                        } else if(index % 10 == 9){
                            poolType = "special";
                        }else{
                            poolType = "normal";
                        }

                        // 左クリック時、問題がなければ報酬渡す
                        if (event.isLeftClick()){
                            if (isInventoryFull(player)) {
                                player.sendMessage("§cインベントリに空きがないため、報酬を受け取れません");
                                player.closeInventory();
                            } else if(!(canClaimRewardDay == loginBonusData.getLoginCount(playerUUID, currentLoginBonusName) - 1)) {
                                player.sendMessage("§c日付が変わりました。もう一度ログインボーナス画面を開きなおしてください");
                                player.closeInventory();
                            }else{
                                ItemStack clickedSlotItem = event.getCurrentItem();
                                ItemMeta clickedSlotItemMeta = clickedSlotItem.getItemMeta();
                                // 2回目以降は報酬を取れないように
                                if (!(clickedSlotItemMeta.getDisplayName().contains("無効"))) {
                                    player.closeInventory();
                                    //TODO: 連打して受け取れたりしないかの確認
                                    ItemStack item = RewardManager.getRandomRewards(currentLoginBonusName, poolType);
                                    player.getInventory().addItem(item);
                                    loginBonusData.setClaimedItemStack(playerUUID, poolType, item.toString(), LocalDateTime.now());
                                    player.sendMessage(item.getType().name() + "×" + item.getAmount() + " §aを受け取りました！");
                                }else{
                                    player.sendMessage("§cこの報酬は無効であるため、受け取れません");
                                }
                            }
                            // poolTypeごとに表示する報酬プールを変更
                        } else if (event.isRightClick()){
                            ItemStack clickedSlotItem = event.getCurrentItem();
                            ItemMeta clickedSlotItemMeta = clickedSlotItem.getItemMeta();
                            String clickedRewardPoolType = "";
                            if (clickedSlotItemMeta.getDisplayName().contains("コモン")) {
                                clickedRewardPoolType = "normal";
                                updateUserRewardListGui(currentLoginBonusName, clickedRewardPoolType);
                                getUserRewardListGui().show(player);
                            }else if(clickedSlotItemMeta.getDisplayName().contains("レア")){
                                clickedRewardPoolType = "special";
                                updateUserRewardListGui(currentLoginBonusName, clickedRewardPoolType);
                                getUserRewardListGui().show(player);
                            }else if(clickedSlotItemMeta.getDisplayName().contains("エピック")) {
                                clickedRewardPoolType = "bonus";
                                updateUserRewardListGui(currentLoginBonusName, clickedRewardPoolType);
                                getUserRewardListGui().show(player);
                            }else if(clickedSlotItemMeta.getDisplayName().contains("無効")) {
                                player.sendMessage("§aこの報酬は無効であるため、報酬を閲覧できません");
                            }
                        }
                    }));
                } else {
                    rewardPanes[i].addItem(new GuiItem(chests[index], event -> {
                        Player player = (Player) event.getWhoClicked();
                        if (event.isLeftClick()){
                            player.sendMessage("§aこの報酬は受取済か、本日が受取可能な日ではないため受け取れません");
                            // poolTypeごとに表示する報酬プールを変更（ここは報酬が得られる日じゃなかろうが変わらん）
                        } else if (event.isRightClick()){
                            ItemStack clickedSlotItem = event.getCurrentItem();
                            ItemMeta clickedSlotItemMeta = clickedSlotItem.getItemMeta();
                            String clickedRewardPoolType = "";
                            if (clickedSlotItemMeta.getDisplayName().contains("コモン")) {
                                clickedRewardPoolType = "normal";
                                updateUserRewardListGui(currentLoginBonusName, clickedRewardPoolType);
                                getUserRewardListGui().show(player);
                            }else if((clickedSlotItemMeta.getDisplayName().contains("レア"))){
                                clickedRewardPoolType = "special";
                                updateUserRewardListGui(currentLoginBonusName, clickedRewardPoolType);
                                getUserRewardListGui().show(player);
                            }else if(clickedSlotItemMeta.getDisplayName().contains("エピック")) {
                                clickedRewardPoolType = "bonus";
                                updateUserRewardListGui(currentLoginBonusName, clickedRewardPoolType);
                                getUserRewardListGui().show(player);
                            }else if(clickedSlotItemMeta.getDisplayName().contains("無効")) {
                                player.sendMessage("§aこの報酬は無効であるため、報酬を閲覧できません");
                            }
                        }
                    }));
                }
            }
            paginatedPane.addPane(i, rewardPanes[i]);
        }
        userAccumulatedLoginBonusClaimGui.addPane(paginatedPane);
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
            if(i < canClaimRewardDay || i == canClaimRewardDay && hasClaimedToday(playerUUID, "accumulative")){
                chests[i] = LBItems.alreadyClaimedRewardForUserGlassIS(i + 1);
            }else if(i == canClaimRewardDay && i == daysBetween - 1){
                chests[i] = LBItems.bonusRewardForUserPlayerHeadIS(i + 1, true, daysBetween, loginBonusData.getLoginCount(playerUUID, currentLoginBonusName), currentLoginBonusName);
            }else if(i == daysBetween - 1){
                chests[i] = LBItems.bonusRewardForUserPlayerHeadIS(i + 1, false, daysBetween, loginBonusData.getLoginCount(playerUUID, currentLoginBonusName), currentLoginBonusName);
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

        int pageCount = (daysBetween / 28) + 1; //28=1ページに表示するアイテム数

        // 外周背景
        OutlinePane background = new OutlinePane(0, 0, 9, 6, Pane.Priority.LOWEST);
        Mask mask = new Mask(
                "111111111",
                "100000001",
                "100000001",
                "100000001",
                "100000001",
                "011101010"
        );
        background.applyMask(mask);
        background.addItem(new GuiItem(LBItems.backgroundBlackGlassIS()));
        background.setRepeat(true);
        userAccumulatedLoginBonusClaimGui.addPane(background);

        // 次のページ
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

        // 前のページ
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



        // 連続ログイン日数
        StaticPane loginStreakPane = new StaticPane(6, 5, 1, 1);
        loginStreakPane.addItem(new GuiItem(LBItems.loginStreakShowingPaperIS(loginBonusData.getLoginStreak(playerUUID, currentLoginBonusName)), event -> {
            Player player = (Player) event.getWhoClicked();
            updateUserContinuousLoginBonusClaimGui();
            getUserContinuousLoginBonusClaimGui().show(player);
        }), 0, 0);
        userAccumulatedLoginBonusClaimGui.addPane(loginStreakPane);

        // 表示するplayerHead、現在のページ数の表示を各ページに追加
        OutlinePane[] rewardPanes = new OutlinePane[pageCount];
        StaticPane[] currentPageCountPane = new StaticPane[pageCount];
        for(int i = 0; i < pageCount; i++){
            // 各ページで表示が変わらんところは同じように表示
            paginatedPane.addPane(i, background);
            paginatedPane.addPane(i, nextPagePane);
            paginatedPane.addPane(i, prevPagePane);

            // 現在のページ数の表示
            currentPageCountPane[i] = new StaticPane(4, 5, 1, 1);
            currentPageCountPane[i].addItem(new GuiItem(LBItems.displayPageCountGlassIS(i + 1, pageCount), event -> {
            }), 0, 0);
            paginatedPane.addPane(i, currentPageCountPane[i]);

            rewardPanes[i] = new OutlinePane(1, 1, 7, 4);
            for(int j = 0; j < 28; j++){
                int index = i * 28 + j;

                // playerHeadを表示し終えたらbreak
                if(index >= daysBetween){
                    break;
                }

                // 受取可能な日の報酬についての設定
                if (index == canClaimRewardDay) {
                    rewardPanes[i].addItem(new GuiItem(chests[index], event -> {
                        Player player = (Player) event.getWhoClicked();

                        // クリック時、抽選する報酬プールを変更するためにindexごとにpoolTypeを設定
                        String poolType;
                        if(index == daysBetween - 1){
                            poolType = "bonus";
                        } else if(index % 10 == 9){
                            poolType = "special";
                        }else{
                            poolType = "normal";
                        }

                        // 左クリック時、問題がなければ報酬渡す
                        if (event.isLeftClick()){
                            if (isInventoryFull(player)) {
                                player.sendMessage("§cインベントリに空きがないため、報酬を受け取れません");
                                player.closeInventory();
                            } else if(!(canClaimRewardDay == loginBonusData.getLoginCount(playerUUID, currentLoginBonusName) - 1)) {
                                player.sendMessage("§c日付が変わりました。もう一度ログインボーナス画面を開きなおしてください");
                                player.closeInventory();
                            }else{
                                ItemStack clickedSlotItem = event.getCurrentItem();
                                ItemMeta clickedSlotItemMeta = clickedSlotItem.getItemMeta();
                                // 2回目以降は報酬を取れないように
                                if (!(clickedSlotItemMeta.getDisplayName().contains("無効"))) {
                                    player.closeInventory();
                                    //TODO: 連打して受け取れたりしないかの確認
                                    ItemStack item = RewardManager.getRandomRewards(currentLoginBonusName, poolType);
                                    player.getInventory().addItem(item);
                                    loginBonusData.setClaimedItemStack(playerUUID, poolType, item.toString(), LocalDateTime.now());
                                    player.sendMessage(item.getItemMeta().displayName() + "×" + item.getAmount() + " §aを受け取りました！");
                                }else{
                                    player.sendMessage("§cこの報酬は無効であるため、受け取れません");
                                }
                            }
                            // poolTypeごとに表示する報酬プールを変更
                        } else if (event.isRightClick()){
                            ItemStack clickedSlotItem = event.getCurrentItem();
                            ItemMeta clickedSlotItemMeta = clickedSlotItem.getItemMeta();
                            String clickedRewardPoolType = "";
                            if (clickedSlotItemMeta.getDisplayName().contains("コモン")) {
                                clickedRewardPoolType = "normal";
                                updateUserRewardListGui(currentLoginBonusName, clickedRewardPoolType);
                                getUserRewardListGui().show(player);
                            }else if(clickedSlotItemMeta.getDisplayName().contains("レア")){
                                clickedRewardPoolType = "special";
                                updateUserRewardListGui(currentLoginBonusName, clickedRewardPoolType);
                                getUserRewardListGui().show(player);
                            }else if(clickedSlotItemMeta.getDisplayName().contains("エピック")) {
                                clickedRewardPoolType = "bonus";
                                updateUserRewardListGui(currentLoginBonusName, clickedRewardPoolType);
                                getUserRewardListGui().show(player);
                            }else if(clickedSlotItemMeta.getDisplayName().contains("無効")) {
                                player.sendMessage("§aこの報酬は無効であるため、報酬を閲覧できません");
                            }
                        }
                    }));
                } else {
                    rewardPanes[i].addItem(new GuiItem(chests[index], event -> {
                        Player player = (Player) event.getWhoClicked();
                        if (event.isLeftClick()){
                            player.sendMessage("§aこの報酬は受取済か、本日が受取可能な日ではないため受け取れません");
                            // poolTypeごとに表示する報酬プールを変更（ここは報酬が得られる日じゃなかろうが変わらん）
                        } else if (event.isRightClick()){
                            ItemStack clickedSlotItem = event.getCurrentItem();
                            ItemMeta clickedSlotItemMeta = clickedSlotItem.getItemMeta();
                            String clickedRewardPoolType = "";
                            if (clickedSlotItemMeta.getDisplayName().contains("コモン")) {
                                clickedRewardPoolType = "normal";
                                updateUserRewardListGui(currentLoginBonusName, clickedRewardPoolType);
                                getUserRewardListGui().show(player);
                            }else if((clickedSlotItemMeta.getDisplayName().contains("レア"))){
                                clickedRewardPoolType = "special";
                                updateUserRewardListGui(currentLoginBonusName, clickedRewardPoolType);
                                getUserRewardListGui().show(player);
                            }else if(clickedSlotItemMeta.getDisplayName().contains("エピック")) {
                                clickedRewardPoolType = "bonus";
                                updateUserRewardListGui(currentLoginBonusName, clickedRewardPoolType);
                                getUserRewardListGui().show(player);
                            }else if(clickedSlotItemMeta.getDisplayName().contains("無効")) {
                                player.sendMessage("§aこの報酬は無効であるため、報酬を閲覧できません");
                            }
                        }
                    }));
                }
            }
            paginatedPane.addPane(i, rewardPanes[i]);
        }
        userAccumulatedLoginBonusClaimGui.addPane(paginatedPane);
    }

    public void userRewardListGui() {
        userRewardListGui = new ChestGui(6, "報酬プール");
        userRewardListGui.setOnGlobalClick(event -> event.setCancelled(true));

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
        userRewardListGui.addPane(background);

        // 戻るボタン
        StaticPane saveItemPane = new StaticPane(4, 5, 1, 1);
        saveItemPane.addItem(new GuiItem(LBItems.returnLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            updateUserAccumulatedLoginBonusClaimGui();
            getUserAccumulatedLoginBonusClaimGui().show(player);
        }), 0, 0);
        userRewardListGui.addPane(saveItemPane);

        // チュートリアル
        StaticPane tutorialPane = new StaticPane(8, 5, 1, 1);
        tutorialPane.addItem(new GuiItem(LBItems.poolForUserTutorialBookIS(), event -> {
            event.setCancelled(true);
        }), 0, 0);
        userRewardListGui.addPane(tutorialPane);
    }

    public void updateUserRewardListGui(String currentBonusName, String poolType) {
        userRewardListGui.getPanes().removeIf(pane -> pane.getPriority() == Pane.Priority.HIGH);

        OutlinePane rewardItemPane = new OutlinePane(0, 0, 9, 5, Pane.Priority.HIGH);
        ArrayList<ItemStack> rewardItems = RewardManager.getAllRewards(currentBonusName, poolType);
        for (int i = 0; i < rewardItems.size(); i++) {
            ItemStack item = rewardItems.get(i);
            GuiItem guiItem = new GuiItem(item);
            rewardItemPane.addItem(guiItem);
        }
        userRewardListGui.addPane(rewardItemPane);
    }

    public void userContinuousLoginBonusClaimGui(){
        userContinuousLoginBonusClaimGui = new ChestGui(3, "連続ログボ受取");
        userContinuousLoginBonusClaimGui.setOnGlobalClick(event -> event.setCancelled(true));

        int needDayToClaim = 10;
        int streak = loginBonusData.getLoginStreak(playerUUID, RewardManager.getCurrentBonusName());
        String currentLoginBonusName = RewardManager.getCurrentBonusName();
        String poolType = "continuous";

        // 外周背景
        OutlinePane background = new OutlinePane(0, 0, 9, 3, Pane.Priority.LOWEST);
        Mask mask = new Mask(
                "111111111",
                "100000001",
                "111101111"
        );
        background.applyMask(mask);
        background.addItem(new GuiItem(LBItems.backgroundBlackGlassIS()));
        background.setRepeat(true);
        userContinuousLoginBonusClaimGui.addPane(background);

        StaticPane continuousRewardPane = new StaticPane(4, 1, 1, 1, Pane.Priority.HIGH);
        if (hasClaimedToday(playerUUID, "continuous")) {
            continuousRewardPane.addItem(new GuiItem(LBItems.alreadyClaimedContinuousRewardForUserGlassIS(), event -> {
                Player player = (Player) event.getWhoClicked();
                player.sendMessage("§c報酬は受け取り済みです");
            }), 0, 0);
        } else {
            continuousRewardPane.addItem(new GuiItem(LBItems.continuousRewardForUserPlayerHeadIS(streak, needDayToClaim), event -> {
                Player player = (Player) event.getWhoClicked();
                if(streak >= needDayToClaim){

                    // 左クリック時、問題がなければ報酬渡す
                    if (event.isLeftClick()) {
                        if (isInventoryFull(player)) {
                            player.sendMessage("§cインベントリに空きがないため、報酬を受け取れません");
                            player.closeInventory();
                        } else {
                            ItemStack clickedSlotItem = event.getCurrentItem();
                            ItemMeta clickedSlotItemMeta = clickedSlotItem.getItemMeta();
                            player.closeInventory();
                            //TODO: 連打して受け取れたりしないかの確認
                            ItemStack item = RewardManager.getRandomRewards(currentLoginBonusName, poolType);
                            player.getInventory().addItem(item);
                            loginBonusData.setClaimedItemStack(playerUUID, poolType, item.toString(), LocalDateTime.now());
                            player.sendMessage(item.getType().name() + "×" + item.getAmount() + " §aを受け取りました！");
                        }
                    } else if (event.isRightClick()) {
                        updateUserRewardListGui(currentLoginBonusName, poolType);
                        getUserRewardListGui().show(player);
                    }
                } else {
                    if (event.isLeftClick()) {
                        player.sendMessage("§c連続ログイン日数が足りません。" + needDayToClaim + "日必要です。現在: " + streak + "日");
                    }else if(event.isRightClick()){
                        updateUserRewardListGui(currentLoginBonusName, poolType);
                        getUserRewardListGui().show(player);
                    }
                }
            }), 0, 0);
        }
        userContinuousLoginBonusClaimGui.addPane(continuousRewardPane);

        StaticPane returnPane = new StaticPane(4, 2, 1, 1);
        returnPane.addItem(new GuiItem(LBItems.returnLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            updateUserAccumulatedLoginBonusClaimGui();
            getUserAccumulatedLoginBonusClaimGui().show(player);
        }), 0, 0);
        userContinuousLoginBonusClaimGui.addPane(returnPane);
    }

    public void updateUserContinuousLoginBonusClaimGui() {
        userContinuousLoginBonusClaimGui.getPanes().removeIf(pane -> pane.getPriority() == Pane.Priority.HIGH);

        int needDayToClaim = 10;
        int streak = loginBonusData.getLoginStreak(playerUUID, RewardManager.getCurrentBonusName());
        String currentLoginBonusName = RewardManager.getCurrentBonusName();
        String poolType = "continuous";

        StaticPane continuousRewardPane = new StaticPane(4, 1, 1, 1, Pane.Priority.HIGH);
        if (hasClaimedToday(playerUUID, "continuous")) {
            continuousRewardPane.addItem(new GuiItem(LBItems.alreadyClaimedContinuousRewardForUserGlassIS(), event -> {
                Player player = (Player) event.getWhoClicked();
                player.sendMessage("§c報酬は受け取り済みです");
            }), 0, 0);
        } else {
            continuousRewardPane.addItem(new GuiItem(LBItems.continuousRewardForUserPlayerHeadIS(streak, needDayToClaim), event -> {
                Player player = (Player) event.getWhoClicked();
                if(streak >= needDayToClaim){

                    // 左クリック時、問題がなければ報酬渡す
                    if (event.isLeftClick()) {
                        if (isInventoryFull(player)) {
                            player.sendMessage("§cインベントリに空きがないため、報酬を受け取れません");
                            player.closeInventory();
                        } else {
                            ItemStack clickedSlotItem = event.getCurrentItem();
                            ItemMeta clickedSlotItemMeta = clickedSlotItem.getItemMeta();
                            player.closeInventory();
                            //TODO: 連打して受け取れたりしないかの確認
                            ItemStack item = RewardManager.getRandomRewards(currentLoginBonusName, poolType);
                            player.getInventory().addItem(item);
                            loginBonusData.setClaimedItemStack(playerUUID, poolType, item.toString(), LocalDateTime.now());
                            player.sendMessage(item.getType().name() + "×" + item.getAmount() + " §aを受け取りました！");
                        }
                    } else if (event.isRightClick()) {
                        updateUserRewardListGui(currentLoginBonusName, poolType);
                        getUserRewardListGui().show(player);
                    }
                } else {
                    if (event.isLeftClick()) {
                        player.sendMessage("§c連続ログイン日数が足りません。" + needDayToClaim + "日必要です。現在: " + streak + "日");
                    }else if(event.isRightClick()){
                        updateUserRewardListGui(currentLoginBonusName, poolType);
                        getUserRewardListGui().show(player);
                    }
                }
            }), 0, 0);
        }
        userContinuousLoginBonusClaimGui.addPane(continuousRewardPane);
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

    public ChestGui getUserRewardListGui(){
        return userRewardListGui;
    }

    public ChestGui getAdminTestGui(){
        return adminTestGui;
    }

    public ChestGui getUserContinuousLoginBonusClaimGui(){
        return userContinuousLoginBonusClaimGui;
    }

    private boolean isInventoryFull(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType().isAir()) {
                return false;
            }
        }
        return true;
    }

    public boolean hasClaimedToday(UUID playerUUID, String poolType) {
        LocalDateTime lastClaimedDate = null;
        if (poolType.equals("accumulative")) {
            lastClaimedDate = loginBonusData.getLastAccumulatedRewardClaimedDate(playerUUID);
        }else if(poolType.equals("continuous")){
            lastClaimedDate = loginBonusData.getLastContinuousRewardClaimedDate(playerUUID);
        }

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