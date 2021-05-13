package us.rjks.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import us.rjks.event.CountDownEndsEvent;
import us.rjks.event.CountDownTimeEvent;
import us.rjks.game.Main;
import us.rjks.utils.*;

import java.util.logging.Level;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 25.04.2021 / 17:11
 *
 **************************************************************************/

public class CountDown implements Listener {

    @EventHandler
    public void onCountDownEnds(CountDownEndsEvent event) {
        if (event.getType().equals(Type.MAP)) {
            if(MapManager.getSetUpMap().size() == 0) {
                System.out.println("§c§lATTENTION: THERE ARE NOT SETUP MAPS, PLUGIN IS SETUP MODE");
                Main.getGame().setSetup(true);
            } else {
                Main.getGame().getCurrentMap().unloadMap();
                Main.getGame().getCurrentMap().deleteMap();

                MapManager.Map map = MapManager.getRandomMap();
                if(MapManager.getSetUpMap().size() == 1 && !Config.getBoolean("map-change-counter-allow-two-maps-in-a-row")) {
                    Bukkit.getLogger().log(Level.WARNING, "THE PROPERTY: map-change-counter-allow-two-maps-in-a-row in CONFIG.YML IS SET TO FALSE BUT ONLY ONE MAP EXISTS: RESULT: IGNORED CONFIG");
                } else {
                    if(!Config.getBoolean("map-change-counter-allow-two-maps-in-a-row")) {
                        MapManager.Map tmp = MapManager.getRandomMap();
                        while (Main.getGame().getCurrentMap().getName().equalsIgnoreCase(tmp.getName())) {
                            tmp = MapManager.getRandomMap();
                        }
                        map = tmp;
                    }
                }

                map.loadMap();
                Main.getGame().setCurrentMap(map);
                System.out.println("[MAP] Loaded " + map.getName() + " as next map");

                MapManager.Map finalMap = map;
                Messages.getStringList("map-change-broadcast").forEach(msg -> {
                    Bukkit.broadcastMessage(msg.replaceAll("%mapname%", finalMap.getName())
                            .replaceAll("%builder%", finalMap.getAuthor()));
                });

                Bukkit.getOnlinePlayers().forEach(player -> {
                    if(Config.getBoolean("show-score-board")) {
                        ScoreBoard.setScoreBoard(player);
                    }
                });

                Bukkit.getOnlinePlayers().forEach(player -> {
                    TitleManager.sendTitle(player, Messages.getString("map-change-main-title").replaceAll("%mapname%", finalMap.getName()).replaceAll("%builder%", finalMap.getAuthor()), Messages.getString("map-change-sub-title").replaceAll("%mapname%", finalMap.getName()).replaceAll("%builder%", finalMap.getAuthor()));
                    player.teleport(Main.getGame().getCurrentMap().getLocation("spawn"));
                });
            }
        }
    }

    @EventHandler
    public void onCountDownTime(CountDownTimeEvent event) {
        if (event.getType().equals(Type.MAP)) {

        }
    }

}
