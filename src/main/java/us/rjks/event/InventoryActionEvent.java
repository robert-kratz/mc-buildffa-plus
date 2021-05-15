package us.rjks.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import us.rjks.utils.InventoryManager;
import us.rjks.utils.Type;

import java.util.ArrayList;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 14.05.2021 / 16:20
 *
 **************************************************************************/

public class InventoryActionEvent extends Event {

    private InventoryClickEvent event;
    private InventoryManager inventoryManager;
    private static final HandlerList handlers = new HandlerList();

    public InventoryActionEvent() {

    }

    public InventoryActionEvent(InventoryClickEvent event, InventoryManager inventoryManager) {
        this.event = event;
        this.inventoryManager = inventoryManager;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public InventoryClickEvent getEvent() {
        return event;
    }

    @Override
    public String getEventName() {
        return super.getEventName();
    }
}
