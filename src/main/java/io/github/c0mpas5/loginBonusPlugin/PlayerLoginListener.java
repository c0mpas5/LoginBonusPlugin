package io.github.c0mpas5.loginBonusPlugin;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.LocalDate;
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

        // 最終ログイン日を取得
        //LocalDate lastLoginDate = loginBonusData.getLastLoginDate(playerUUID);

        // 最終ログイン日が今日ではない場合にのみ streak を加算
        if (true) {
            int streak = loginBonusData.getLoginStreak(playerUUID);
            streak++;
            loginBonusData.setLoginStreak(playerUUID, streak);
            //loginBonusData.setLastLoginDate(playerUUID, today);

            // ログインボーナスの報酬を与えるロジック
            player.sendMessage("ログインボーナスを受け取りました！連続ログイン日数: " + streak);
            // 例: アイテムを与える
            // player.getInventory().addItem(new ItemStack(Material.DIAMOND, streak));
        }
    }
}