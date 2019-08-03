package com.shreyaslad.skyblock.Events;

import com.shreyaslad.skyblock.Skyblock;
import com.sk89q.worldguard.bukkit.event.block.PlaceBlockEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ClickEvent implements Listener {

    @EventHandler
    @SuppressWarnings({"Duplicates", "Unchecked"})
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

                        try {
                            JSONParser parser = new JSONParser();
                            Object object = parser.parse(new FileReader(playerSave));
                            JSONObject jsonObject = (JSONObject) object;

                            String coords = jsonObject.get("coords").toString();

                            //int[] coords = Skyblock.StringArrToIntArr(textCoords);

                            if (coords.equals("null")) {
                                player.setOp(true);

                                String message = "/skyblock forcegen " + player.getDisplayName();
                                player.sendMessage("Doing " + message);
                                player.chat(message);
                                player.setOp(false);
                            } else {
                                int[] coordsArr = Skyblock.StringArrToIntArr(coords.split(","));
                                player.teleport(new Location(Bukkit.getServer().getWorld("skyblock"), coordsArr[0], coordsArr[1], coordsArr[2]));
                            }
                        } catch (IOException | ParseException ex) {
                            ex.printStackTrace();
                        }

                        /*if (!Skyblock.hasIsland(player)) {
                            int[] coords = Skyblock.getOffsetCoords();
                            Skyblock.generateIsland(player, playerSave, coords[0], coords[1], coords[2]);
                            player.teleport(new Location(Bukkit.getServer().getWorld("skyblock"), coords[0], coords[1] + 8, coords[2]));

                            player.setBedSpawnLocation(new Location(Bukkit.getServer().getWorld("skyblock"), coords[0], coords[1] + 8, coords[2]));

                            try {
                                JSONParser jsonParser = new JSONParser();
                                Object object = jsonParser.parse(new FileReader(playerSave));
                                JSONObject jsonObject = (JSONObject) object;
                                jsonObject.put("coords", coords[0] + "," + coords[1] + "," + coords[2]);
                                FileWriter fileWriter = new FileWriter(playerSave);
                                fileWriter.write(jsonObject.toString());
                                fileWriter.close();
                            } catch (IOException | ParseException ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            try {
                                JSONParser parser = new JSONParser();
                                Object object = parser.parse(new FileReader(playerSave));
                                JSONObject jsonObject = (JSONObject) object;
                                String[] textCoords = jsonObject.get("coords").toString().split(",");
                                int[] coords = Skyblock.StringArrToIntArr(textCoords);
                                player.teleport(new Location(Bukkit.getServer().getWorld("skyblock"), coords[0], coords[1] + 8, coords[2]));
                                player.setBedSpawnLocation(new Location(Bukkit.getServer().getWorld("skyblock"), coords[0], coords[1] + 8, coords[2]));
                            } catch (IOException | ParseException ex) {
                                ex.printStackTrace();
                            }
                        }*/
                        break;
                    case BARRIER:
                        player.closeInventory();
                        player.sendMessage(ChatColor.RED + "Skyblock" + ChatColor.GRAY + " | " + ChatColor.WHITE + "Returning you to survival");
                        player.chat("/mv tp " + player.getDisplayName() + " world");
                        break;
                    case ARROW:
                        player.closeInventory();
                        player.sendMessage(ChatColor.RED + "Skyblock" + ChatColor.GRAY + " | " + ChatColor.WHITE + "WIP");
                        break;
                }
                e.setCancelled(true);
            }

        }
    }

    @EventHandler
    public static void something(InventoryInteractEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();

            if (player.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.DARK_GRAY + "Skyblock")) {
                event.setCancelled(true);
            }
        }
    }
}