// LoginBonusPlugin.java
package io.github.c0mpas5.loginBonusPlugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class LoginBonusPlugin extends JavaPlugin {

    private LoginBonusData loginBonusData;
    private MySQLManager mysqlManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getLogger().info("プラグインが有効化されました");

        createConfig();

        // MySQLManagerの初期化
        mysqlManager = new MySQLManager(this, "LoginBonus");

        // データ管理クラスの初期化
        loginBonusData = new LoginBonusData(this, mysqlManager);
        // イベントリスナーの登録
        getServer().getPluginManager().registerEvents(new EventListener(this, loginBonusData), this);

        // コマンドの登録
        LoginBonusCommand loginBonusCommand = new LoginBonusCommand(this, loginBonusData);
        getCommand("loginbonus").setExecutor(loginBonusCommand);
        getCommand("loginbonus").setTabCompleter(new LoginBonusTabCompleter());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.getLogger().info("プラグインが無効化されました");
    }

    public void createConfig() {
        // config.ymlの作成
        this.saveDefaultConfig();
        this.getLogger().info("現在は" + this.getConfig().getString("target_server_name") + "が対象のサーバーとして設定されています");
    }


}



//TODO: コマンド発行？

//NOTICE: サブ垢 ：実装済み要確認→確認しづらいのでhelperの誰かに確認してもらう