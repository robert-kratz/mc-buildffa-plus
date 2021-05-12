package us.rjks.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import us.rjks.utils.Config;
import us.rjks.utils.TabList;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 25.04.2021 / 16:27
 *
 **************************************************************************/

public class Join implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (Config.getBoolean("enable-rank-system") && Config.getBoolean("enable-tab-rank")) {
            TabList.setTabList(event.getPlayer());
        }

    }

}
