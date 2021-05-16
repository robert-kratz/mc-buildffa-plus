package us.rjks.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.rjks.game.Main;
import us.rjks.utils.Config;
import us.rjks.utils.MapManager;
import us.rjks.utils.Messages;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 15.05.2021 / 15:31
 *
 **************************************************************************/

public class Forcemap implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command label, String s, String[] args) {

        if(sender instanceof Player) {
            if(sender.hasPermission(Config.getString("permissions.forcemap"))) {
                if (Main.getGame().getForcemap() == null) {
                    if(args.length == 1) {
                        if(MapManager.getMapFromName(args[0]) != null && MapManager.getSetUpMap().contains(MapManager.getMapFromName(args[0]))) {
                            Main.getGame().setForcemap(MapManager.getMapFromName(args[0]));
                            sender.sendMessage(Messages.getString("forcemap-command-successfully").replaceAll("%map%", MapManager.getMapFromName(args[0]).getName()));
                        } else {
                            sender.sendMessage(Messages.getString("forcemap-command-map-not-found").replaceAll("%map%", args[0]));
                        }
                    } else {
                        String maps = "";
                        for (MapManager.Map m : MapManager.getSetUpMap()) {
                            if(maps.isEmpty()) {
                                maps = m.getName();
                            } else {
                                maps = maps + ", " + m.getName();
                            }
                        }
                        final String maa = maps;
                        System.out.println(maa);
                        Messages.getStringList("forcemap-command-syntax-help").forEach(s1 -> {
                            sender.sendMessage(s1.replaceAll("%maps%", maa));
                        });
                    }
                } else {
                    sender.sendMessage(Messages.getString("forcemap-command-already-forced"));
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