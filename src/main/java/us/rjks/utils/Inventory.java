package us.rjks.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 16.05.2021 / 17:34
 *
 **************************************************************************/

public class Inventory {

    public static void loadInvSelect(Player player) {
        player.getInventory().setHeldItemSlot(3);
        player.getInventory().setItem(3, new ItemBuilder(Material.FEATHER, "§8➤ §6§lItem selection").checkout());
        player.getInventory().setItem(5, new ItemBuilder(Material.CHEST, "§8➤ §6§lPerks").checkout());
    }

    public static void loadLobby(Player player) {
        org.bukkit.inventory.Inventory inv = Bukkit.createInventory(null, 9, "&6&lInventory");
        KitManager.getKitFromName("startInv").getInv().forEach((s, itemStack) -> {
            try {
                if (Integer.parseInt(s) >= 0 && Integer.parseInt(s) < 9) {
                    inv.setItem(Integer.parseInt(s), itemStack);
                }
            } catch (Exception e) {

            }
        });
        player.openInventory(inv);
    }

}
