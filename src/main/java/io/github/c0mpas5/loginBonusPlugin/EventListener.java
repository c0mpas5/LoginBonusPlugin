package io.github.c0mpas5.loginBonusPlugin;

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
        LocalDate baseDate = null; // X日5時~X+1日4時59分59秒までをX日とするための変数
        int dailyResetHour = 5;

        // 現在時刻を取得
        LocalDateTime currentDate = LocalDateTime.now();

        // X日5時~X+1日4時59分59秒までをX日とするための処理
        if(currentDate.getHour() >= dailyResetHour){
            baseDate = LocalDate.now();
        }else{
            baseDate = LocalDate.now().minusDays(1);
        }

        // 最終ログイン日を取得
        LocalDate lastLoginDate = loginBonusData.getLastLoginDate(playerUUID);
        // ログイン履歴が無い場合、DBに新しく書き込んでチャットにメッセージを送信
        if(lastLoginDate == null){
            loginBonusData.setAllData(playerUUID, 1, 1, 0, baseDate);
            // ログボGUIを開くためのメッセージ送信処理
            player.sendMessage("[ログボを受け取る]");
            return;
        }

        // 最終ログイン日がbaseDateより前の場合、各カウントを増加
        if(lastLoginDate.isAfter(baseDate)){
            int loginCount = loginBonusData.getLoginCount(playerUUID);
            loginCount++;
            loginBonusData.setLoginCount(playerUUID, loginCount);
            player.sendMessage("合計ログイン" + loginCount + "日目！");
            // 最終ログイン日がbaseDateの1日前の時（連続している時）、loginStreakを増加。していない時は1にリセット
            if(lastLoginDate.compareTo(baseDate) == -1){
                int loginStreak = loginBonusData.getLoginStreak(playerUUID);
                loginStreak++;
                loginBonusData.setLoginStreak(playerUUID, loginStreak);
                player.sendMessage("連続ログイン" + loginCount + "日目！");
            }else{
                loginBonusData.setLoginStreak(playerUUID, 1);
                player.sendMessage("連続ログイン日数がリセットされました。ログイン" + loginCount + "日目");
            }

            // 最終ログイン日時をbaseDateに更新。
            loginBonusData.setLastLoginDate(playerUUID, baseDate);


        }

        if(loginBonusData.getClaimedCount(playerUUID) < loginBonusData.getLoginCount(playerUUID)){
            player.sendMessage("[ログボを受け取る]");
            // ログボGUIを開くためのメッセージ送信処理
        }else{
            player.sendMessage("あなたは既にログインボーナスをすべて受け取っています。");
        }



//        // 最終ログイン日が今日ではない場合にのみ loginCount を加算（lastLoginDateとcurrentDateがdailyResetTimeを挟んでいるor）
//        if ((lastLoginDate.isBefore(dailyResetTime) && dailyResetTime.isBefore(currentDate))) { //|| lastLoginDate.plusDays(1).isBefore(currentDate)
//            int loginCount = loginBonusData.getLoginCount(playerUUID);
//            loginCount++;
//            loginBonusData.setLoginCount(playerUUID, loginCount);
//            player.sendMessage("ログインしているサーバー名は " + serverName + " です。");
//
//            // ログインボーナスの報酬を与えるロジック
//            player.sendMessage("ログインボーナスを受け取りました！連続ログイン日数: " + loginCount);
//            player.sendMessage("時間は" + lastLoginDate.toString());
//            // 例: アイテムを与える
//            // player.getInventory().addItem(new ItemStack(Material.DIAMOND, loginCount));
//        }
    }
}