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

        // dbからデータ取ってきてチャットに色々表示させる処理（スレッド）
        Thread th = new Thread(() -> {
            boolean hasClaimedTodayAccumulatedReward = !hasClaimedToday(playerUUID, "accumulated", currentBonusName);
            boolean hasClaimedTodayContinuousReward = !hasClaimedToday(playerUUID, "continuous", currentBonusName);
            int loginCount = loginBonusData.getLoginCount(playerUUID, currentBonusName);
            int loginStreak = loginBonusData.getLoginStreak(playerUUID, currentBonusName);
            Bukkit.getScheduler().runTask(plugin, new Runnable() {
                @Override
                public void run() {
                    if(hasClaimedTodayAccumulatedReward){
                        sendOpenAccumulatedRewardGuiMessage(player);
                        player.sendMessage("ログイン" + loginCount + "日目！");
                    } else {
                        player.sendMessage("受取可能なログインボーナスはありません。");
                    }

                    if(hasClaimedTodayContinuousReward && loginStreak == 10) {
                        sendOpenContinuousRewardGuiMessage(player);
                        player.sendMessage("連続ログイン" + loginStreak + "日目！以下をクリックで連続ログインボーナス報酬を受け取れます");
                    } else {
                        player.sendMessage("連続ログイン" + loginStreak + "日目！");
                    }
                }
            });
        });
        th.start();
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