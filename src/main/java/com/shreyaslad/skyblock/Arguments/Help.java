package com.shreyaslad.skyblock.Arguments;

import org.bukkit.entity.Player;

public class Help {
    public static void sendHelp(Player player) {
        String s = "------Skyblock Help------\n" + "/skyblock help - shows the help section";
        player.sendMessage(s);
    }
}
