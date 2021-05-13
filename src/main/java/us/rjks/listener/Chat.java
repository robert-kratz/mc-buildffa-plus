package us.rjks.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.rjks.utils.Config;
import us.rjks.utils.TabList;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 25.04.2021 / 16:28
 *
 **************************************************************************/

public class Chat implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (Config.getBoolean("enable-rank-system") && Config.getBoolean("enable-chat-rank") && Config.getBoolean("enabled")) {
            String message = event.getMessage().replaceAll("%", "%%");
            String format = TabList.getRankByPlayer(event.getPlayer()).getChatformat().replaceAll("%playername%", event.getPlayer().getName());
            if(!event.isCancelled()) event.setFormat(format + message);
        }
    }

}
