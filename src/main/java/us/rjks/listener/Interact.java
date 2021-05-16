package us.rjks.listener;

import org.bukkit.Bukkit;
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
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {

            //NORMAL CHEST
            if(event.getClickedBlock().getType().equals(Material.valueOf(Config.getString("regular-chest-type")))) {
                event.getClickedBlock().setType(Material.AIR);

                Block block = event.getClickedBlock();
                if(Config.getString("regular-chest-open-sound").equalsIgnoreCase("none")) {
                    event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.valueOf(Config.getString("regular-chest-open-sound")), 1, 1);
                }

                ItemStack stack = Config.getItemStackList("regular-items").get(new Random().nextInt(Config.getItemStackList("regular-items").size()));

                event.getPlayer().getInventory().addItem(stack);
                event.getPlayer().sendMessage(Messages.getString("player-gets-item").replaceAll("%amount%", stack.getAmount() + "").replaceAll("%type%", stack.getType().toString()));

                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        if(Bukkit.getWorlds().contains(block.getWorld())) {
                            block.setType(Material.valueOf(Config.getString("regular-chest-type")));
                        }
                    }
                }, Config.getInteger("regular-chest-duration-until-respawn") * 20);
            }

            //LEGENDARY CHEST
            if(event.getClickedBlock().getType().equals(Material.valueOf(Config.getString("legendary-chest-type")))) {
                event.getClickedBlock().setType(Material.AIR);

                Block block = event.getClickedBlock();
                if(Config.getString("legendary-chest-open-sound").equalsIgnoreCase("none")) {
                    event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.valueOf(Config.getString("legendary-chest-open-sound")), 1, 1);
                }

                ItemStack stack = Config.getItemStackList("legendary-items").get(new Random().nextInt(Config.getItemStackList("legendary-items").size()));

                event.getPlayer().getInventory().addItem(stack);
                event.getPlayer().sendMessage(Messages.getString("player-gets-item").replaceAll("%amount%", stack.getAmount() + "").replaceAll("%type%", stack.getType().toString()));


                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        if(Bukkit.getWorlds().contains(block.getWorld())) {
                            block.setType(Material.valueOf(Config.getString("legendary-chest-type")));
                        }
                    }
                }, Config.getInteger("legendary-chest-duration-until-respawn") * 20);
            }

            //SPECIAL CHEST
            if(event.getClickedBlock().getType().equals(Material.valueOf(Config.getString("special-chest-type")))) {
                event.getClickedBlock().setType(Material.AIR);

                Block block = event.getClickedBlock();
                if(Config.getString("special-chest-open-sound").equalsIgnoreCase("none")) {
                    event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.valueOf(Config.getString("special-chest-open-sound")), 1, 1);
                }

                PotionEffect effect = Config.getPotionEffectList("special-effects").get(new Random().nextInt(Config.getPotionEffectList("special-effects").size()));
                event.getPlayer().addPotionEffect(effect);
                event.getPlayer().sendMessage(Messages.getString("player-gets-effect").replaceAll("%effect%", effect.getType().getName().toString() + "").replaceAll("%duration%", effect.getDuration() / 20 + ""));

                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        if(Bukkit.getWorlds().contains(block.getWorld())) {
                            block.setType(Material.valueOf(Config.getString("special-chest-type")));
                        }
                    }
                }, Config.getInteger("special-chest-duration-until-respawn") * 20);
            }
        }

    }

}
