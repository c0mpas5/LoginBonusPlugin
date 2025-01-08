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
        LocalDate today = LocalDate.now();
        LocalTime resetTime = LocalTime.of(5, 0);
        String serverName = Bukkit.getServer().getName();

        // 最終ログイン日を取得
        //LocalDate lastLoginDate = loginBonusData.getLastLoginDate(playerUUID);

        // 最終ログイン日が今日ではない場合にのみ streak を加算
        if (true) {
            int streak = loginBonusData.getLoginStreak(playerUUID);
            LocalDateTime lastLoginDate = loginBonusData.getLastLoginDate(playerUUID);
            streak++;
            loginBonusData.setLoginStreak(playerUUID, streak);
            player.sendMessage("ログインしているサーバー名は " + serverName + " です。");

            // ログインボーナスの報酬を与えるロジック
            player.sendMessage("ログインボーナスを受け取りました！連続ログイン日数: " + streak);
            player.sendMessage("時間は" + lastLoginDate.toString());
            // 例: アイテムを与える
            // player.getInventory().addItem(new ItemStack(Material.DIAMOND, streak));
        }
    }
}