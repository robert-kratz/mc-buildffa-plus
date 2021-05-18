package us.rjks.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import us.rjks.game.Main;
import us.rjks.utils.Config;

import javax.annotation.Nonnull;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 15.05.2021 / 17:13
 *
 **************************************************************************/

public class Build implements Listener {

    @EventHandler
    public void onBlockEvent(@Nonnull BlockFromToEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(@Nonnull WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityExplodeEvent(@Nonnull EntityExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityExplodeEvent(@Nonnull EntitySpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onLeavesDecayEvent(@Nonnull LeavesDecayEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onLeavesDecayEvent(@Nonnull BlockGrowEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onAchievment(@Nonnull PlayerAchievementAwardedEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onFood(@Nonnull FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(@Nonnull BlockPlaceEvent event) {
        if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            if(event.getBlock().getY() < (double)Main.getGame().getCurrentMap().getProperty("safeheight")) {
                event.setCancelled(false);
                Block block = event.getBlock();
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        if(Bukkit.getWorlds().contains(block.getWorld())) {
                            block.setType(Material.AIR);
                        }
                    }
                }, Config.getInteger("block-disapear-after") * 20);
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            event.setCancelled(true);
        }
    }
}
