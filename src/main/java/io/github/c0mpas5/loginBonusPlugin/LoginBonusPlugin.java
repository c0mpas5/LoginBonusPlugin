// LoginBonusPlugin.java
package io.github.c0mpas5.loginBonusPlugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class LoginBonusPlugin extends JavaPlugin {

    private LoginBonusData loginBonusData;
    private MySQLManager mysqlManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("プラグインが有効化されました");

        // MySQLManagerの初期化
        mysqlManager = new MySQLManager(this, "LoginBonus");

        // データ管理クラスの初期化
        loginBonusData = new LoginBonusData(this, mysqlManager);

        // イベントリスナーの登録
        getServer().getPluginManager().registerEvents(new EventListener(this, loginBonusData), this);

        // コマンドの登録
        LoginBonusCommand loginBonusCommand = new LoginBonusCommand(this, loginBonusData);
        getCommand("loginbonus").setExecutor(loginBonusCommand);
        getCommand("test").setExecutor(loginBonusCommand);
        getCommand("admingui").setExecutor(loginBonusCommand);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("プラグインが無効化されました");
    }

    public LoginBonusData getLoginBonusData() {
        return loginBonusData;
    }
}