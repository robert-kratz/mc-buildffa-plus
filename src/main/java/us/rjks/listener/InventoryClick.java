package us.rjks.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import us.rjks.utils.Config;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 24.05.2021 / 15:35
 *
 **************************************************************************/

public class InventoryClick implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;

        if (event.getClickedInventory().getType().equals(InventoryType.PLAYER) && !Config.getBoolean("allow-item-move-in-inventory")){
            event.setCancelled(true);
        }

        if(event.getAction() == InventoryAction.HOTBAR_SWAP){
            event.setCancelled(true);
        }

        if (event.getCurrentItem().equals(Config.getItemStack("item-inventory-sort"))
                || event.getCurrentItem().equals(Config.getItemStack("item-inventory-sort"))) {
            event.setCancelled(true);
        }
    }

}
