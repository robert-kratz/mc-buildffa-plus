package us.rjks.listener;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
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
import us.rjks.utils.Inventory;
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

            //Luck Block
            if(event.getClickedBlock().getType().equals(Material.valueOf(Config.getString("luck-block-type")))) {
                event.getClickedBlock().setType(Material.AIR);

                Block block = event.getClickedBlock();
                if(Config.getString("luck-block-open-sound").equalsIgnoreCase("none")) {
                    event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.valueOf(Config.getString("luck-block-open-sound")), 1, 1);
                }

                ItemStack stack = Config.getItemStackList("luck-block-items").get(new Random().nextInt(Config.getItemStackList("luck-block-items").size()));

                event.getPlayer().getInventory().addItem(stack);
                event.getPlayer().sendMessage(Messages.getString("player-gets-item").replaceAll("%amount%", stack.getAmount() + "").replaceAll("%type%", stack.getType().toString()));

                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        if(Bukkit.getWorlds().contains(block.getWorld())) {
                            block.setType(Material.valueOf(Config.getString("luck-block-type")));
                        }
                    }
                }, Config.getInteger("luck-block-duration-until-respawn") * 20);
            }
        }

        if (event.getPlayer().getItemInHand() != null) {
            if (event.getPlayer().getItemInHand().getType().equals(Material.FEATHER)) {
                Inventory.loadLobby(event.getPlayer());
            }
        }

    }

}
