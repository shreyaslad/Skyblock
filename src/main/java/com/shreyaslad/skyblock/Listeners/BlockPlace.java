package com.shreyaslad.skyblock.Listeners;

import com.shreyaslad.skyblock.Skyblock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class BlockPlace implements Listener {
    @EventHandler
    @SuppressWarnings("Duplicates")
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        World skyblock = Bukkit.getServer().getWorld("skyblock");
        File saveFolder = new File("/mnt/plugins/Skyblock");
        File playerSave = new File(saveFolder + "/" + player.getDisplayName() + ".json");

        if (player.getWorld().getName().equalsIgnoreCase("skyblock")) {
            try {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(new FileReader(playerSave));
                JSONObject jsonObject = (JSONObject) obj;

                String value = (String) jsonObject.get("coords");
                String[] strCoords = value.split(",");
                int[] coords = Skyblock.StringArrToIntArr(strCoords);

                if ((player.getLocation().getBlockX() > coords[0]) && (player.getLocation().getBlockX() < coords[3])) {
                    if ((player.getLocation().getBlockY() > coords[1]) && (player.getLocation().getBlockY() < coords[4])) {
                        if ((player.getLocation().getBlockZ() > coords[2]) && (player.getLocation().getBlockZ() < coords[5])) {
                            event.setCancelled(false);
                        } else {
                            event.setCancelled(true);
                            player.sendMessage(ChatColor.RED + "Skyblock" + ChatColor.GRAY + "|" + ChatColor.WHITE + "You are not allowed to place this block since it is not a part of your island");
                        }
                    }
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        } else {
            //ignore
        }
    }
}
