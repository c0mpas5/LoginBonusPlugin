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
        int dailyResetHour = RewardManager.getDailyResetTime(currentBonusName);

        // 現在時刻を取得
        LocalDateTime currentDate = LocalDateTime.now();

        // 基準とする日時を決定する。
        if(currentDate.getHour() >= dailyResetHour){
            baseDate = LocalDate.now();
        }else{
            baseDate = LocalDate.now().minusDays(1);
        }

        // 最終報酬取得日を取得
        LocalDateTime lastClaimedDateTime = loginBonusData.getLastClaimedDate(playerUUID);
        if(lastClaimedDateTime == null){
            loginBonusData.setClaimedCount(playerUUID, 0);
            lastClaimedDateTime = LocalDateTime.now().minusDays(1); //仮置き 昨日受け取ったことにする
        }
        LocalDate lastClaimedDate = null;
        if(lastClaimedDateTime.getHour() >= dailyResetHour){
            lastClaimedDate = lastClaimedDateTime.toLocalDate();
        }else{
            lastClaimedDate = lastClaimedDateTime.toLocalDate().minusDays(1);
        }

//        LocalDateTime lastLoginDateTime = loginBonusData.getLastLoginDateTime(playerUUID);
//        LocalDate lastLoginDate = null;
//        if(lastLoginDateTime.getHour() >= dailyResetHour){
//            lastLoginDate = lastLoginDateTime.toLocalDate();
//        }else{
//            lastLoginDate = lastLoginDateTime.toLocalDate().minusDays(1);
//        }

        // gui開くメッセージを送信する -> ついでに日付が変わっているならdbのlogin_countを1増やす -> 最終ログイン日時更新（旧）
//        if(lastClaimedDateTime == null) {
//            loginBonusData.setAllData(playerUUID, 0, 0, currentDate);
//            sendOpenGuiMessage(player);
//        }else if(lastClaimedDate.isAfter(baseDate)){
//            sendOpenGuiMessage(player);
//            if(lastLoginDate.isAfter(baseDate)){
//                int loginCount = loginBonusData.getLoginCount(playerUUID);
//                loginCount++;
//                player.sendMessage("ログイン" + loginCount + "日目！");
//                loginBonusData.setLoginCount(playerUUID, loginCount);
//                loginBonusData.setLastLoginDateTime(playerUUID, currentDate);
//            }
//        }else{
//            player.sendMessage("あなたは既にログインボーナスをすべて受け取っています。");
//        }

        if (lastClaimedDate.isBefore(baseDate)) {
            sendOpenGuiMessage(player);
            int loginCount = loginBonusData.getLoginCount(playerUUID, dailyResetHour, RewardManager.getPeriodStartDate(currentBonusName), RewardManager.getPeriodEndDate(currentBonusName));
            player.sendMessage("ログイン" + loginCount + "日目！");
        } else {
            player.sendMessage("受取可能なログインボーナスはありません。");
            player.sendMessage("lastClaimedDate:" + lastClaimedDate);
            player.sendMessage("baseDate:" + baseDate);
        }
        player.sendMessage("連続ログイン" + loginBonusData.getLoginStreak(playerUUID, dailyResetHour) + "日目！");
    }

    public void sendOpenGuiMessage(Player player){
        TextComponent message = new TextComponent("ログボ受取");
        message.setColor(ChatColor.YELLOW);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/loginbonus"));
        player.spigot().sendMessage(message);
    }
}