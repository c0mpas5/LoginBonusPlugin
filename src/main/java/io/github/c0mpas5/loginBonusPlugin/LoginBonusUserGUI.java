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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static org.bukkit.Bukkit.getServer;

public class LoginBonusUserGUI implements Listener {
    private ChestGui userAccumulatedLoginBonusClaimGui;

    private ChestGui adminTestGui;

    // 仮置き（開催中のログボを判断して代入する必要あり）
    private String currentLoginBonusName = RewardManager.getCurrentBonusName();

    public LoginBonusUserGUI(){
        userAccumulatedLoginBonusClaimGui();

        adminTestGui();
    }



    public void userAccumulatedLoginBonusClaimGui(){
        userAccumulatedLoginBonusClaimGui = new ChestGui(6, "累積ログボ受取");
        userAccumulatedLoginBonusClaimGui.setOnGlobalClick(event -> event.setCancelled(true));

        //全ページ
        PaginatedPane paginatedPane = new PaginatedPane(0, 0, 9, 6);
        LocalDate startDate = RewardManager.getPeriodStartDate(currentLoginBonusName);
        LocalDate endDate = RewardManager.getPeriodEndDate(currentLoginBonusName);
        int daysBetween;
        if(startDate == null || endDate == null){
            daysBetween = 10; // 仮置き
        }else{
            daysBetween = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
        }
        ItemStack[] chests = new ItemStack[daysBetween];

        // 表示するplayerHeadの配列作る
        for(int i = 0; i < daysBetween; i++){
            if(i % 10 == 9) {
                chests[i] = LBItems.specialRewardForUserPlayerHeadIS();
            }else{
                chests[i] = LBItems.normalRewardForUserPlayerHeadIS();
            }
        }

        int pageCount = (daysBetween / 28) + 1; //28...1ページに表示するアイテム数
        int canClaimRewardDay = 0; // ログインボーナスを受け取れる日

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
                if(index == canClaimRewardDay){
                    rewardPanes[i].addItem(new GuiItem(chests[index], event -> {
                        Player player = (Player) event.getWhoClicked();
                        // 【未】クリックしたら受け取れる処理を追加
                        player.sendMessage("§a報酬を受け取りました！");
                        event.setCancelled(true);
                    }));
                }else {
                    rewardPanes[i].addItem(new GuiItem(chests[index], event -> {
                        Player player = (Player) event.getWhoClicked();
                        player.sendMessage("§aこの報酬は受取済か、本日が受取可能な日ではないため受け取れません。");
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
        // 一旦全部再生成。絶対重いので改善すべき
        userAccumulatedLoginBonusClaimGui.getPanes().clear();

        //全ページ
        PaginatedPane paginatedPane = new PaginatedPane(0, 0, 9, 6);
        LocalDate startDate = RewardManager.getPeriodStartDate(currentLoginBonusName);
        LocalDate endDate = RewardManager.getPeriodEndDate(currentLoginBonusName);
        int daysBetween;
        if(startDate == null || endDate == null){
            daysBetween = 10; // 仮置き
        }else{
            daysBetween = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
        }
        ItemStack[] chests = new ItemStack[daysBetween];

        // 表示するplayerHeadの配列作る
        for(int i = 0; i < daysBetween; i++){
            if(i % 10 == 9) {
                chests[i] = LBItems.specialRewardForUserPlayerHeadIS();
            }else{
                chests[i] = LBItems.normalRewardForUserPlayerHeadIS();
            }
        }

        int pageCount = (daysBetween / 28) + 1; //28...1ページに表示するアイテム数
        int canClaimRewardDay = 0; // ログインボーナスを受け取れる日

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
                int test_i = i;
                int test_j = j;
                if(index >= daysBetween){
                    break;
                }
                if(index == canClaimRewardDay){
                    rewardPanes[i].addItem(new GuiItem(chests[index], event -> {
                        Player player = (Player) event.getWhoClicked();
                        // 【未】クリックしたら受け取れる処理を追加
                        //player.sendMessage("§a報酬を受け取りました！");
                        player.sendMessage("§ai=" + test_i + ", j=" + test_j + ", index=" + index);
                        event.setCancelled(true);
                    }));
                }else {
                    rewardPanes[i].addItem(new GuiItem(chests[index], event -> {
                        Player player = (Player) event.getWhoClicked();
                        //player.sendMessage("§aこの報酬は受取済か、本日が受取可能な日ではないため受け取れません。");
                        player.sendMessage("§ai=" + test_i + ", j=" + test_j + ", index=" + index);
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
}