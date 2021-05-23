package us.rjks.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import us.rjks.db.Shop;
import us.rjks.game.Main;
import us.rjks.utils.Config;
import us.rjks.utils.Inventory;
import us.rjks.utils.InventoryBuilder;
import us.rjks.utils.Perks;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 23.05.2021 / 16:22
 *
 **************************************************************************/

public class PerkInventory implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!Config.getBoolean("database")) return;

        if (event.getCurrentItem() == null) event.getWhoClicked().closeInventory();

        if (InventoryBuilder.getInventoryByName(event.getInventory().getTitle()) != null) {
            event.setCancelled(true);

            try {
                InventoryBuilder.Inventory inventory = InventoryBuilder.getInventoryByName(event.getInventory().getTitle());

                InventoryBuilder.Distribution category = inventory.getDistributionFromName("category-item");

                int a = 0;
                for (Integer integer : category.getRange()) {
                    if (integer == event.getSlot()) {
                        Main.getGame().getInventory().loadPerksBlock((Player) event.getWhoClicked(), Perks.getCategorys().get(a).getConfig());
                    }
                    a++;
                }

                InventoryBuilder.Distribution perks = inventory.getDistributionFromName("perk-item");

                int b = 0;
                for (Integer integer : perks.getRange()) {
                    if (integer == event.getSlot()) {
                        String cate = "";
                        for (Perks.Category s : Perks.getCategorys()) {
                            System.out.println(inventory.getName());
                            System.out.println(s.getConfig().toLowerCase());
                            if (inventory.getName().contains(s.getConfig().toLowerCase())) {
                                cate = s.getConfig();
                            }
                        }
                        if (cate.isEmpty()) return;
                        Perks.Perk perk = Perks.getCategoryItems(cate).get(b);
                        Shop shop = Main.getGame().getShop();
                        String uuid = event.getWhoClicked().getUniqueId().toString();

                        if (shop.hasItem(uuid, perk.getName()) && !shop.hasSelected(uuid, cate, perk.getName())) {
                            shop.unSelectCategory(uuid, cate);
                            shop.selectItem(uuid, cate, perk.getName());
                            event.getWhoClicked().sendMessage("You selected " + perk.getIngame());
                            Main.getGame().getInventory().loadPerksBlock((Player) event.getWhoClicked(), cate);
                        } else if (!shop.hasItem(uuid, perk.getName())) {
                            if (Main.getGame().getCoins().getCoins(uuid) >= perk.getPrice()) {
                                shop.addItem(uuid, perk.getName());
                                Main.getGame().getCoins().removeCoins(uuid, perk.getPrice());
                                shop.unSelectCategory(uuid, cate);
                                shop.selectItem(uuid, cate, perk.getName());
                                event.getWhoClicked().sendMessage("You successfully bought " + perk.getIngame());
                                Main.getGame().getInventory().loadPerksBlock((Player) event.getWhoClicked(), cate);
                            } else {
                                event.getWhoClicked().sendMessage("You dont have enough coins");
                                event.getWhoClicked().closeInventory();
                            }
                        } else {
                            Main.getGame().getInventory().loadPerksBlock((Player) event.getWhoClicked(), cate);
                        }


                    }
                    b++;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                //ITEM NOT SET
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
