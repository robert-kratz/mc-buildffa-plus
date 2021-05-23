package us.rjks.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
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

    public void loadPerks(Player player, String category) {
        org.bukkit.inventory.Inventory inv = Bukkit.createInventory(null, 9*3, "§ePerks §8- §e" + category);

        for (int i = 0; i < 9; i++) {
            inv.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE, " ").setDamage((short) 15).checkout());
        }
        for (int i = 18; i < 27; i++) {
            inv.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE, " ").setDamage((short) 15).checkout());
        }

        int i = 9;
        String defauult = Main.getGame().getShop().getSelectedPerk(player.getUniqueId().toString(), category);
        for (Perks.Perk categoryItem : Perks.getCategoryItems(category)) {
            if (categoryItem.playerHasPerk(player.getUniqueId().toString())) {
                ItemStack stack = new ItemBuilder(categoryItem.getType(), "§8➤ §e" + categoryItem.getIngame()).setLore("§7  You already bought this item").addItemFlag(ItemFlag.HIDE_ENCHANTS).checkout();
                if (defauult.equalsIgnoreCase(categoryItem.getName())) {
                    stack.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
                }
                inv.setItem(i, stack);
            } else {
                ItemStack stack = new ItemBuilder(categoryItem.getType(), "§8➤ §e" + categoryItem.getIngame()).setLore("§7  Price: §6" + categoryItem.getPrice()).checkout();
                if (defauult.equalsIgnoreCase(categoryItem.getName())) {
                    stack.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
                }
                inv.setItem(i, stack);
            }
            i++;
        }
        player.openInventory(inv);
    }

    private void transformGUISize(int i) {
        int a = i;
    }

}
