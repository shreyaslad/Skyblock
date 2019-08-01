package com.shreyaslad.skyblock.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        World skyblock = Bukkit.getServer().getWorld("skyblock");

        if (player.getWorld().getName().equalsIgnoreCase("skyblock")) {
            player.sendMessage("You placed a block in skyblock");
        } else {
            //ignore
        }
    }
}
