package us.rjks.listener;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import us.rjks.game.Main;
import us.rjks.utils.Config;
import us.rjks.utils.Inventory;
import us.rjks.utils.Messages;

import java.util.Random;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 15.05.2021 / 16:06
 *
 **************************************************************************/

public class Interact implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        if (event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType().equals(Material.BANNER)) {
            event.setCancelled(true);
        }

        if (!Main.getGame().getIngame().contains(event.getPlayer())) {
            if (event.getPlayer().getItemInHand() != null) {
                if (event.getPlayer().getItemInHand().getType().equals(Material.FEATHER)) {
                    Main.getGame().getInventory().loadLobby(event.getPlayer());
                }
                if (event.getPlayer().getItemInHand().getType().equals(Material.CHEST)) {
                    Main.getGame().getInventory().loadPerksBlock(event.getPlayer(), "STICK");
                }
            }
        }

    }

}
