package us.rjks.cmd;

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
 *  Erstellt: 16.05.2021 / 09:20
 *
 **************************************************************************/

public class Stats implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command label, String s, String[] args) {

        if(sender instanceof Player) {
            if(Config.getBoolean("enable-stats-system")) {
                final int kills = Main.getGame().getStats().getKills(((Player) sender).getUniqueId().toString()), deaths = Main.getGame().getStats().getDeaths(((Player) sender).getUniqueId().toString());
                double kd = 0;

                try {
                    kd = kills / deaths;
                } catch (Exception e) {

                }
                final double kkd = kd;

                Messages.getStringList("stats-command-show").forEach(s1 -> {
                    sender.sendMessage(s1.replaceAll("%player%", sender.getName()).replaceAll("%kills%", kills + "").replaceAll("%deaths%", deaths + "").replaceAll("%kd%", kkd + "").replaceAll("%ranking%", Main.getGame().getStats().getRank(((Player) sender).getUniqueId().toString()) + ""));
                });
            } else {
                sender.sendMessage("stats-command-disabled");
            }
        } else {
            sender.sendMessage(Config.getString("no-player"));
        }

        return false;
    }
}
