package us.rjks.listener;

import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import us.rjks.db.Coins;
import us.rjks.db.Stats;
import us.rjks.game.GameManager;
import us.rjks.game.Main;
import us.rjks.utils.Config;
import us.rjks.utils.KitManager;
import us.rjks.utils.Messages;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 15.05.2021 / 17:18
 *
 **************************************************************************/

public class Death implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        respawn(event.getEntity());

        event.setDroppedExp(0);
        event.setDeathMessage(null);
        event.getDrops().clear();

        if(Config.getBoolean("enable-stats-system")) {
            Stats.addDeaths(player.getUniqueId().toString(), 1);
        }

        player.playSound(player.getLocation(), Sound.valueOf(Messages.getString("player-died-sound")), 1, 1);
        if(event.getEntity().getKiller() != null) {
            if(event.getEntity().getKiller().equals(event.getEntity())) {
                player.sendMessage(Messages.getString("player-died-due-suicide-message"));
            } else {
                Player killer = event.getEntity().getKiller();

                player.sendMessage(Messages.getString("player-killed-by-killer-message").replaceAll("%killer%", killer.getName()).replaceAll("%killstreak%", killer.getLevel() + ""));
                killer.sendMessage(Messages.getString("killer-killed-player-message").replaceAll("%player%", player.getName()).replaceAll("%killstreak%", player.getLevel() + ""));

                player.playSound(player.getLocation(), Sound.valueOf(Messages.getString("killer-killed-sound")), 1, 1);

                if(Config.getBoolean("enable-stats-system")) {
                    Stats.addKills(killer.getUniqueId().toString(), 1);
                }

                killer.setLevel(killer.getLevel() + 1);
                checkStreak(killer);

                if(Config.getBoolean("enable-coins-system")) {
                    Coins.addCoins(killer.getUniqueId().toString(), Config.getInteger("killer-coins-on-kill"));
                    killer.sendMessage(Messages.getString("player-gets-coins").replaceAll("%amount%", Config.getInteger("killer-coins-on-kill") + ""));
                    Coins.removeCoins(player.getUniqueId().toString(), Config.getInteger("player-lose-coins-on-death"));
                    player.sendMessage(Messages.getString("player-lose-coins").replaceAll("%amount%", Config.getInteger("player-lose-coins-on-death") + ""));
                }

                if(Config.getBoolean("show-score-board")) {
                    Main.getGame().getScoreBoard().setScoreBoard(killer);
                }
            }
        } else {
            player.sendMessage(Messages.getString("player-died-message"));
        }

        if(Config.getBoolean("show-score-board")) {
            Main.getGame().getScoreBoard().setScoreBoard(player);
        }
        Main.getGame().getScoreBoard().setScoreBoard(player);
        player.setLevel(0);
    }

    private void checkStreak(Player player) {
        Config.getBoolean("enable-coins-system");
        Config.getIntegerList("kill-streak-broadcast").forEach(integer -> {
            if(player.getLevel() == integer) {
                Messages.getStringList("player-killstreak-broadcast").forEach(s -> {
                    Bukkit.broadcastMessage(s.replaceAll("%player%", player.getName()).replaceAll("%streak%", player.getLevel() + ""));
                });
            }
        });
        if(Config.getBoolean("enable-coins-system")) {
            Config.getIntegerList("kill-streak-bonus-coins").forEach(integer -> {
                if(player.getLevel() == integer) {
                    Coins.addCoins(player.getUniqueId().toString(), Config.getInteger("killstreak-bonus-coins"));
                    player.sendMessage(Messages.getString("player-gets-coins").replaceAll("%amount%", Config.getInteger("killstreak-bonus-coins") + ""));
                }
            });
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent event) {
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable(){

            @Override
            public void run() {
                Main.getGame().getCurrentMap().teleportPlayerToRandomLocationCollection(event.getPlayer(), "spawn");
                if(KitManager.getKitFromName("startInv") != null) {
                    KitManager.getKitFromName("startInv").setKit(event.getPlayer());
                }
            }
        }, 1);
    }

    private void respawn(final Player p) {
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable(){

            @Override
            public void run() {
                ((CraftPlayer)p).getHandle().playerConnection.a(new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN));
            }
        }, 1);
    }

}
