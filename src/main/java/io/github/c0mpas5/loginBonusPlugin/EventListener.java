package io.github.c0mpas5.loginBonusPlugin;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class EventListener implements Listener {

    private final LoginBonusPlugin plugin;
    private final LoginBonusData loginBonusData;

    public EventListener(LoginBonusPlugin plugin, LoginBonusData loginBonusData) {
        this.plugin = plugin;
        this.loginBonusData = loginBonusData;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        String serverName = Bukkit.getServer().getName();
        LocalDate baseDate = null; // 基準とする日時。ここから24時間先まで1日とみなす。

        String currentBonusName = RewardManager.getCurrentBonusName();
        if(currentBonusName == null){
            player.sendMessage("現在開催中のログインボーナスはありません");
            return;
        }
        int dailyResetHour = RewardManager.getDailyResetTime(currentBonusName);

//        // 現在時刻を取得
//        LocalDateTime currentDate = LocalDateTime.now();
//
//        // 基準とする日時を決定する。
//        if(currentDate.getHour() >= dailyResetHour){
//            baseDate = LocalDate.now();
//        }else{
//            baseDate = LocalDate.now().minusDays(1);
//        }
//
//        // 最終報酬取得日を取得
//        LocalDateTime lastClaimedDateTime = loginBonusData.getLastAccumulatedRewardClaimedDate(playerUUID);
//        if(lastClaimedDateTime == null){
//            loginBonusData.setClaimedCount(playerUUID, 0);
//            lastClaimedDateTime = LocalDateTime.now().minusDays(1);
//        }
//        LocalDate lastClaimedDate = null;
//        if(lastClaimedDateTime.getHour() >= dailyResetHour){
//            lastClaimedDate = lastClaimedDateTime.toLocalDate();
//        }else{
//            lastClaimedDate = lastClaimedDateTime.toLocalDate().minusDays(1);
//        }

        if (!hasClaimedToday(playerUUID, "accumulated", currentBonusName)) {
            sendOpenAccumulatedRewardGuiMessage(player);
            int loginCount = loginBonusData.getLoginCount(playerUUID, currentBonusName);
            player.sendMessage("ログイン" + loginCount + "日目！");
        } else {
            player.sendMessage("受取可能なログインボーナスはありません。");
        }
        if (!hasClaimedToday(playerUUID, "continuous", currentBonusName) && loginBonusData.getLoginStreak(playerUUID, currentBonusName) == 10) {
            player.sendMessage("連続ログイン" + loginBonusData.getLoginStreak(playerUUID, currentBonusName) + "日目！以下をクリックで連続ログインボーナス報酬を受け取れます");
            sendOpenContinuousRewardGuiMessage(player);
        }else{
            player.sendMessage("連続ログイン" + loginBonusData.getLoginStreak(playerUUID, currentBonusName) + "日目！");
        }
    }

    public void sendOpenAccumulatedRewardGuiMessage(Player player){
        TextComponent message = new TextComponent("ログボ受取");
        message.setColor(ChatColor.YELLOW);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/loginbonus total"));
        player.spigot().sendMessage(message);
    }

    public void sendOpenContinuousRewardGuiMessage(Player player){
        TextComponent message = new TextComponent("連続ログボ受取");
        message.setColor(ChatColor.YELLOW);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/loginbonus streak"));
        player.spigot().sendMessage(message);
    }

    public boolean hasClaimedToday(UUID playerUUID, String poolType, String currentBonusName) {
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
}