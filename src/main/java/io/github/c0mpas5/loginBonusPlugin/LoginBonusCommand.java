// LoginBonusCommand.java
package io.github.c0mpas5.loginBonusPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoginBonusCommand implements CommandExecutor {

    private final LoginBonusPlugin plugin;
    private final LoginBonusData loginBonusData;
    String messagePrefix;

    public LoginBonusCommand(LoginBonusPlugin plugin, LoginBonusData loginBonusData) {
        messagePrefix = "§6§l[LoginBonusPlugin] §r";

        this.plugin = plugin;
        this.loginBonusData = loginBonusData;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (command.getName().equalsIgnoreCase("loginbonus")) {
                // loginBonusDataがnullの場合はエラーメッセージを表示
                if (loginBonusData == null) {
                    return false;
                }

                // サブコマンドの処理
                if (args.length > 0) {
                    LoginBonusUserGUI userGui;
                    switch (args[0].toLowerCase()) {
                        case "total":
                            if (RewardManager.getCurrentBonusName() == null) {
                                sender.sendMessage(messagePrefix + "現在開催中のログインボーナスはありません");
                                return false;
                            }
                            // 累計ログイン日数を表示
                            userGui = new LoginBonusUserGUI(loginBonusData, player.getUniqueId(), plugin);
                            userGui.updateUserAccumulatedLoginBonusClaimGui();
                            userGui.getUserAccumulatedLoginBonusClaimGui().show(player);
                            return true;
                        case "streak":
                            if (RewardManager.getCurrentBonusName() == null) {
                                sender.sendMessage(messagePrefix + "現在開催中のログインボーナスはありません");
                                return false;
                            }
                            // 連続ログイン日数を表示
                            userGui = new LoginBonusUserGUI(loginBonusData, player.getUniqueId(), plugin);
                            userGui.updateUserContinuousLoginBonusClaimGui();
                            userGui.getUserContinuousLoginBonusClaimGui().show(player);
                            return true;
                        case "admin":
                            if(sender.hasPermission("loginbonusplugin.op")){
                                LoginBonusAdminGUI adminGui = new LoginBonusAdminGUI();
                                adminGui.getAdminHomeGui().show(player);
                                return true;
                            }
                        case "help":
                            // ヘルプを表示
                            player.sendMessage("§f【ログインボーナスプラグインコマンド一覧】───────");
                            player.sendMessage("§b/loginbonus[lb] §r§7- 累計ログインボーナスGUIを表示");
                            player.sendMessage("§b/loginbonus[lb] total §r§7- 累計ログインボーナスGUIを表示");
                            player.sendMessage("§b/loginbonus[lb] streak §r§7- 連続ログインボーナスGUIを表示");
                            if (sender.hasPermission("loginbonusplugin.op")) {
                                player.sendMessage("§b/loginbonus[lb] admin §r§7- 管理者用GUIを表示。ログボを作成・編集できます");
                            }
                            player.sendMessage("§b/loginbonus[lb] help §r§7- ヘルプを表示");
                            player.sendMessage("§f（[]：括弧内の書き方に代替可能）");
                            return true;
//                        case "test":
//                            if(sender.hasPermission("loginbonusplugin.op")){
//                                LoginBonusAdminGUI adminGui = new LoginBonusAdminGUI();
//                                adminGui.getAdminTestGui().show(player);
//                                return true;
//                            }
                        default:
                            // 引数がわからない場合はGUIを表示
                            player.sendMessage(messagePrefix + "§cコマンドが誤っています");
                    }
                } else {
                    if (RewardManager.getCurrentBonusName() == null) {
                        sender.sendMessage(messagePrefix + "現在開催中のログインボーナスはありません");
                        return false;
                    }
                    // 引数がない場合はデフォルトでGUIを表示
                    LoginBonusUserGUI userGui = new LoginBonusUserGUI(loginBonusData, player.getUniqueId(), plugin);
                    userGui.updateUserAccumulatedLoginBonusClaimGui();
                    userGui.getUserAccumulatedLoginBonusClaimGui().show(player);
                    return true;
                }
            }
        }
        return false;
    }
}