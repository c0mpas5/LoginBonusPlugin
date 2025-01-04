// LoginBonusData.java
package io.github.c0mpas5.loginBonusPlugin;

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

    public LoginBonusData(LoginBonusPlugin plugin, MySQLManager mysqlManager) {
        this.plugin = plugin;
        this.mysqlManager = mysqlManager;
        createTableIfNotExists();
    }

    public void setLoginStreak(UUID playerUUID, int streak) {
        mysqlManager.execute("INSERT INTO loginbonus_info (uuid, streak) VALUES ('" + playerUUID.toString() + "', " + streak + ") ON DUPLICATE KEY UPDATE streak = " + streak, 0);
    }

    public int getLoginStreak(UUID playerUUID) {
        try (ResultSet rs = mysqlManager.query("SELECT streak FROM loginbonus_info WHERE uuid = '" + playerUUID.toString() + "'", 0)) {
            if (rs.next()) {
                return rs.getInt("streak");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public void setLastLoginDate(UUID playerUUID, LocalDateTime date) {
        mysqlManager.execute("INSERT INTO connection_data (uuid, dt) VALUES ('" + playerUUID.toString() + "', '" + date + "') ON DUPLICATE KEY UPDATE dt = '" + date + "'", 1);
    }

    public LocalDateTime getLastLoginDate(UUID playerUUID) {
        try (ResultSet rs = mysqlManager.query("SELECT dt FROM connection_data WHERE uuid = '" + playerUUID.toString() + "'", 1)) {
            if (rs.next()) {
                return rs.getTimestamp("dt").toLocalDateTime();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private void createTableIfNotExists() {
        String query = "CREATE TABLE IF NOT EXISTS loginbonus_info (uuid VARCHAR(36) PRIMARY KEY, streak INT)";
        mysqlManager.execute(query, 0);
        query = "CREATE TABLE IF NOT EXISTS connection_data (uuid VARCHAR(36) PRIMARY KEY, dt DATETIME)";
        mysqlManager.execute(query, 1);
    }
}