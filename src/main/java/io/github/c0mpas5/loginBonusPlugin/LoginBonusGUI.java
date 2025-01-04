package io.github.c0mpas5.loginBonusPlugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

public class LoginBonusGUI implements Listener {

    private final LoginBonusPlugin plugin;

    public LoginBonusGUI(LoginBonusPlugin plugin) {
        this.plugin = plugin;
    }

    public void openInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, "ログインボーナス設定");

        // インベントリのアイテムを設定
        ItemStack item = new ItemStack(Material.DIAMOND);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("1日目の報酬");
        item.setItemMeta(meta);
        inventory.setItem(0, item);

        player.openInventory(inventory);
    }
}