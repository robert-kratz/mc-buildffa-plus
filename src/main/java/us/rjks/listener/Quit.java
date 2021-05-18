package us.rjks.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import us.rjks.game.Main;
import us.rjks.utils.Config;
import us.rjks.utils.KitManager;
import us.rjks.utils.Messages;
import us.rjks.utils.TabList;

import java.util.List;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 25.04.2021 / 16:27
 *
 **************************************************************************/

public class Quit implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (Config.getBoolean("enable-rank-system") && Config.getBoolean("enable-tab-rank")) {
            TabList.cache.remove(event.getPlayer());
        }

        if(Main.getGame().ingame.contains(event.getPlayer())) {
            Main.getGame().ingame.remove(event.getPlayer());
        }

        if(Config.getBoolean("enable-player-leaves-message")) {
            event.setQuitMessage(Messages.getString("player-leaves-message").replaceAll("%player%", event.getPlayer().getName()));
        }

        event.getPlayer().getInventory().setBoots(null);
        event.getPlayer().getInventory().setLeggings(null);
        event.getPlayer().getInventory().setChestplate(null);
        event.getPlayer().getInventory().setHelmet(null);

        event.getPlayer().getInventory().clear();
        event.getPlayer().setFoodLevel(30);
        event.getPlayer().setLevel(0);
    }

}
