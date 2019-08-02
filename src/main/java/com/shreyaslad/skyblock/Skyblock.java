package com.shreyaslad.skyblock;

import com.shreyaslad.skyblock.Arguments.Help;
import com.shreyaslad.skyblock.Events.ClickEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public final class Skyblock extends JavaPlugin implements Listener {

    private static File locSave = new File("/mnt/plugins/Skyblock/locSave.txt");

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("Skyblock plugin loaded!");
        getServer().getPluginManager().registerEvents(new ClickEvent(), this);
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

            File playerSave = new File(saveFolder + "/" + player.getDisplayName() + ".json");

            if (args.length == 0) {

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

                if (hasIsland(player)) {
                    int[] coords = getOffsetCoords();

                    generateIsland(player, playerSave, coords[0], coords[1], coords[2]);
                    player.teleport(new Location(Bukkit.getServer().getWorld("skyblock"), coords[0], coords[1] + 8, coords[2]));

                    try {
                        JSONParser parser = new JSONParser();
                        Object object = parser.parse(new FileReader(playerSave));
                        JSONObject jsonObject = (JSONObject) object;

                        jsonObject.put("coords", coords[0] + "," + coords[1] + "," + coords[2]);

                        FileWriter fileWriter = new FileWriter(playerSave);
                        fileWriter.write(jsonObject.toString());
                        fileWriter.close();
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONParser parser = new JSONParser();
                        Object object = parser.parse(new FileReader(playerSave));
                        JSONObject jsonObject = (JSONObject) object;

                        String[] textCoords = jsonObject.get("coords").toString().split(",");
                        int[] coords = StringArrToIntArr(textCoords);

                        player.teleport(new Location(Bukkit.getServer().getWorld("skyblock"), coords[0], coords[1] + 8, coords[2]));
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
                }


            } else {
                switch (args[0]) {
                    case "help":
                        Help.sendHelp(player);
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
                            if (!locSave.exists()) {
                                locSave.createNewFile();
                                System.out.println("Skyblock [DEBUG] - Creating LocSave file " + locSave.getCanonicalPath());
                                player.sendMessage("Skyblock [DEBUG] - Creating LocSave file " + locSave.getCanonicalPath());
                            } else {
                                System.out.println("Skyblock [DEBUG] - LocSave exists");
                                player.sendMessage("Skyblock [DEBUG] - LocSave exists");
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
                    case "gui":

                        Inventory gui = Bukkit.createInventory(player, 9, ChatColor.DARK_GRAY + "Skyblock");

                        ItemStack teleport = new ItemStack(Material.ENDER_PEARL);
                        ItemStack leave = new ItemStack(Material.BARRIER);
                        ItemStack visit = new ItemStack(Material.ARROW);

                        ItemMeta teleportMeta = teleport.getItemMeta();
                        teleportMeta.setDisplayName(ChatColor.DARK_AQUA + "Teleport");
                        ArrayList<String> teleportLore = new ArrayList<>();
                        teleportLore.add(ChatColor.GOLD + "Teleport to your Skyblock island");
                        teleportMeta.setLore(teleportLore);

                        teleport.setItemMeta(teleportMeta);

                        ItemMeta leaveMeta = leave.getItemMeta();
                        leaveMeta.setDisplayName(ChatColor.DARK_AQUA + "Leave");
                        ArrayList<String> leaveLore = new ArrayList<>();
                        leaveLore.add(ChatColor.GOLD + "Return to survival");
                        leaveMeta.setLore(leaveLore);

                        leave.setItemMeta(leaveMeta);

                        ItemMeta visitMeta = leave.getItemMeta();
                        visitMeta.setDisplayName(ChatColor.DARK_PURPLE + "Visit Island");
                        ArrayList<String> visitLore = new ArrayList<>();
                        visitLore.add(ChatColor.GOLD + "Check out anyone's island");
                        leaveMeta.setLore(visitLore);

                        visit.setItemMeta(visitMeta);

                        /*ItemStack[]  menuItems = {teleport, visit, leave};
                        gui.setContents(menuItems);*/
                        gui.addItem(teleport);
                        gui.addItem(visit);
                        gui.addItem(leave);
                        player.openInventory(gui);

                        break;
                    default:

                }
            }

        }

        return false;
    }

    public static void generateIsland(Player player, File playerSave, int x, int y, int z) {
        int stonex = x + 4;
        int stoney = y + 2;
        int stonez = z + 5;

        int midy = stoney + 1;

        int dirty = midy + 1; //lmao dirt + y = dirty

        int voidx = stonex + 3;
        int voidy = y - 1;
        int voidz = stonez + 3;

        int backx = x - 3;
        int backz = z - 3;

        //String test = "/fill 112 72 -418 118 78 -426 minecraft:stone";
        String fillStone = "/fill " + x + " " + y + " " + z + " " + stonex + " " + stoney + " " + stonez + " minecraft:stone";
        String fillDirt = "/fill " + stonex + " " + midy + " " + stonez + " " + x + " " + dirty + " " + z + " minecraft:dirt";
        String fillAir = "/fill " + backx + " " + voidy + " " + backz + " " + voidx + " " + 0 + " " + voidz + " minecraft:air";
        //String fill = String.format(test + "%s %d %e minecraft:stone", Integer.toString(x+6), Integer.toString(y+6), Integer.toString(z+6));
        if (Bukkit.getServer().getPlayer(player.getDisplayName()).isOp()) {
            //String fill = "/fill " + x + " " + y + " " + z + (x+6) + " " + (y+6) + " " + (z+6) + " minecraft:stone";
            player.chat(fillStone);
            player.chat(fillDirt);
            player.chat(fillAir);
        } else {
            player.setOp(true);
            player.chat(fillStone);
            player.chat(fillDirt);
            player.chat(fillAir);
            player.setOp(false);
        }

        int chesty = dirty + 1;

        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(playerSave));
            JSONObject jsonObject = (JSONObject) obj;

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

        try {
            FileWriter fileWriter = new FileWriter(locSave);
            fileWriter.write(x + "," + y + "," + z);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static int[] getOffsetCoords() {
        try {
            String line;

            BufferedReader reader = new BufferedReader(new FileReader(locSave));

            while ((line = reader.readLine()) != null) {
                String[] textCoords = line.split(",");
                int[] coords = StringArrToIntArr(textCoords);

                coords[0] += 100;

                return coords;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int[] StringArrToIntArr(String[] s) {
        int[] result = new int[s.length];
        for (int i = 0; i < s.length; i++) {
            result[i] = Integer.parseInt(s[i]);
        }
        return result;
    }

    // Will implement this later
    public static boolean hasIsland(Player player) {
        try {
            File playerSave = new File("/mnt/plugins/Skyblock/" + player.getDisplayName() + ".json");

            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(playerSave));
            JSONObject jsonObject = (JSONObject) obj;

            String coords = jsonObject.toString();

            if (coords.equalsIgnoreCase("null")) {
                return false;
            } else {
                return true;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
