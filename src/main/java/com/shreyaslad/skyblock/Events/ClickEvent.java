package com.shreyaslad.skyblock.Events;

import com.shreyaslad.skyblock.Skyblock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import java.io.File;

public class ClickEvent implements Listener {

    @EventHandler
    public void clickEvent(InventoryClickEvent e) {

        if (e.getWhoClicked() instanceof Player) {
            Player player = (Player) e.getWhoClicked();

            File playerSave = new File( "/mnt/plugins/Skyblock/" + player.getDisplayName() + ".json");

            //Inventory gui = e.getInventory();
            if (player.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.DARK_GRAY + "Skyblock")) {
                switch (e.getCurrentItem().getType()) {
                    case ENDER_PEARL:
                        player.closeInventory();
                        player.sendMessage(ChatColor.RED + "Skyblock" + ChatColor.GRAY + " | " + ChatColor.WHITE + "Teleporting you to your island");

                        int[] coords = Skyblock.getOffsetCoords();

                        if (Skyblock.hasIsland(player)) {
                            int[] playerCoords = Skyblock.getCoords(player);
                            player.teleport(new Location(Bukkit.getServer().getWorld("skyblock"), playerCoords[0], playerCoords[1], playerCoords[2]));
                        } else {
                            Skyblock.generateIsland(player, playerSave, coords[0], coords[1], coords[2]);
                        }
                        break;
                    case BARRIER:
                        player.closeInventory();
                        player.sendMessage(ChatColor.RED + "Skyblock" + ChatColor.GRAY + " | " + ChatColor.WHITE + "Returning you to survival");
                        player.chat("/goto world");
                        break;
                    case ARROW:
                        player.closeInventory();
                        player.sendMessage(ChatColor.RED + "Skyblock" + ChatColor.GRAY + " | " + ChatColor.WHITE + "WIP");
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