package com.shreyaslad.skyblock;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.Scanner;

public final class Skyblock extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("Skyblock plugin loaded!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("skyblock")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players are allowed to use this command");
            }

            Player player = (Player) sender;

            File saveFolder = new File(getDataFolder() + "/");
            File save = new File(saveFolder + "/data.json");

            if (args.length == 0) {
                player.sendMessage("Doing the things");
                //TODO: check if they have a world

            } else {
                switch (args[0]) {
                    case "help":
                        player.sendMessage("------Skyblock Help------\n" + "/skyblock help - shows the help section");
                        break;
                    case "debug":
                        try {
                            if (!saveFolder.exists()) {
                                saveFolder.mkdir();
                                System.out.println("Skyblock [DEBUG] - Creating save folder " + saveFolder.getCanonicalPath());
                            } else {
                                System.out.println("Skyblock [DEBUG] - Save folder exists");
                            }
                            if (!save.exists()) {
                                save.createNewFile();
                                System.out.println("Skyblock [DEBUG] - Creating save file " + save.getCanonicalPath());
                            } else {
                                System.out.println("Skyblock [DEBUG] - Save file exists");
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    case "debug2":
                        /*try {
                            player.sendMessage("Doing more things");
                            Scanner scanner = new Scanner(save);
                            while (scanner.hasNextLine()) {
                                String line = scanner.nextLine();
                                if (line.contains(player.getDisplayName())) {
                                    player.sendMessage(ChatColor.RED + "Skyblock" + ChatColor.GRAY + "|" + ChatColor.WHITE + "We found your world");
                                } else {
                                    player.sendMessage(ChatColor.RED + "Skyblock" + ChatColor.GRAY + "|" + ChatColor.WHITE + "No world found. Creating one now");

                                    //TODO: try to use WorldEdit api and block radius detection (https://bukkit.org/threads/find-block-in-radius.88298/) (https://www.spigotmc.org/threads/getting-blocks-in-a-radius.60296/)

                                    JSONObject object = new JSONObject();
                                    object.put("name", player.getDisplayName());

                                    try (FileWriter file = new FileWriter(save)) {
                                        file.write(object.toString());
                                        System.out.println("Skyblock [DEBUG] - Wrote " + player.getDisplayName() + " to file.");
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }*/

                        BufferedReader objectReader;
                        try {
                            String line;

                            player.sendMessage(save.getCanonicalPath());
                            objectReader = new BufferedReader(new FileReader(save));
                            while ((line = objectReader.readLine()) != null) {
                                if (line.contains(player.getDisplayName())) {
                                    player.sendMessage("Found your world");
                                    //TODO: save current inventory, teleport them to island, load skyblock inventory, give them nether star GUI thing
                                } else {
                                    JSONObject object = new JSONObject();
                                    object.put("name", player.getDisplayName());

                                    FileWriter fileWriter = new FileWriter(save);
                                    fileWriter.write(line + "," + object.toString());
                                    fileWriter.close();

                                    player.sendMessage("Didn't find your world. Creating one now");
                                }
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    default:

                }
            }

        }

        return false;
    }
}
