package io.github.c0mpas5.loginBonusPlugin;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.component.Slider;
import com.github.stefvanschie.inventoryframework.pane.util.Mask;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import red.man10.man10score.ScoreDatabase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class LoginBonusUserGUI implements Listener {
    private ChestGui userAccumulatedLoginBonusClaimGui;
    private ChestGui userRewardListGui;
    private ChestGui userContinuousLoginBonusClaimGui;

    private ChestGui adminTestGui;
    private LoginBonusData loginBonusData;
    private LoginBonusPlugin plugin;
    private UUID playerUUID;
    private String currentBonusName;
    private LocalDateTime initDateTime;
    private String messagePrefix;
    boolean processing = false;

    public LoginBonusUserGUI(LoginBonusData loginBonusData, UUID playerUUID, LoginBonusPlugin plugin) {
        messagePrefix = "§6§l[LoginBonusPlugin] §r";
        currentBonusName = RewardManager.getCurrentBonusName();
        initDateTime = LocalDateTime.now();

        this.loginBonusData = loginBonusData;
        this.plugin = plugin;

        this.playerUUID = playerUUID;
        userAccumulatedLoginBonusClaimGui();
        userRewardListGui();
        userContinuousLoginBonusClaimGui();
    }



    public void userAccumulatedLoginBonusClaimGui(){
        userAccumulatedLoginBonusClaimGui = new ChestGui(6, "累積ログボ受取");
        userAccumulatedLoginBonusClaimGui.setOnGlobalClick(event -> event.setCancelled(true));
    }

    public void updateUserAccumulatedLoginBonusClaimGui() {
        // 全部再生成
        userAccumulatedLoginBonusClaimGui.getPanes().clear();

        Thread th = new Thread(() -> {

        //全ページ
        PaginatedPane paginatedPane = new PaginatedPane(0, 0, 9, 6);
        int loginCount = loginBonusData.getLoginCount(playerUUID, currentBonusName); // 当日も含めたログイン日数
        int loginStreak = loginBonusData.getLoginStreak(playerUUID, currentBonusName); // 連続ログイン日数
        LocalDate startDate = RewardManager.getPeriodStartDate(currentBonusName);
        LocalDate endDate = RewardManager.getPeriodEndDate(currentBonusName);
        int daysBetween;
        if (startDate == null || endDate == null) {
            return;
        } else {
            daysBetween = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
        }
        ItemStack[] chests = new ItemStack[daysBetween];


        int canClaimRewardDay = loginCount - 1; // ログインボーナスを受け取れる日（インデックス番号）
        LocalDate currentDate = LocalDateTime.now().minusHours(RewardManager.getDailyResetTime(currentBonusName)).toLocalDate();
        int daysSinceStart = (int) ChronoUnit.DAYS.between(startDate, currentDate) + 1; // 開催されてからの経過日数

        // 開催終了までの残り日数と時間を計算
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDateTime = endDate.plusDays(1).atTime(RewardManager.getDailyResetTime(currentBonusName), 0);
        long daysUntil = ChronoUnit.DAYS.between(now, endDateTime);
        long hoursUntil = ChronoUnit.HOURS.between(now.plusDays(daysUntil), endDateTime);
        String daysUntilEnd = daysUntil + "日" + hoursUntil + "時間"; // 開催終了までの残り日数と時間

        // 表示するplayerHeadの配列作る
        for (int i = 0; i < daysBetween; i++) {
            if (i < canClaimRewardDay || i == canClaimRewardDay && hasClaimedToday(playerUUID, "accumulated")) {
                chests[i] = LBItems.alreadyClaimedRewardForUserGlassIS(i + 1);
            } else if (i == canClaimRewardDay && i == daysBetween - (daysSinceStart - loginCount) - 1) { // 開催日数から逃がした日数を引く（後ろの-1はインデックス番号用に調整するもの）
                chests[i] = LBItems.bonusRewardForUserPlayerHeadIS(i + 1, true, daysBetween, loginCount, currentBonusName);
            } else if (i == daysBetween - (daysSinceStart - loginCount) - 1) {
                chests[i] = LBItems.bonusRewardForUserPlayerHeadIS(i + 1, false, daysBetween, loginCount, currentBonusName);
            } else if (i % 10 == 9 && i == canClaimRewardDay) {
                chests[i] = LBItems.specialRewardForUserPlayerHeadIS(i + 1, true);
            } else if (i % 10 == 9) {
                chests[i] = LBItems.specialRewardForUserPlayerHeadIS(i + 1, false);
            } else if (i == canClaimRewardDay) {
                chests[i] = LBItems.normalRewardForUserPlayerHeadIS(i + 1, true);
            } else {
                chests[i] = LBItems.normalRewardForUserPlayerHeadIS(i + 1, false);
            }
        }

        int pageCount = (daysBetween / 28) + 1; //28=1ページに表示するアイテム数

        // 外周背景
        OutlinePane background = new OutlinePane(0, 0, 9, 6, Pane.Priority.LOWEST);
        Mask mask = new Mask(
                "111111111",
                "100000001",
                "000000001",
                "000000001",
                "100000001",
                "011101010"
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
                Bukkit.getScheduler().runTask(plugin, new Runnable()
                {
                    @Override
                    public void run()
                    {
                        userAccumulatedLoginBonusClaimGui.update();
                    }
                });
            } else {
                Bukkit.getScheduler().runTask(plugin, new Runnable()
                {
                    @Override
                    public void run()
                    {
                        player.sendMessage(messagePrefix + "§cこのページが末尾のページであるため、次ページを開けません。");
                    }
                });
            }
        }), 0, 0);

        // 前のページ
        StaticPane prevPagePane = new StaticPane(0, 5, 1, 1);
        prevPagePane.addItem(new GuiItem(LBItems.prevPageAquaPlayerHeadIS(), event -> {
            Player player = (Player) event.getWhoClicked();
            if (paginatedPane.getPage() > 0) {
                paginatedPane.setPage(paginatedPane.getPage() - 1);
                Bukkit.getScheduler().runTask(plugin, new Runnable()
                {
                    @Override
                    public void run()
                    {
                        userAccumulatedLoginBonusClaimGui.update();
                    }
                });
            } else {
                Bukkit.getScheduler().runTask(plugin, new Runnable()
                {
                    @Override
                    public void run()
                    {
                        player.sendMessage(messagePrefix + "§cこのページが先頭のページであるため、前ページを開けません。");
                    }
                });
            }
        }), 0, 0);


        // 連続ログイン日数
        StaticPane loginStreakPane = new StaticPane(6, 5, 1, 1);
        loginStreakPane.addItem(new GuiItem(LBItems.loginStreakShowingPaperIS(loginStreak), event -> {
            Player player = (Player) event.getWhoClicked();

            // クリック時、日付が変わってたらインベントリ閉じて処理中断
            if (closeInvWhenDayChanged(player)) {
                return;
            }

            Bukkit.getScheduler().runTask(plugin, new Runnable()
            {
                @Override
                public void run()
                {
                    updateUserContinuousLoginBonusClaimGui();
                    getUserContinuousLoginBonusClaimGui().show(player);
                }
            });
        }), 0, 0);
//        userAccumulatedLoginBonusClaimGui.addPane(loginStreakPane);

        //チュートリアル1
        StaticPane ruleTutorialPane = new StaticPane(0, 2, 1, 1);
        ruleTutorialPane.addItem(new GuiItem(LBItems.ruleTutorialBookForUserIS(currentBonusName), event -> {
        }), 0, 0);
//        userAccumulatedLoginBonusClaimGui.addPane(ruleTutorialPane);

        //チュートリアル2
        StaticPane rewardTutorialPane = new StaticPane(0, 3, 1, 1);
        rewardTutorialPane.addItem(new GuiItem(LBItems.rewardTutorialBookForUserIS(currentBonusName), event -> {
        }), 0, 0);
//        userAccumulatedLoginBonusClaimGui.addPane(rewardTutorialPane);

        // 表示するplayerHead、現在のページ数の表示を各ページに追加
        OutlinePane[] rewardPanes = new OutlinePane[pageCount];
        StaticPane[] currentPageCountPane = new StaticPane[pageCount];
        for (int i = 0; i < pageCount; i++) {
            // 各ページで表示が変わらんところは同じように表示
            paginatedPane.addPane(i, background);
            paginatedPane.addPane(i, nextPagePane);
            paginatedPane.addPane(i, prevPagePane);
            paginatedPane.addPane(i, loginStreakPane);
            paginatedPane.addPane(i, ruleTutorialPane);
            paginatedPane.addPane(i, rewardTutorialPane);

            // 現在のページ数の表示
            currentPageCountPane[i] = new StaticPane(4, 5, 1, 1);
            currentPageCountPane[i].addItem(new GuiItem(LBItems.displayPageCountGlassIS(i + 1, pageCount), event -> {
            }), 0, 0);
            paginatedPane.addPane(i, currentPageCountPane[i]);

            rewardPanes[i] = new OutlinePane(1, 1, 7, 4);
            for (int j = 0; j < 28; j++) {
                int index = i * 28 + j;

                // playerHeadを表示し終えたらbreak
                if (index >= daysBetween) {
                    break;
                }

                // 受取可能な日の報酬についての設定
                if (index == canClaimRewardDay) {
                    rewardPanes[i].addItem(new GuiItem(chests[index], event -> {
                        Player player = (Player) event.getWhoClicked();

                        // クリック時、日付が変わってたらインベントリ閉じて処理中断
                        if (closeInvWhenDayChanged(player)) {
                            return;
                        }

                        // クリック時、抽選する報酬プールを変更するためにindexごとにpoolTypeを設定
                        String poolType;
                        if (index == daysBetween - (daysSinceStart - loginCount) - 1) {
                            poolType = "bonus";
                        } else if (index % 10 == 9) {
                            poolType = "special";
                        } else {
                            poolType = "normal";
                        }

                        // 左クリック時、問題がなければ報酬渡す
                        if (event.isLeftClick()) {
                            if(processing){
                                Bukkit.getScheduler().runTask(plugin, new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        player.sendMessage(messagePrefix + "§c複数の受け取り処理を同時に実行することはできません");
                                    }
                                });
                                return;
                            }
                            processing = true;
                            if (isInventoryFull(player)) {
                                Bukkit.getScheduler().runTask(plugin, new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        player.sendMessage(messagePrefix + "§cインベントリに空きがないため、報酬を受け取れません");
                                        player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
                                        player.closeInventory();
                                    }
                                });
                            } else if (!(canClaimRewardDay == loginCount - 1)) { // これいる？
                                Bukkit.getScheduler().runTask(plugin, new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        player.sendMessage(messagePrefix + "§c日付が変わりました。もう一度ログインボーナス画面を開きなおしてください");
                                        player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
                                        player.closeInventory();
                                    }
                                });
                            } else {
                                // specialの時のサブ垢処理
                                if (poolType.equals("special") || poolType.equals("bonus")) {
                                    int accountCount = checkSubAccountClaimedCount(player.getUniqueId(), index, poolType);
                                    int accountLimit = RewardManager.getAccountRewardLimit();
                                    if(accountCount >= accountLimit){
                                        Bukkit.getScheduler().runTask(plugin, new Runnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                player.sendMessage(messagePrefix + "§c同一報酬を受取可能なアカウント数の上限に達したため、報酬を受け取れません");
                                                player.sendMessage("§c（上限：" + accountLimit + " アカウント）");
                                                player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
                                                player.closeInventory();
                                            }
                                        });
                                        return;
                                    }
                                }

                                ItemStack clickedSlotItem = event.getCurrentItem();
                                ItemMeta clickedSlotItemMeta = clickedSlotItem.getItemMeta();
                                if(clickedSlotItemMeta.getDisplayName().contains("エピック") && clickedSlotItemMeta.getLore().get(0).contains("受取不可")){
                                    Bukkit.getScheduler().runTask(plugin, new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            player.sendMessage(messagePrefix + "§cボーナス枠報酬の受け取り条件を満たしていないため、この報酬は受け取れません");
                                            player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
                                        }
                                    });
                                } else if (clickedSlotItemMeta.getDisplayName().contains("無効")) { // 2回目以降は報酬を取れないように
                                    Bukkit.getScheduler().runTask(plugin, new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            player.sendMessage(messagePrefix + "§cこの報酬は無効であるため、受け取れません");
                                            player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
                                        }
                                    });
                                } else {
                                    ItemStack item = RewardManager.getRandomRewards(currentBonusName, poolType);
                                    Bukkit.getScheduler().runTask(plugin, new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            player.closeInventory();
                                            player.getInventory().addItem(item);
                                            player.sendMessage(Component.text(messagePrefix).append(getItemDisplayName(item)).append(Component.text(" §f×" + item.getAmount() + " §aを受け取りました！")));
                                            player.playSound(player.getLocation(), "minecraft:entity.player.levelup", 1.0f, 1.0f);
                                        }
                                    });
                                    loginBonusData.setClaimedItemStack(playerUUID, currentBonusName, index + 1, poolType, item.toString(), LocalDateTime.now());
                                }
                            }
                            processing = false;
                            // poolTypeごとに表示する報酬プールを変更
                        } else if (event.isRightClick()) {
                            ItemStack clickedSlotItem = event.getCurrentItem();
                            ItemMeta clickedSlotItemMeta = clickedSlotItem.getItemMeta();
                            Bukkit.getScheduler().runTask(plugin, new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    String clickedRewardPoolType = "";
                                    if (clickedSlotItemMeta.getDisplayName().contains("コモン")) {
                                        clickedRewardPoolType = "normal";
                                        updateUserRewardListGui(clickedRewardPoolType);
                                        getUserRewardListGui().show(player);
                                    } else if (clickedSlotItemMeta.getDisplayName().contains("レア")) {
                                        clickedRewardPoolType = "special";
                                        updateUserRewardListGui(clickedRewardPoolType);
                                        getUserRewardListGui().show(player);
                                    } else if (clickedSlotItemMeta.getDisplayName().contains("エピック")) {
                                        clickedRewardPoolType = "bonus";
                                        updateUserRewardListGui(clickedRewardPoolType);
                                        getUserRewardListGui().show(player);
                                    } else if (clickedSlotItemMeta.getDisplayName().contains("無効")) {
                                        player.sendMessage(messagePrefix + "§cこの報酬は無効であるため、報酬を閲覧できません");
                                    }
                                }
                            });
                        }
                    }));
                } else {
                    rewardPanes[i].addItem(new GuiItem(chests[index], event -> {
                        Player player = (Player) event.getWhoClicked();

                        // クリック時、日付が変わってたらインベントリ閉じて処理中断
                        if (closeInvWhenDayChanged(player)) {
                            return;
                        }

                        Bukkit.getScheduler().runTask(plugin, new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                if (event.isLeftClick()) {
                                    player.sendMessage(messagePrefix + "§cこの報酬は受取済か、本日が受取可能な日ではないため受け取れません");
                                    player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
                                    // poolTypeごとに表示する報酬プールを変更（ここは報酬が得られる日じゃなかろうが変わらん）
                                } else if (event.isRightClick()) {
                                    ItemStack clickedSlotItem = event.getCurrentItem();
                                    ItemMeta clickedSlotItemMeta = clickedSlotItem.getItemMeta();
                                    String clickedRewardPoolType = "";
                                    if (clickedSlotItemMeta.getDisplayName().contains("コモン")) {
                                        clickedRewardPoolType = "normal";
                                        updateUserRewardListGui(clickedRewardPoolType);
                                        getUserRewardListGui().show(player);
                                    } else if ((clickedSlotItemMeta.getDisplayName().contains("レア"))) {
                                        clickedRewardPoolType = "special";
                                        updateUserRewardListGui(clickedRewardPoolType);
                                        getUserRewardListGui().show(player);
                                    } else if (clickedSlotItemMeta.getDisplayName().contains("エピック")) {
                                        clickedRewardPoolType = "bonus";
                                        updateUserRewardListGui(clickedRewardPoolType);
                                        getUserRewardListGui().show(player);
                                    } else if (clickedSlotItemMeta.getDisplayName().contains("無効")) {
                                        player.sendMessage(messagePrefix + "§cこの報酬は無効であるため、報酬を閲覧できません");
                                        player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
                                    }
                                }
                            }
                        });
                    }));
                }
            }
            paginatedPane.addPane(i, rewardPanes[i]);
        }
            Bukkit.getScheduler().runTask(plugin, new Runnable()
            {
                @Override
                public void run()
                {
                    userAccumulatedLoginBonusClaimGui.addPane(paginatedPane);
                    userAccumulatedLoginBonusClaimGui.update();
                    userAccumulatedLoginBonusClaimGui.setTitle("累積ログボ §l⏰残り" + daysUntilEnd);
                }
            });
        });
        th.start();
    }

    //TODO: こっから
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

            // クリック時、日付が変わってたらインベントリ閉じて処理中断
            if(closeInvWhenDayChanged(player)){
                return;
            }

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

    public void updateUserRewardListGui(String poolType) {
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

        StaticPane returnPane = new StaticPane(4, 2, 1, 1);
        returnPane.addItem(new GuiItem(LBItems.returnLimeGlassIS(), event -> {
            Player player = (Player) event.getWhoClicked();

            // クリック時、日付が変わってたらインベントリ閉じて処理中断
            if(closeInvWhenDayChanged(player)){
                return;
            }

            updateUserAccumulatedLoginBonusClaimGui();
            getUserAccumulatedLoginBonusClaimGui().show(player);
        }), 0, 0);
        userContinuousLoginBonusClaimGui.addPane(returnPane);
    }

    public void updateUserContinuousLoginBonusClaimGui() {
        userContinuousLoginBonusClaimGui.getPanes().removeIf(pane -> pane.getPriority() == Pane.Priority.HIGH);

        Thread th = new Thread(() -> {
            int needDayToClaim = 10;
            int streak = loginBonusData.getLoginStreak(playerUUID, currentBonusName);
            String poolType = "continuous";

            StaticPane continuousRewardPane = new StaticPane(4, 1, 1, 1, Pane.Priority.HIGH);
            if (hasClaimedToday(playerUUID, "continuous")) {
                continuousRewardPane.addItem(new GuiItem(LBItems.alreadyClaimedContinuousRewardForUserGlassIS(), event -> {
                    Player player = (Player) event.getWhoClicked();

                    // クリック時、日付が変わってたらインベントリ閉じて処理中断
                    if (closeInvWhenDayChanged(player)) {
                        return;
                    }
                    Bukkit.getScheduler().runTask(plugin, new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            player.sendMessage(messagePrefix + "§c報酬は受け取り済みです");
                            player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
                        }
                    });
                }), 0, 0);
            } else {
                continuousRewardPane.addItem(new GuiItem(LBItems.continuousRewardForUserPlayerHeadIS(streak, needDayToClaim), event -> {
                    Player player = (Player) event.getWhoClicked();

                    // クリック時、日付が変わってたらインベントリ閉じて処理中断
                    if (closeInvWhenDayChanged(player)) {
                        return;
                    }

                    if (streak >= needDayToClaim) {

                        // 左クリック時、問題がなければ報酬渡す
                        if (event.isLeftClick()) {
                            if (processing) {
                                Bukkit.getScheduler().runTask(plugin, new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        player.sendMessage(messagePrefix + "§c複数の受け取り処理を同時に実行することはできません");
                                    }
                                });
                                return;
                            }
                            processing = true;
                            if (isInventoryFull(player)) {
                                Bukkit.getScheduler().runTask(plugin, new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        player.sendMessage(messagePrefix + "§cインベントリに空きがないため、報酬を受け取れません");
                                        player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
                                        player.closeInventory();
                                    }
                                });
                            } else {
                                int claimedContinuousRewardNum = loginBonusData.getNumOfClaimedContinuousReward(playerUUID, currentBonusName); // 最後に受け取った連続ログボ報酬の番号
                                int accountLimit = RewardManager.getAccountRewardLimit();
                                // 連続ログボのサブ垢処理
                                int accountCount = checkSubAccountClaimedCount(player.getUniqueId(), claimedContinuousRewardNum, poolType);
                                if(accountCount >= accountLimit){
                                    Bukkit.getScheduler().runTask(plugin, new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            player.sendMessage(messagePrefix + "§c同一報酬を受取可能なアカウント数の上限に達したため、報酬を受け取れません");
                                            player.sendMessage("§c（上限：" + accountLimit + " アカウント）");
                                            player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
                                            player.closeInventory();
                                        }
                                    });
                                    return;
                                }

                                ItemStack item = RewardManager.getRandomRewards(currentBonusName, poolType);
                                Bukkit.getScheduler().runTask(plugin, new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        player.closeInventory();
                                        player.getInventory().addItem(item);
                                        player.sendMessage(Component.text(messagePrefix).append(getItemDisplayName(item)).append(Component.text(" §f×" + item.getAmount() + " §aを受け取りました！")));
                                        player.playSound(player.getLocation(), "minecraft:entity.player.levelup", 1.0f, 1.0f);
                                    }
                                });

                                loginBonusData.setClaimedItemStack(playerUUID, currentBonusName, claimedContinuousRewardNum, poolType, item.toString(), LocalDateTime.now());
                            }
                            processing = false;
                        } else if (event.isRightClick()) {
                            updateUserRewardListGui(poolType);
                            Bukkit.getScheduler().runTask(plugin, new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    getUserRewardListGui().show(player);
                                }
                            });
                        }
                    } else {
                        Bukkit.getScheduler().runTask(plugin, new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                if (event.isLeftClick()) {
                                    player.sendMessage(messagePrefix + "§c連続ログイン日数が足りません。" + needDayToClaim + "日必要です。現在: " + streak + "日");
                                    player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
                                } else if (event.isRightClick()) {
                                    updateUserRewardListGui(poolType);
                                    getUserRewardListGui().show(player);
                                }
                            }
                        });
                    }
                }), 0, 0);
            }
            Bukkit.getScheduler().runTask(plugin, new Runnable()
            {
                @Override
                public void run()
                {
                    userContinuousLoginBonusClaimGui.addPane(continuousRewardPane);
                    userContinuousLoginBonusClaimGui.update();
                    Bukkit.getPlayer(playerUUID).sendMessage(messagePrefix + "GUIを更新しました");
                }
            });
        });
        th.start();
    }

    public ChestGui getUserAccumulatedLoginBonusClaimGui(){
        return userAccumulatedLoginBonusClaimGui;
    }

    public ChestGui getUserRewardListGui(){
        return userRewardListGui;
    }

    public ChestGui getUserContinuousLoginBonusClaimGui(){
        return userContinuousLoginBonusClaimGui;
    }

    private boolean isInventoryFull(Player player) {
        // メインインベントリ（0-35）のみをチェック
        ItemStack[] contents = player.getInventory().getStorageContents();
        for (ItemStack item : contents) {
            if (item == null || item.getType().isAir()) {
                return false;
            }
        }
        return true;
    }

