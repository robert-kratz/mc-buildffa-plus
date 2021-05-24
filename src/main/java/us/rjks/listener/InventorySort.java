package us.rjks.listener;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import us.rjks.db.Sort;
import us.rjks.game.Main;
import us.rjks.utils.Config;
import us.rjks.utils.KitManager;
import us.rjks.utils.Messages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 18.05.2021 / 18:22
 *
 **************************************************************************/

public class InventorySort implements Listener {

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        if (event.getInventory().getName().equalsIgnoreCase(Config.getString("inventory-sort-inventory-name"))) {
            if (KitManager.getKitFromName("startInv") == null) return;

            //CHECK IF ALL ITEMS ARE CONTAINED
            ArrayList<ItemStack> list = new ArrayList<ItemStack>(Arrays.asList(event.getInventory().getContents()));
            HashMap<Integer, ItemStack> kit = KitManager.getKitFromName("startInv").getHotbarItems();
            for (int integer : kit.keySet()) {
                if (!list.contains(kit.get(integer))) {
                    event.getPlayer().sendMessage(Messages.getString("player-saves-inventory-error"));
                    Main.getGame().getInventory().loadInvSelect((Player) event.getPlayer());
                    ((Player) event.getPlayer()).playSound(event.getPlayer().getLocation(), Sound.valueOf(Messages.getString("player-saves-inventory-error-sound")), 1, 1);
                    return;
                }
            }
            HashMap<Integer, ItemStack> fin = KitManager.getKitFromName("startInv").getHotbarItems();
            HashMap<Integer, Integer> fina = new HashMap<>();

            ArrayList<Integer> doubbled = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                for (Integer integer : fin.keySet()) {
                    if (!doubbled.contains(integer) && event.getInventory().getItem(i) != null && fin.get(integer) != null && event.getInventory().getItem(i).equals(fin.get(integer))) {
                        fina.put(i, integer);
                        doubbled.add(integer);
                    }
                }
            }

            if (Main.getGame().getSort().getInventorySort(event.getPlayer().getUniqueId().toString()) != fina) {
                Main.getGame().getSort().setInventorySort(event.getPlayer().getUniqueId().toString(), fina);
            }
            ((Player) event.getPlayer()).playSound(event.getPlayer().getLocation(), Sound.valueOf(Messages.getString("player-saves-inventory-sound")), 1, 1);
            event.getPlayer().sendMessage(Messages.getString("player-saves-inventory"));
            Main.getGame().getInventory().loadInvSelect((Player) event.getPlayer());

            if (Main.getGame().getSort().getInventorySort(event.getPlayer().getUniqueId().toString()) != fina) {
                Main.getGame().getSort().setInventorySort(event.getPlayer().getUniqueId().toString(), fina);
            }
        }
    }
}
