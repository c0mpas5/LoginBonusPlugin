package io.github.c0mpas5.loginBonusPlugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

/**
 * Created by takatronix on 2017/03/05.
 */


public class MySQLManager {

    public  Boolean debugMode = false;
    private JavaPlugin plugin;
    String HOST0 = null;
    String DB0 = null;
    String USER0 = null;
    String PASS0 = null;
    String PORT0 = null;

    String HOST1 = null;
    String DB1 = null;
    String USER1 = null;
    String PASS1 = null;
    String PORT1 = null;

    private boolean connected = false;
    private Statement st = null;
    private Connection con = null;
    private String conName;
    private MySQLFunc MySQL;

    private String serverName;

    ////////////////////////////////
    //      コンストラクタ
    ////////////////////////////////
    public MySQLManager(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.conName = name;
        this.connected = false;
        loadConfig();

        if(Connect(0) && Connect(1)){
            this.connected = true;
        }
        //this.connected = Connect(); //Connect(HOST, DB, USER, PASS, PORT)

        if (!this.connected) {
            plugin.getLogger().info("Unable to establish a MySQL connection.");
        }
    }

    /////////////////////////////////
    //       設定ファイル読み込み
    /////////////////////////////////
    public void loadConfig(){
        plugin.getLogger().info("MYSQL Config loading");
        plugin.reloadConfig();

        HOST0 = plugin.getConfig().getString("mysql_loginbonus_info.host");
        USER0 = plugin.getConfig().getString("mysql_loginbonus_info.user");
        PASS0 = plugin.getConfig().getString("mysql_loginbonus_info.pass");
        PORT0 = plugin.getConfig().getString("mysql_loginbonus_info.port");
        DB0 = plugin.getConfig().getString("mysql_loginbonus_info.db");

        HOST1 = plugin.getConfig().getString("mysql_connection_data.host");
        USER1 = plugin.getConfig().getString("mysql_connection_data.user");
        PASS1 = plugin.getConfig().getString("mysql_connection_data.pass");
        PORT1 = plugin.getConfig().getString("mysql_connection_data.port");
        DB1 = plugin.getConfig().getString("mysql_connection_data.db");

        plugin.getLogger().info("Config loaded");
    }

    public void commit(){
        try{
            this.con.commit();
        }catch (Exception e){

        }
    }

    ////////////////////////////////
    //       接続
    ////////////////////////////////
    public Boolean Connect(int dbNum) { //Connect(String host, String db, String user, String pass,String port)
        //this.HOST = host;
        //this.DB = db;
        //this.USER = user;
        //this.PASS = pass;
        if(dbNum == 0){
            this.MySQL = new MySQLFunc(HOST0, DB0, USER0, PASS0, PORT0);
        }else if(dbNum == 1){
            this.MySQL = new MySQLFunc(HOST1, DB1, USER1, PASS1, PORT1);
        }else{
            plugin.getLogger().info("データベース番号が不正です");
        }

        this.con = this.MySQL.open();
        if(this.con == null){
            plugin.getLogger().info("failed to open MYSQL");
            return false;
        }

        try {
            this.st = this.con.createStatement();
            this.connected = true;
            this.plugin.getLogger().info("[" + this.conName + "] Connected to the database.");
        } catch (SQLException var6) {
            this.connected = false;
            this.plugin.getLogger().info("[" + this.conName + "] Could not connect to the database.");
        }

        this.MySQL.close(this.con);
        return Boolean.valueOf(this.connected);
    }

    ////////////////////////////////
    //     行数を数える
    ////////////////////////////////
    public int countRows(String table) {
        int count = 0;
        ResultSet set = this.query(String.format("SELECT * FROM %s", new Object[]{table}), 0);

        try {
            while(set.next()) {
                ++count;
            }
        } catch (SQLException var5) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not select all rows from table: " + table + ", error: " + var5.getErrorCode());
        }

        return count;
    }
    ////////////////////////////////
    //     レコード数
    ////////////////////////////////
    public int count(String table) {
        int count = 0;
        ResultSet set = this.query(String.format("SELECT count(*) from %s", table), 0);

        try {
            count = set.getInt("count(*)");

        } catch (SQLException var5) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not select all rows from table: " + table + ", error: " + var5.getErrorCode());
            return -1;
        }

        return count;
    }
    ////////////////////////////////
    //      実行
    ////////////////////////////////
    public boolean execute(String query, int dbNum) {
        if(dbNum == 0){
            this.MySQL = new MySQLFunc(HOST0, DB0, USER0, PASS0, PORT0);
        }else if(dbNum == 1){
            this.MySQL = new MySQLFunc(HOST1, DB1, USER1, PASS1, PORT1);
        }else{
            plugin.getLogger().info("データベース番号が不正です");
        }

        this.con = this.MySQL.open();
        if(this.con == null){
            plugin.getLogger().info("failed to open MYSQL");
            return false;
        }
        boolean ret = true;
        if (debugMode){
            plugin.getLogger().info("query:" + query);
        }

        try {
            this.st = this.con.createStatement();
            this.st.execute(query);
        } catch (SQLException var3) {
            this.plugin.getLogger().info("[" + this.conName + "] Error executing statement: " +var3.getErrorCode() +":"+ var3.getLocalizedMessage());
            this.plugin.getLogger().info(query);
            ret = false;

        }

        this.close();
        return ret;
    }

    ////////////////////////////////
    //      クエリ
    ////////////////////////////////
    public ResultSet query(String query, int dbNum) {
        if(dbNum == 0){
            this.MySQL = new MySQLFunc(HOST0, DB0, USER0, PASS0, PORT0);
        }else if(dbNum == 1){
            this.MySQL = new MySQLFunc(HOST1, DB1, USER1, PASS1, PORT1);
        }else{
            plugin.getLogger().info("データベース番号が不正です");
        }

        this.con = this.MySQL.open();
        ResultSet rs = null;
        if(this.con == null){
            plugin.getLogger().info("failed to open MYSQL");
            return rs;
        }

        if (debugMode){
            plugin.getLogger().info("[DEBUG] query:" + query);
        }

        try {
            this.st = this.con.createStatement();
            rs = this.st.executeQuery(query);
        } catch (SQLException var4) {
            this.plugin.getLogger().info("[" + this.conName + "] Error executing query: " + var4.getErrorCode());
            this.plugin.getLogger().info(query);
        }

//        this.close();

        return rs;
    }


    public void close(){

        try {
            this.st.close();
            this.con.close();
            this.MySQL.close(this.con);

        } catch (SQLException var4) {
        }

    }
}