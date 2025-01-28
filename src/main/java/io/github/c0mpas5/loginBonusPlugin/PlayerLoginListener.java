package io.github.c0mpas5.loginBonusPlugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class PlayerLoginListener implements Listener {

    private final LoginBonusPlugin plugin;
    private final LoginBonusData loginBonusData;

    public PlayerLoginListener(LoginBonusPlugin plugin, LoginBonusData loginBonusData) {
        this.plugin = plugin;
        this.loginBonusData = loginBonusData;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        String serverName = Bukkit.getServer().getName();
        LocalDateTime dailyResetTime = null;
        int dailyResetHour = 5;

        // 最終ログイン日を取得
        LocalDateTime lastLoginDate = loginBonusData.getLastLoginDate(playerUUID);
        if(lastLoginDate == null){
            // 初回ログイン時の処理入れる
        }

        // 現在時刻を取得
        LocalDateTime currentDate = LocalDateTime.now();

        // デイリーリセット時刻を取得
        if(currentDate.getHour() >= dailyResetHour){
            dailyResetTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(5, 0));
        }else{
            dailyResetTime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(5, 0));
        }

        // 最終ログイン日が今日ではない場合にのみ loginCount を加算（lastLoginDateとcurrentDateがdailyResetTimeを挟んでいるor）
        if ((lastLoginDate.isBefore(dailyResetTime) && dailyResetTime.isBefore(currentDate))) { //|| lastLoginDate.plusDays(1).isBefore(currentDate)
            int loginCount = loginBonusData.getLoginCount(playerUUID);
            loginCount++;
            loginBonusData.setLoginCount(playerUUID, loginCount);
            player.sendMessage("ログインしているサーバー名は " + serverName + " です。");

            // ログインボーナスの報酬を与えるロジック
            player.sendMessage("ログインボーナスを受け取りました！連続ログイン日数: " + loginCount);
            player.sendMessage("時間は" + lastLoginDate.toString());
            // 例: アイテムを与える
            // player.getInventory().addItem(new ItemStack(Material.DIAMOND, loginCount));
        }
    }
}