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

import java.util.ArrayList;
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

    public void loadGameInv(Player player) {
        KitManager.getKitFromName("startInv").setKit(player);
        player.getInventory().clear();

        Main.getGame().getSort().getInventory(player.getUniqueId().toString()).forEach((integer, itemStack) -> {
            ItemStack stack = new ItemStack(itemStack);
            stack.setType(Perks.formatInventory(player.getUniqueId().toString(), itemStack.getType()));
            player.getInventory().setItem(integer, stack);
        });
    }

    public void loadPerksBlock(Player player, String cat) {
        if (!Config.getBoolean("database")) return;
        if (InventoryBuilder.getInventoryByName("perks-inventory-" + cat) == null) return;

        InventoryBuilder.Inventory inventory = InventoryBuilder.getInventoryByName("perks-inventory-" + cat);

        org.bukkit.inventory.Inventory inv = Bukkit.createInventory(null, 9*inventory.getRows(), inventory.getDisplayname());

        InventoryBuilder.Distribution placeholder = inventory.getDistributionFromName("placeholder-item");
        for (Integer integer : placeholder.getRange()) {
            inv.setItem(integer, inventory.getTemplateFromName(placeholder.getName()).getItemStack());
        }

        InventoryBuilder.Distribution category = inventory.getDistributionFromName("category-item");
        int i = 0;
        for (Integer integer : category.getRange()) {
            try {
                InventoryBuilder.Templates templates = inventory.getTemplateFromName("category-item-unselected");
                if (Perks.getCategorys().get(i).getConfig().equalsIgnoreCase(cat)) {
                    templates = inventory.getTemplateFromName("category-item-selected");
                }

                templates.getItemFormat().setType(Perks.getCategorys().get(i).getType());
                templates.getItemFormat().setDamage(Perks.getCategorys().get(i).getDamage());
                templates.getItemFormat().setDisplayname(Perks.getCategorys().get(i).getDisplayname());

                inv.setItem(integer, templates.getItemStack());
                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        InventoryBuilder.Distribution perks = inventory.getDistributionFromName("perk-item");
        ArrayList<Perks.Perk> perk = Perks.getCategoryItems(cat);
        int a = 0;
        for (Integer integer : perks.getRange()) {
            try {
                InventoryBuilder.Templates templates = inventory.getTemplateFromName("perk-item-unbought");

                if (Main.getGame().getShop().hasItem(player.getUniqueId().toString(), perk.get(a).getName())) {
                    if (Main.getGame().getShop().getSelectedPerk(player.getUniqueId().toString(), cat).equalsIgnoreCase(perk.get(a).getName())) {
                        //SELECTED AND BOUGHT
                        templates = inventory.getTemplateFromName("perk-item-bought-selected");
                    } else {
                        //BOUGHT BUT NOT SELECTED
                        templates = inventory.getTemplateFromName("perk-item-bought");
                    }
                }

                //FORMAT ITEM
                templates.getItemFormat().setType(perk.get(a).getType());
                templates.getItemFormat().setDamage(perk.get(a).getDamage());
                templates.getItemFormat().addReplacement("%price%", perk.get(a).getPrice());
                templates.getItemFormat().addReplacement("%perk-name%", perk.get(a).getIngame());

                inv.setItem(integer, templates.getItemStack());
                a++;
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }

        player.openInventory(inv);
    }

    private void transformGUISize(int i) {
        int a = i;
    }

}
