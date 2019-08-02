package com.shreyaslad.skyblock.Arguments;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Help {
    public static void sendHelp(Player player) {
        String s = "------" + ChatColor.RED + "Skyblock Help" + ChatColor.WHITE + "------\n" + ChatColor.RED + "/skyblock" + ChatColor.WHITE + " - opens up the Skyblock GUI\n" + ChatColor.RED + "/skyblock " + ChatColor.GOLD + "help" + ChatColor.WHITE + " - shows the help section";
        player.sendMessage(s);
    }
}
