package us.rjks.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import us.rjks.utils.Config;
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
    }

}
