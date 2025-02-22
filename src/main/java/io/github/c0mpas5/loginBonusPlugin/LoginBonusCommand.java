// LoginBonusCommand.java
package io.github.c0mpas5.loginBonusPlugin;

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

    public LoginBonusCommand(LoginBonusPlugin plugin, LoginBonusData loginBonusData, LoginBonusAdminGUI adminGUI) {
        this.plugin = plugin;
        this.loginBonusData = loginBonusData;
        this.adminGui = adminGUI;
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
                    ArrayList<ItemStack> rewardItems = RewardManager.getAllRewards(currentLoginBonusName, "normal");
                    break;
                case "admingui":
                    if(sender != player){
                        sender.sendMessage("プレイヤーでないと実行できません");
                        return false;
                    }
                    adminGui.getAdminHomeGui().show(player);
                    break;
            }
        }
        return true;
    }
}