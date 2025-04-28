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
    String messagePrefix;

    public EventListener(LoginBonusPlugin plugin, LoginBonusData loginBonusData) {
        messagePrefix = "§6§l[LoginBonusPlugin] §r";

        this.plugin = plugin;
        this.loginBonusData = loginBonusData;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Thread th = new Thread(() -> {
            if(plugin.getConfig().getString("status").equals("on")){
                Player player = event.getPlayer();
                UUID playerUUID = player.getUniqueId();

                String currentBonusName = RewardManager.getCurrentBonusName();
                if (currentBonusName == null) {
                    Bukkit.getScheduler().runTask(plugin, new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            player.sendMessage(messagePrefix + "現在開催中のログインボーナスはありません");
                        }
                    });
                    return;
                }
                boolean hasClaimedTodayAccumulatedReward = !hasClaimedToday(playerUUID, "accumulated", currentBonusName);
                boolean hasClaimedTodayContinuousReward = !hasClaimedToday(playerUUID, "continuous", currentBonusName);
                int loginCount = loginBonusData.getLoginCount(playerUUID, currentBonusName);
                int loginStreak = loginBonusData.getLoginStreak(playerUUID, currentBonusName);

                Bukkit.getScheduler().runTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        if (hasClaimedTodayAccumulatedReward) {
                            player.sendMessage(messagePrefix + "ログイン" + loginCount + "日目！");
                            sendOpenAccumulatedRewardGuiMessage(player);
                        } else {
                            player.sendMessage(messagePrefix + "受取可能なログインボーナスはありません。");
                        }

                        // 空白
                        player.sendMessage(" ");

                        if (hasClaimedTodayContinuousReward && loginStreak == 10) {
                            player.sendMessage(messagePrefix + "連続ログイン" + loginStreak + "日目！以下をクリックで連続ログインボーナス報酬を受け取れます");
                            sendOpenContinuousRewardGuiMessage(player);
                        } else {
                            player.sendMessage(messagePrefix + "連続ログイン" + loginStreak + "日目！");
                        }
                    }
                });
            }
        });
        th.start();
    }

    public void sendOpenAccumulatedRewardGuiMessage(Player player){
        TextComponent message = new TextComponent("[累積ログインボーナスを受け取る]");
        message.setColor(ChatColor.YELLOW);
        message.setBold(true);
        message.setUnderlined(true);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/loginbonus total"));
        player.spigot().sendMessage(message);
    }

    public void sendOpenContinuousRewardGuiMessage(Player player){
        TextComponent message = new TextComponent("[連続ログインボーナスを受け取る]");
        message.setColor(ChatColor.YELLOW);
        message.setBold(true);
        message.setUnderlined(true);
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