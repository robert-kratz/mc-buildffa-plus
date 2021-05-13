package us.rjks.cmd;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.rjks.utils.Config;
import us.rjks.utils.ItemBuilder;
import us.rjks.utils.MapManager;
import us.rjks.utils.Messages;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 11.05.2021 / 17:31
 *
 **************************************************************************/

public class Map implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command label, String s, String[] args) {

        if(sender instanceof Player) {
            if(sender.hasPermission(Config.getString("permissions.operator"))) {
                if(args.length == 0) {
                    Messages.getStringList("map-command-syntax").forEach(msg -> {
                        sender.sendMessage(msg);
                    });
                    return true;
                } else if(args.length == 1) {
                    if(args[0].equalsIgnoreCase("list")) {
                        sender.sendMessage(Messages.getString("map-command-list-title").replaceAll("%amount%", MapManager.getMaps().size() + ""));
                        MapManager.getMaps().forEach(map -> {
                            sender.sendMessage(Messages.getString("map-command-list-item").replaceAll("%mapname%", map.getName()).replaceAll("%mapsrc%", map.getSrc()));
                        });
                    }
                    return true;
                } else if(args.length == 2) {
                    if(args[0].equalsIgnoreCase("delete")) {
                        if(MapManager.getMapFromName(args[1]) != null) {
                            MapManager.Map map = MapManager.getMapFromName(args[1]);

                            map.unloadMap();
                            map.deleteMapFromConfig();
                            sender.sendMessage(Messages.getString("map-command-delete-success").replaceAll("%mapname%", args[0]));
                        } else {
                            sender.sendMessage(Messages.getString("map-command-delete-error").replaceAll("%mapname%", args[1]));
                        }
                    }
                    return true;
                } else if(args.length == 3) {
                    if(args[0].equalsIgnoreCase("create")) {
                        if(MapManager.getMapFromName(args[1]) == null) {
                            boolean ex = MapManager.createMap(args[1], args[2], new ItemBuilder(Material.SUGAR, args[2]).checkout(), "none", "This Map was loaded from " + sender.getName());
                            if(!ex) {
                                sender.sendMessage(Messages.getString("map-command-map-not-found").replaceAll("%mapname%", args[1]));
                            } else {
                                sender.sendMessage(Messages.getString("map-command-create-success").replaceAll("%mapname%", args[1]));

                                MapManager.getMapFromName(args[1]).loadMap();
                                MapManager.getMapFromName(args[1]).teleportPlayerToWorld(((Player) sender).getPlayer());
                                sender.sendMessage(Messages.getString("map-command-create-set-spawn-after"));
                            }
                        } else {
                            sender.sendMessage(Messages.getString("map-command-map-already-exists").replaceAll("%mapname%", args[1]));
                        }
                    } else if(args[0].equalsIgnoreCase("setspawn")) {
                        if(MapManager.getMapFromName(args[1]) != null) {
                            MapManager.Map map = MapManager.getMapFromName(args[1]);

                            map.setLocation(((Player) sender).getLocation(), args[2]);
                            sender.sendMessage(Messages.getString("map-command-spawn-set").replaceAll("%mapname%", args[1]).replaceAll("%spawn%", args[2]));
                        } else {
                            sender.sendMessage(Messages.getString("map-command-map-not-found").replaceAll("%mapname%", args[1]).replaceAll("%spawn%", args[2]));
                        }
                    }
                    return true;
                } else {
                    Messages.getStringList("map-command-syntax").forEach(msg -> {
                        sender.sendMessage(msg);
                    });
                }
            } else {
                sender.sendMessage(Messages.getString("no-permission"));
            }
        } else {
            sender.sendMessage(Config.getString("no-player"));
        }

        return false;
    }
}
