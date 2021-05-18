package us.rjks.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import us.rjks.game.Main;
import us.rjks.utils.KitManager;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 16.05.2021 / 16:32
 *
 **************************************************************************/

public class Move implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if(!Main.getGame().isSetup()) {
            if((double)Main.getGame().getCurrentMap().getProperty("safeheight") >= event.getPlayer().getLocation().getY()) {
                if(!Main.getGame().ingame.contains(event.getPlayer())) {
                    Main.getGame().ingame.add(event.getPlayer());
                    event.getPlayer().closeInventory();
                    if(KitManager.getKitFromName("startInv") != null) {
                        KitManager.getKitFromName("startInv").setKit(event.getPlayer());
                    }
                }
            }

            if((double)Main.getGame().getCurrentMap().getProperty("deathheight") >= event.getPlayer().getLocation().getY()) {
                event.getPlayer().setHealth(0);
            }
        }
    }

}
