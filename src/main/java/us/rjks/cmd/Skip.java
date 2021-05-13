package us.rjks.cmd;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.rjks.game.Main;
import us.rjks.utils.Config;
import us.rjks.utils.ItemBuilder;
import us.rjks.utils.MapManager;
import us.rjks.utils.Messages;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 13.05.2021 / 12:08
 *
 **************************************************************************/

public class Skip implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command label, String s, String[] args) {

        if(sender instanceof Player) {
            if(sender.hasPermission(Config.getString("permissions.skip"))) {
                if(Main.getGame().getMapchange().getCountdown() < Config.getInteger("map-skip-countdown-possible-only-at-second") || (Main.getGame().getMapchange().getCountdown() > Config.getInteger("map-skip-countdown-possible-only-at-second") && sender.hasPermission(Config.getString("permissions.always-skip")))) {
                    if(Main.getGame().getMapchange().getCountdown() > Config.getInteger("map-skip-countdown-to")) {
                        Main.getGame().getMapchange().setCountdown(Config.getInteger("map-skip-countdown-to"));
                        sender.sendMessage(Messages.getString("skip-command-map-skiped-successfully"));
                        Bukkit.broadcastMessage(Messages.getString("skip-command-map-skiped-successfully-broadcast").replaceAll("%playername%", sender.getName()));
                    } else {
                        sender.sendMessage(Messages.getString("skip-command-map-skiped-error"));
                    }
                } else {
                    sender.sendMessage(Messages.getString("skip-command-map-skiped-wait-until-skip"));
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
