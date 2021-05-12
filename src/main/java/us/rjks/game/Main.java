package us.rjks.game;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import us.rjks.cmd.Map;
import us.rjks.db.MySQL;
import us.rjks.listener.Join;
import us.rjks.utils.*;

import java.awt.*;
import java.io.File;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 25.04.2021 / 16:27
 *
 **************************************************************************/

public class Main extends JavaPlugin {

    private static Main instance;
    private static GameManager game;

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;

        game = new GameManager();

        try {
            //CREATE PROJECT FOLDER
            if(!getDataFolder().exists()) getDataFolder().mkdirs();
            if(!new File("plugins/" + getName() + "/maps").exists()) new File("plugins/" + getName() + "/maps").mkdirs();

            Config.create();
            Messages.create();
            MapManager.create();
            TabList.create();

            TabList.loadTabllist();
            MapManager.loadMaps();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!Config.getBoolean("enabled")) {
            getPluginLoader().disablePlugin(getPlugin());
            Bukkit.getConsoleSender().sendMessage("§c§lATTENTION: YOU MAY HAVE TO RELOAD YOUR SERVER");
            Bukkit.getConsoleSender().sendMessage("§c§lATTENTION: PLUGIN HAS BEEN DISABLED BECAUSE OF THE CONFIGURATION, CHECK 'config.yml' > enable: false");
            return;
        }

        loadListeners();

        if(MapManager.getSetUpMap().size() == 0) {
            System.out.println("§c§lATTENTION: THERE ARE NOT SETUP MAPS, PLUGIN IS SETUP MODE");
            game.setSetup(true);
        } else {
            MapManager.Map map = MapManager.getRandomMap();
            map.loadMap();
            game.setCurrentMap(map);
            System.out.println("[MAP] Loaded " + map.getName() + " as default");

            Bukkit.getOnlinePlayers().forEach(player -> {
                TabList.setTabList(player);
            });
        }

        if (Config.getBoolean("enable-rank-system") && Config.getBoolean("enable-tab-rank")) {
            if(TabList.getDefaultRank() != null) {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    TabList.setTabList(player);
                });
            } else {
                System.out.println("§c§lATTENTION: YOU HAVE NOT DEFINED ANY DEFAULT RANK");
            }
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(getPlugin(), new Runnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    try {
                        if (Config.getBoolean("enable-rank-system") && Config.getBoolean("display-effect-on-player")) {
                            if (player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SURVIVAL)) {
                                Color color = Color.decode("#" + TabList.getRankByPlayer(player).getHex());
                                Location location = player.getLocation();
                                location.setY(location.getY() + 2);

                                if (player.isSneaking() && Config.getBoolean("display-effect-on-top-while-sneaking")) {
                                    player.getWorld().spigot().playEffect(location, Effect.COLOURED_DUST, 0, 0, (float) color.getRed()/255, (float) color.getGreen()/255, (float) color.getBlue()/255, 1, 0, 300);
                                } else {
                                    player.getWorld().spigot().playEffect(player.getLocation(), Effect.COLOURED_DUST, 0, 0, (float) color.getRed()/255, (float) color.getGreen()/255, (float) color.getBlue()/255, 1, 0, 300);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }, 1L, 1L);

    }

    @Override
    public void onDisable() {
        super.onDisable();

        for (MapManager.Map maps : MapManager.getMaps()) {
            if (maps.isLoaded()) {
                maps.unloadMap();
                maps.deleteMap();
            }
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    public void loadListeners() {
        getCommand("map").setExecutor(new Map());

        Bukkit.getPluginManager().registerEvents(new Join(), this);
    }

    public static Main getPlugin() {
        return instance;
    }

    public static Main getInstance() {
        return instance;
    }
}
