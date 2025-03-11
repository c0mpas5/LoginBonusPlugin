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

            switch (command.getName().toLowerCase()) {
                case "test":
                    // 空
                    break;
                case "admingui":
                    LoginBonusAdminGUI adminGui = new LoginBonusAdminGUI();
                    adminGui.getAdminHomeGui().show(player);
                    break;
                case "loginbonus":
                    if(RewardManager.getCurrentBonusName() == null){
                        sender.sendMessage("現在開催中のログインボーナスはありません");
                        return false;
                    }
                    // TODO:テスト。削除必要
                    if(loginBonusData == null){
                        sender.sendMessage("loginBonusDataがnull");
                        return false;
                    }
                    LoginBonusUserGUI userGui = new LoginBonusUserGUI(loginBonusData, player.getUniqueId());
                    userGui.updateUserAccumulatedLoginBonusClaimGui();
                    userGui.getUserAccumulatedLoginBonusClaimGui().show(player);
                    break;
            }
        }
        return true;
    }
}