// LoginBonusData.java
package io.github.c0mpas5.loginBonusPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
        mysqlManager.execute("INSERT INTO loginbonus_info (uuid, streak) VALUES ('" + playerUUID.toString() + "', " + streak + ") ON DUPLICATE KEY UPDATE streak = " + streak);
    }

    public int getLoginStreak(UUID playerUUID) {
        try (ResultSet rs = mysqlManager.query("SELECT streak FROM loginbonus_info WHERE uuid = '" + playerUUID.toString() + "'");) {
            if (rs.next()) {
                return rs.getInt("streak");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    private void createTableIfNotExists() {
        String query = "CREATE TABLE IF NOT EXISTS loginbonus_info (uuid VARCHAR(36) PRIMARY KEY, streak INT)";
        mysqlManager.execute(query);
    }
}