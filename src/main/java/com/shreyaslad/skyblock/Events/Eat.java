package com.shreyaslad.skyblock.Events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Eat implements Listener {

   /* @EventHandler
    public static void onConsumeMutton(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();

        File playerSave = new File( "/mnt/plugins/Skyblock/" + player.getDisplayName() + ".json");

        if (event.getItem().getType() == Material.COOKED_MUTTON) {
            try {
                JSONParser parser = new JSONParser();
                Object object = parser.parse(new FileReader(playerSave));
                JSONObject jsonObject = (JSONObject) object;

                int count = Integer.parseInt(jsonObject.get("muttonCount").toString());

                player.sendMessage("You ate cooked mutton!");
                player.sendMessage("Mutton Count: " + count);

                if (count == 0) {
                    //TODO: spawn new island
                    player.sendMessage(ChatColor.RED + "Skyblock" + ChatColor.GRAY + " | " + ChatColor.WHITE + "You unlocked a new island! Go check it out.");
                } else {
                    jsonObject.put("muttonCount", count - 1);

                    FileWriter writer = new FileWriter(playerSave);
                    writer.write(jsonObject.toString());
                    writer.close();
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }*/

}
