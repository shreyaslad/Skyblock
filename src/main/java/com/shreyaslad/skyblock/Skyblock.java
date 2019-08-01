package com.shreyaslad.skyblock;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.MCEditSchematicReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;

import com.sk89q.worldedit.world.DataException;
import net.minecraft.server.v1_14_R1.WorldData;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.json.simple.JSONObject;

import javax.sound.sampled.Clip;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
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

    public static WorldEditPlugin getWorldEdit() {
        return (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("Skyblock");
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
                        /*loc.getBlock().setType(Material.GRASS_BLOCK);
                        loc2.getBlock().setType(Material.GRASS_BLOCK);
                        loc3.getBlock().setType(Material.GRASS_BLOCK);*/

                        int x = Integer.parseInt(args[1]);
                        int y = Integer.parseInt(args[2]);
                        int z = Integer.parseInt(args[3]);

                        File schem = new File("/mnt/plugins/WorldEdit/schematics/island.schem");

                        Vector to = new Vector(x, y, z);

                        /*com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(world);

                        ClipboardFormat format = ClipboardFormats.findByFile(schem);

                        try (ClipboardReader reader = format.getReader(new FileInputStream(schem))) {
                            Clipboard clipboard = reader.read();

                            try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1)) {
                                Operation operation = new ClipboardHolder(clipboard).createPaste(editSession).to(BlockVector3.at(x, y, z)).ignoreAirBlocks(true).build();

                                try {
                                    Operations.complete(operation);
                                    editSession.flushSession();
                                } catch (WorldEditException ex) {
                                    ex.printStackTrace();
                                }

                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }*/

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
                    case "debug":
                        //Doesn't really work
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


                        try {
                            //BufferedReader objectReader;
                            //String line;

                            File playerSave = new File(saveFolder + "/" + player.getDisplayName() + ".json");

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
                    default:

                }
            }

        }

        return false;
    }

}
