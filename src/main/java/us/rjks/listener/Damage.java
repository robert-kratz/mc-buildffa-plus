package us.rjks.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import us.rjks.game.Main;
import us.rjks.utils.Config;
import us.rjks.utils.Messages;

import javax.swing.text.html.parser.Entity;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 15.05.2021 / 17:53
 *
 **************************************************************************/

public class Damage implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            if(!Main.getGame().isSetup()) {
                Player player = (Player) event.getEntity();
                if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                    event.setCancelled(true);
                }
                if (!Config.getBoolean("allow-damage")) {
                    event.setDamage(0);
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player) {
            if(!Main.getGame().isSetup()) {
                Player player = (Player) event.getEntity();
                if(event.getDamager().getLocation().getY() >= (double)Main.getGame().getCurrentMap().getProperty("safeheight")) {
                    event.setCancelled(true);
                    event.getDamager().sendMessage(Messages.getString("player-is-in-spawn-protection").replaceAll("%player%", event.getEntity().getName()));
                }
                if (!Config.getBoolean("allow-damage")) {
                    event.setDamage(0);
                }
            }
        }
    }

}
