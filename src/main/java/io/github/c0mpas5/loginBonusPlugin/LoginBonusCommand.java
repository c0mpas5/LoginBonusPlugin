// LoginBonusCommand.java
package io.github.c0mpas5.loginBonusPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoginBonusCommand implements CommandExecutor {

    private final LoginBonusPlugin plugin;
    private final LoginBonusData loginBonusData;

    public LoginBonusCommand(LoginBonusPlugin plugin, LoginBonusData loginBonusData) {
        this.plugin = plugin;
        this.loginBonusData = loginBonusData;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            switch (command.getName().toLowerCase()) {
                case "loginbonus":
                    // loginbonus コマンドの処理
                    break;
                case "test":
                    int loginCount = loginBonusData.getLoginCount(player.getUniqueId());
                    player.sendMessage("あなたの連続ログイン日数は " + loginCount + " 日です。");
                    player.sendMessage("あなたのUUIDは " + player.getUniqueId().toString() + " です。");
                    break;
                case "admingui":
                    if(sender != player){
                        sender.sendMessage("プレイヤーでないと実行できません");
                        return false;
                    }
                    // GUIを開く処理(senderわたす)
                    break;
            }
        }
        return true;
    }
}