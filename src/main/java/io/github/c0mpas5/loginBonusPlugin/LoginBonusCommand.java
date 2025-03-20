// LoginBonusCommand.java
package io.github.c0mpas5.loginBonusPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

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

            if (command.getName().equalsIgnoreCase("loginbonus")) {
                // 現在のボーナス名がnullの場合はエラーメッセージを表示
                if (RewardManager.getCurrentBonusName() == null) {
                    sender.sendMessage("現在開催中のログインボーナスはありません");
                    return false;
                }

                // loginBonusDataがnullの場合はエラーメッセージを表示
                if (loginBonusData == null) {
                    sender.sendMessage("loginBonusDataがnull");
                    return false;
                }

                LoginBonusUserGUI userGui = new LoginBonusUserGUI(loginBonusData, player.getUniqueId());

                // サブコマンドの処理
                if (args.length > 0) {
                    switch (args[0].toLowerCase()) {
                        case "total":
                            // 累計ログイン日数を表示
                            userGui.updateUserAccumulatedLoginBonusClaimGui();
                            userGui.getUserAccumulatedLoginBonusClaimGui().show(player);
                            return true;
                        case "streak":
                            // 連続ログイン日数を表示
                            userGui.updateUserContinuousLoginBonusClaimGui();
                            userGui.getUserContinuousLoginBonusClaimGui().show(player);
                            return true;
                        default:
                            // 引数がわからない場合はGUIを表示
                            player.sendMessage("§cコマンドが誤っています");
                    }
                } else {
                    // 引数がない場合はデフォルトでGUIを表示
                    userGui.updateUserAccumulatedLoginBonusClaimGui();
                    userGui.getUserAccumulatedLoginBonusClaimGui().show(player);
                    return true;
                }
            } else if (command.getName().equalsIgnoreCase("test")) {
                // テスト用コマンドの処理
                return true;
            } else if (command.getName().equalsIgnoreCase("admingui")) {
                // 管理者GUI表示
                LoginBonusAdminGUI adminGui = new LoginBonusAdminGUI();
                adminGui.getAdminHomeGui().show(player);
                return true;
            }
        }
        return false;
    }
}