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
}

//TODO: finally使ってないとこ確認
//TODO: 音付ける
//TODO: コマンド予測変換
//TODO: ログボ変更時、booleanをfalseにしてそれ以降の処理が走らないようにする必要あり
//TODO: thread処理
//NOTICE:よくわからんnbtタグがつくのを治す→https://discord.com/channels/277712676371562496/757026124651036672/1352695936899612692→付与には付かなくなってたけどListにいれると付いちゃう