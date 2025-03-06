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
    private final LoginBonusAdminGUI adminGui;
    private final LoginBonusUserGUI userGui;

    public LoginBonusCommand(LoginBonusPlugin plugin, LoginBonusData loginBonusData, LoginBonusAdminGUI adminGUI, LoginBonusUserGUI userGui) {
        this.plugin = plugin;
        this.loginBonusData = loginBonusData;
        this.adminGui = adminGUI;
        this.userGui = userGui;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            switch (command.getName().toLowerCase()) {
                case "test":
                    userGui.getUserAccumulatedLoginBonusClaimGui().show(player);
                    break;
                case "admingui":
                    if(sender != player){
                        sender.sendMessage("プレイヤーでないと実行できません");
                        return false;
                    }
                    adminGui.getAdminHomeGui().show(player);
                    break;
                case "loginbonus":
                    userGui.updateUserAccumulatedLoginBonusClaimGui();
                    userGui.getUserAccumulatedLoginBonusClaimGui().show(player);
                    break;
            }
        }
        return true;
    }
}