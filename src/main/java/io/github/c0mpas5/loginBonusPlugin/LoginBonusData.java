// LoginBonusData.java
package io.github.c0mpas5.loginBonusPlugin;

import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LoginBonusData {

    private final LoginBonusPlugin plugin;
    private final MySQLManager mysqlManager;
    private Connection con = null;
    private MySQLFunc MySQL;

    public LoginBonusData(LoginBonusPlugin plugin, MySQLManager mysqlManager) {
        this.plugin = plugin;
        this.mysqlManager = mysqlManager;
        createTableIfNotExists();
    }

    public void setLoginStreak(UUID playerUUID, int streak) {
//        mysqlManager.execute("INSERT INTO loginbonus_info (uuid, streak) VALUES ('" + playerUUID.toString() + "', " + streak + ") ON DUPLICATE KEY UPDATE streak = " + streak, 0);
        this.MySQL = new MySQLFunc(mysqlManager.HOST0, mysqlManager.DB0, mysqlManager.USER0, mysqlManager.PASS0, mysqlManager.PORT0);
        this.con = this.MySQL.open();
        if(this.con == null){
            Bukkit.getLogger().info("failed to open MYSQL");
            return;
        }
        try {
            PreparedStatement ps = this.con.prepareStatement("INSERT INTO loginbonus_info (uuid, streak) VALUES (?, ?) ON DUPLICATE KEY UPDATE streak = ?");
            ps.setString(1, playerUUID.toString());
            ps.setInt(2, streak);
            ps.setInt(3, streak);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.mysqlManager.close();
    }

    public int getLoginStreak(UUID playerUUID) {
//        try (ResultSet rs = mysqlManager.query("SELECT streak FROM loginbonus_info WHERE uuid = '" + playerUUID.toString() + "'", 0)) {
//            if (rs.next()) {
//                return rs.getInt("streak");
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        this.MySQL = new MySQLFunc(mysqlManager.HOST0, mysqlManager.DB0, mysqlManager.USER0, mysqlManager.PASS0, mysqlManager.PORT0);
        this.con = this.MySQL.open();
        if(this.con == null){
            Bukkit.getLogger().info("failed to open MYSQL");
            return 0;
        }
        try {
            PreparedStatement ps = this.con.prepareStatement("SELECT streak FROM loginbonus_info WHERE uuid = ?");
            ps.setString(1, playerUUID.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("streak");
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.mysqlManager.close();
        return 0;
    }

    //開発用においてるだけ
    public void setLastLoginDate(UUID playerUUID, LocalDateTime date, String serverName) {
//        mysqlManager.execute("INSERT INTO connection_log (uuid, connected_time, server) VALUES ('" + playerUUID.toString() + "', '" + date + "', '" + serverName + "') ON DUPLICATE KEY UPDATE connected_time = '" + date + "'", 1);
        this.MySQL = new MySQLFunc(mysqlManager.HOST1, mysqlManager.DB1, mysqlManager.USER1, mysqlManager.PASS1, mysqlManager.PORT1);
        this.con = this.MySQL.open();
        if(this.con == null){
            Bukkit.getLogger().info("failed to open MYSQL");
            return;
        }
        try {
            PreparedStatement ps = this.con.prepareStatement("INSERT INTO connection_log (uuid, connected_time, server) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE connected_time = ?");
            ps.setString(1, playerUUID.toString());
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(date));
            ps.setString(3, serverName);
            ps.setTimestamp(4, java.sql.Timestamp.valueOf(date));
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.mysqlManager.close();
    }

    public LocalDateTime getLastLoginDate(UUID playerUUID) {
//        try (ResultSet rs = mysqlManager.query("SELECT connected_time FROM connection_log WHERE uuid = '" + playerUUID.toString() + "'", 1)) {
//            if (rs.next()) {
//                return rs.getTimestamp("connected_time").toLocalDateTime();
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        this.MySQL = new MySQLFunc(mysqlManager.HOST1, mysqlManager.DB1, mysqlManager.USER1, mysqlManager.PASS1, mysqlManager.PORT1);
        this.con = this.MySQL.open();
        if(this.con == null){
            Bukkit.getLogger().info("failed to open MYSQL");
            return null;
        }
        try {
            PreparedStatement ps = this.con.prepareStatement("SELECT connected_time FROM connection_log WHERE uuid = ?");
            ps.setString(1, playerUUID.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getTimestamp("connected_time").toLocalDateTime();
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.mysqlManager.close();
        return null;
    }

    private void createTableIfNotExists() {
        String query = "CREATE TABLE IF NOT EXISTS loginbonus_info (uuid VARCHAR(36) PRIMARY KEY, streak INT)";
        mysqlManager.execute(query, 0);
        query = "CREATE TABLE IF NOT EXISTS connection_log (uuid VARCHAR(36) PRIMARY KEY, connected_time DATETIME, server VARCHAR(16))";
        mysqlManager.execute(query, 1);

        //デバッグ：DBに適当なログイン日時を入力
        setLastLoginDate(UUID.fromString("7f05cd38-a2fa-44fc-8c9e-825e86c86efe"), LocalDateTime.of(2025,1,7,10,0), "Paper");
    }
}