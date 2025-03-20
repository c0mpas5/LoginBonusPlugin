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


//TODO:期間設定とか、現在設定されている値を表示したい場合は更新したいので↓のようにpane削除とかする必要があるっぽい
//TODO:ログボの作成を完了するときに設定がすべて完了していなければ弾く処理
//NOTICE:インベントリ操作中に開催中のログインボーナス、つまりcurrentLoginBonusNameが変わった時に、アイテムを不正に得られたりしないように対策必要
//  →至る所（主に推移先）で受け取ったcurrentLoginBonusNameと実行直後に得たcurrentLoginBonusNameが一致するか確かめて、一致しないならcloseInv
//NOTICE:報酬プールにアイテムが入っていない時、報酬用のplayerHeadが「プレイヤーの頭」という名前になる？報酬プールを空に出来ないようにするなら対策不要そう→一時保存できるようにして、本格的な設定は全部の設定埋める必要がある形にする