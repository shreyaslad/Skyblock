package com.shreyaslad.skyblock.Events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ClickEvent implements Listener {

    @EventHandler
    public void clickEvent(InventoryClickEvent e) {

        if (e.getWhoClicked() instanceof Player) {
            Player player = (Player) e.getWhoClicked();

            Inventory gui = e.getInventory();
            if (player.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.DARK_GRAY + "Skyblock")) {
                switch (e.getCurrentItem().getType()) {
                    case ENDER_PEARL:
                        player.closeInventory();
                        player.sendMessage(ChatColor.RED + "Skyblock" + ChatColor.GRAY + " | " + ChatColor.WHITE + "Teleporting you to your island");
                        break;
                    case BARRIER:
                        player.closeInventory();
                        player.sendMessage(ChatColor.RED + "Skyblock" + ChatColor.GRAY + " | " + ChatColor.WHITE + "Returning you to survival");
                        break;
                    case ARROW:
                        player.closeInventory();
                        player.sendMessage(ChatColor.RED + "Skyblock" + ChatColor.GRAY + " | " + ChatColor.WHITE + "Opening a new inventory");
                        break;
                }
                e.setCancelled(true);
            }

        }

        /*Player player = (Player) e.getWhoClicked();

        if (e.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "Skyblock")) {
            switch (e.getCurrentItem().getType()) {
                case ENDER_PEARL:
                    player.closeInventory();
                    player.sendMessage(ChatColor.RED + "Skyblock" + ChatColor.GRAY + " | " + ChatColor.WHITE + "Teleporting you to your island");
                    break;
                case BARRIER:
                    player.closeInventory();
                    player.sendMessage(ChatColor.RED + "Skyblock" + ChatColor.GRAY + " | " + ChatColor.WHITE + "Returning you to survival");
                    break;
                case ARROW:
                    player.closeInventory();
                    player.sendMessage(ChatColor.RED + "Skyblock" + ChatColor.GRAY + " | " + ChatColor.WHITE + "Opening a new inventory");
                    break;
            }

            e.setCancelled(true);
        }*/
    }
}
