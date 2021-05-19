package us.rjks.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.rjks.db.MySQL;
import us.rjks.game.Main;

import java.util.HashMap;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 16.05.2021 / 17:34
 *
 **************************************************************************/

public class Inventory {

    public void loadInvSelect(Player player) {
        player.getInventory().clear();
        player.getInventory().setHeldItemSlot(Config.getInteger("inventory-sort-slot"));

        if (Config.getBoolean("enable-item-sort")) {
            player.getInventory().setItem(Config.getInteger("inventory-sort-slot"), Config.getItemStack("item-inventory-sort"));
        }
        if (Config.getBoolean("enable-perks-sort")) {
            player.getInventory().setItem(Config.getInteger("perks-slot"), Config.getItemStack("item-perks"));
        }
    }

    public void loadLobby(Player player) {
        org.bukkit.inventory.Inventory inv = Bukkit.createInventory(null, 9, Config.getString("inventory-sort-inventory-name"));
        if (KitManager.getKitFromName("startInv") == null) return;
        if (!Config.getBoolean("database")) return;

        Main.getGame().getSort().getInventory(player.getUniqueId().toString()).forEach((integer, itemStack) -> {
            inv.setItem(integer, itemStack);
        });

        player.openInventory(inv);
    }

    private void loadPerks(Player player) {
        org.bukkit.inventory.Inventory inv = Bukkit.createInventory(null, 9, Messages.getString("inventory-perks-name"));
    }

}
