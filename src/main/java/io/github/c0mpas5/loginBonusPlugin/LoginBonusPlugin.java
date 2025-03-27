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
        getCommand("loginbonus").setTabCompleter(new LoginBonusTabCompleter());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("プラグインが無効化されました");
    }
}

//TODO: finally使ってないとこ確認
//TODO: thread処理
//TODO: 10日目の時にバグる？受け取れない+名前がプレイヤーの頭→発生条件が分からん。普通に使ってたら起こらないかも
//TODO: コマンド発行？

//NOTICE: サブ垢 ：実装済み要確認→確認しづらいのでhelperの誰かに確認してもらう
//NOTICE:よくわからんnbtタグがつくのを治す→https://discord.com/channels/277712676371562496/757026124651036672/1352695936899612692→付与には付かなくなってたけどListにいれると付いちゃう