//    private String getItemDisplayName(ItemStack item) {
//        if (item.getItemMeta() != null && item.getItemMeta().hasDisplayName()) {
//            return item.getItemMeta().getDisplayName();
//        } else {
//            return item.getType().name();
//        }
//    }

    private Component getItemDisplayName(ItemStack item) {
        if (item.getItemMeta() != null && item.getItemMeta().hasDisplayName()) {
            return item.getItemMeta().displayName();
        } else {
            return Component.translatable(item.getType().getItemTranslationKey());
        }
    }

    public boolean hasClaimedToday(UUID playerUUID, String poolType) {
        LocalDateTime lastClaimedDate = null;
        if (poolType.equals("accumulated")) {
            lastClaimedDate = loginBonusData.getLastRewardClaimedDate(playerUUID, "accumulated", currentBonusName);
        }else if(poolType.equals("continuous")){
            lastClaimedDate = loginBonusData.getLastRewardClaimedDate(playerUUID, "continuous", currentBonusName);
        }

        if (lastClaimedDate == null) {
            return false;
        }

        int resetHour = RewardManager.getDailyResetTime(currentBonusName);
        LocalDateTime resetDateTime = LocalDate.now().atTime(LocalTime.of(resetHour, 0));
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(resetDateTime)) {
            resetDateTime = resetDateTime.minusDays(1);
        }

        return lastClaimedDate.isAfter(resetDateTime);
    }

    public boolean isCurrentBonusNameChanged() {
        String accurateCurrentBonusName = RewardManager.getCurrentBonusName();
        return !accurateCurrentBonusName.equals(this.currentBonusName);
    }

    public boolean closeInvWhenDayChanged(Player player) {
        //dailyResetTimeを考慮した、インスタンス生成日
        LocalDate initDate = initDateTime.minusHours(RewardManager.getDailyResetTime(currentBonusName)).toLocalDate();
        //dailyResetTimeを考慮した、現在の日付
        LocalDate nowDate = LocalDateTime.now().minusHours(RewardManager.getDailyResetTime(currentBonusName)).toLocalDate();
        // もし、インスタンス生成日が現在の日付よりも前なら、日付が変わったとみなす

        if(initDate.isBefore(nowDate)){
            // 日付が変わった場合、開催中のログインボーナスを再取得
            currentBonusName = RewardManager.getCurrentBonusName();
            Bukkit.getScheduler().runTask(plugin, new Runnable()
            {
                @Override
                public void run()
                {
                    player.closeInventory();
                    player.sendMessage(messagePrefix + "§c日付が変わりました。もう一度ログインボーナス画面を開きなおしてください");
                    player.playSound(player.getLocation(), "minecraft:block.note_block.bass", 1.0f, 0.7f);
                }
            });
            return true;
        } else {
            return false;
        }
    }

    // サブスレッドでしか呼ばれない。現在のアカウントも含む、特定の報酬を受取済みのアカウントの所持数を返す
    public int checkSubAccountClaimedCount(UUID uuid, int index, String poolType) {
        List<UUID> accounts = ScoreDatabase.INSTANCE.getSubAccount(uuid);
        int accountCount = 1; // 現在のアカウントをカウントに含める

        if(poolType.equals("special")){
            for (UUID account : accounts) {
                // インデックス番号ではなく、〇日目の方で扱うので+1する
                if (loginBonusData.hasPlayerClaimedBonusForDayAndPoolType(account, index + 1, "special", currentBonusName)) {
                    accountCount++;
                }
            }
        } else if(poolType.equals("bonus")) {
            for (UUID account : accounts) {
                if (loginBonusData.hasPlayerClaimedBonusForPoolType(account, "bonus", currentBonusName)) {
                    accountCount++;
                }
            }
        } else if(poolType.equals("continuous")){
            for (UUID account : accounts) {
                // インデックス番号ではなく、〇回目の方で扱うので+1する
                if (loginBonusData.hasPlayerClaimedBonusForDayAndPoolType(account, index + 1, "continuous", currentBonusName)) {
                    accountCount++;
                }
            }
        }

        return accountCount;
    }
}