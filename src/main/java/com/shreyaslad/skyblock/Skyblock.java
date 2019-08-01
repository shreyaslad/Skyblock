package com.shreyaslad.skyblock;

import com.shreyaslad.skyblock.Listeners.BlockBreak;
import com.shreyaslad.skyblock.Listeners.BlockPlace;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public final class Skyblock extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("Skyblock plugin loaded!");
        getServer().getPluginManager().registerEvents(new BlockBreak(), this);
        getServer().getPluginManager().registerEvents(new BlockPlace(), this);
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

            Map<String, String> map = new HashMap<>();

            World world = Bukkit.getServer().getWorld("newworld");

            File playerSave = new File(saveFolder + "/" + player.getDisplayName() + ".json");

            /*World world = Bukkit.getServer().getWorld("newworld");
            Location loc = new Location(world, Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
            Location loc2 = new Location(world, Integer.parseInt(args[1]) + 1, Integer.parseInt(args[2]), Integer.parseInt(args[3]));
            Location loc3 = new Location(world, Integer.parseInt(args[1]) + 2, Integer.parseInt(args[2]), Integer.parseInt(args[3]));*/

            if (args.length == 0) {
                player.sendMessage("Doing the things");
                //TODO: check if they have a world

            } else {
                switch (args[0]) {
                    case "help":
                        player.sendMessage("------Skyblock Help------\n" + "/skyblock help - shows the help section");
                        break;

                    case "island":
                        int x = Integer.parseInt(args[1]);
                        int y = Integer.parseInt(args[2]);
                        int z = Integer.parseInt(args[3]);

                        int stonex = x + 4;
                        int stoney = y + 2;
                        int stonez = z + 5;

                        int midy = stoney + 1;

                        int dirtx = x+4;
                        int dirty = midy + 1; //lmao dirt + y = dirty
                        int dirtz = z + 5;

                        //String test = "/fill 112 72 -418 118 78 -426 minecraft:stone";
                        String fillStone = "/fill " + x + " " + y + " " + z + " " + stonex + " " + stoney + " " + stonez + " minecraft:stone";
                        String fillDirt = "/fill " + stonex + " " + midy + " " + stonez + " " + x + " " + dirty + " " + z + " minecraft:dirt";
                        //String fill = String.format(test + "%s %d %e minecraft:stone", Integer.toString(x+6), Integer.toString(y+6), Integer.toString(z+6));
                        if (Bukkit.getServer().getPlayer(player.getDisplayName()).isOp()) {
                            //String fill = "/fill " + x + " " + y + " " + z + (x+6) + " " + (y+6) + " " + (z+6) + " minecraft:stone";
                            player.chat(fillStone);
                            player.chat(fillDirt);
                        } else {
                            player.setOp(true);
                            player.chat(fillStone);
                            player.chat(fillDirt);
                            player.setOp(false);
                        }

                        try {
                            JSONParser parser = new JSONParser();
                            Object obj = parser.parse(new FileReader(playerSave));
                            JSONObject jsonObject = (JSONObject) obj;

                            int chesty = dirty + 1;

                            jsonObject.put("coords", x + "," + y + "," + z + "," + stonex + "," + chesty + "," + stonez);

                            FileWriter fw = new FileWriter(playerSave);
                            fw.write(jsonObject.toString());
                            fw.close();
                        } catch (IOException | ParseException ex) {
                            ex.printStackTrace();
                        }

                        Location loc = new Location(Bukkit.getServer().getWorld("skyblock"), x, dirty + 1, z);
                        Block block = loc.getBlock();
                        loc.getBlock().setType(Material.CHEST);
                        Chest chest = (Chest) block.getState();
                        Inventory inv = chest.getInventory();

                        ItemStack lava = new ItemStack(Material.LAVA_BUCKET, 1);
                        ItemStack water = new ItemStack(Material.WATER_BUCKET, 1);
                        ItemStack mutton = new ItemStack(Material.COOKED_MUTTON, 64);
                        ItemStack sapling = new ItemStack(Material.OAK_SAPLING);
                        ItemStack bonemeal = new ItemStack(Material.BONE_MEAL, 64);

                        inv.addItem(lava, water, mutton, sapling, bonemeal);

                        break;
                    case "init":
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
                    case "delete":
                        try {
                            JSONParser parser = new JSONParser();
                            Object obj = parser.parse(new FileReader(playerSave));
                            JSONObject jsonObject = (JSONObject) obj;

                            if (jsonObject.get("coords").equals("null")) {
                                player.sendMessage(ChatColor.RED + "Skyblock" + ChatColor.GRAY + "|" + ChatColor.WHITE + "Do \"/skyblock island\" to create your island");
                            }

                            String value = (String) jsonObject.get("coords");
                            String[] coords = value.split(",");

                            String area = "/fill " + coords[0] + " " + coords[1] + " " + coords[2] + " " + coords[3] + " " + coords[4] + " " + coords[5] + " minecraft:air";
                            if (Bukkit.getServer().getPlayer(player.getDisplayName()).isOp()) {
                                player.chat(area);
                            } else {
                                player.setOp(true);
                                player.chat(area);
                                player.setOp(false);
                            }

                        } catch (IOException | ParseException e) {
                            player.sendMessage(ChatColor.RED + "Skyblock" + ChatColor.GRAY + "|" + ChatColor.WHITE + "No world found. Do \"/skyblock debug\" to create one");
                        }
                        break;
                    case "debug":

                        try {


                            if (playerSave.exists()) {
                                player.sendMessage(ChatColor.RED + "Skyblock" + ChatColor.GRAY + "|" + ChatColor.WHITE + "We found your world");
                                //TODO: teleport them to their world and load configs with BufferedReader
                            } else {
                                player.sendMessage(ChatColor.RED + "Skyblock" + ChatColor.GRAY + "|" + ChatColor.WHITE + "No world found. Creating one now");
                                playerSave.createNewFile();

                                FileWriter writer = new FileWriter(playerSave);

                                JSONObject object = new JSONObject();
                                object.put("achievements", "null");
                                object.put("coords", "null"); //TODO: put coords when doing island generation
                                object.put("firstlog", "true");

                                writer.write(object.toString());
                                writer.close();
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    case "read":
                        try {
                            JSONParser jsonParser = new JSONParser();
                            try (FileReader fileReader = new FileReader(playerSave)) {
                                Object obj = jsonParser.parse(fileReader);

                                JSONObject playerObject = (JSONObject) obj;

                                String coords = (String) playerObject.get("coords");

                                player.sendMessage(coords);
                            }
                        } catch (IOException | ParseException ex) {
                            ex.printStackTrace();
                        }
                        break;
                    default:

                }
            }

        }

        return false;
    }

    public static int[] StringArrToIntArr(String[] s) {
        int[] result = new int[s.length];
        for (int i = 0; i < s.length; i++) {
            result[i] = Integer.parseInt(s[i]);
        }
        return result;
    }
}
