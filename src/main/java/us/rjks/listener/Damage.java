package us.rjks.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import us.rjks.game.Main;
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
            Player player = (Player) event.getEntity();
            if(Main.getGame().spawnProtection.contains(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if(Main.getGame().spawnProtection.contains(player)) {
                event.setCancelled(true);
                event.getDamager().sendMessage(Messages.getString("player-is-in-spawn-protection").replaceAll("%player%", event.getEntity().getName()));
            }
        }
    }

}
