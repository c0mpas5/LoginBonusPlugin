package io.github.c0mpas5.loginBonusPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LoginBonusAdminGUI implements Listener {

    private Inventory inv = null;

    public LoginBonusAdminGUI() {
        inv = Bukkit.createInventory(null, 27, "Admin GUI");

        // GUIのアイテムを設定
        ItemStack item = new ItemStack(Material.DIAMOND);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Admin Item");
        item.setItemMeta(meta);

        inv.setItem(13, item); // 中央に配置
    }

    public void openInventory(Player player) {
        player.openInventory(inv);
    }

    public void onInventoryClick(InventoryClickEvent event) {
        // クリックイベントの処理
        if (event.getView().getTitle().equals("Admin GUI")) {
            event.setCancelled(true);
            // アイテムクリック時の処理
        }
    }

    public void onInventoryClose(InventoryCloseEvent event) {
        // インベントリが閉じられたときの処理
    }
}