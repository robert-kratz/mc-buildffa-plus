package us.rjks.listener;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

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
    public void onPlace(BlockPlaceEvent event) {
        if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if(!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            event.setCancelled(true);
        }
    }
}
