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
        player.getInventory().setHeldItemSlot(3);
        player.getInventory().setItem(3, new ItemBuilder(Material.FEATHER, "§8➤ §6§lItem selection").checkout());
        player.getInventory().setItem(5, new ItemBuilder(Material.CHEST, "§8➤ §6§lPerks").checkout());
    }

    public void loadLobby(Player player) {
        org.bukkit.inventory.Inventory inv = Bukkit.createInventory(null, 9, Messages.getString("inventory-sort-gui-name"));
        if (KitManager.getKitFromName("startInv") == null) return;
        if (!Config.getBoolean("database")) return;

        Main.getGame().getSort().getInventory(player.getUniqueId().toString()).forEach((integer, itemStack) -> {
            inv.setItem(integer, itemStack);
        });

        player.openInventory(inv);
    }

}
