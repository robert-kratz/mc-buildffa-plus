package us.rjks.utils;

import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.rjks.game.GameManager;
import us.rjks.game.Main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 17.04.2021 / 16:00
 *
 **************************************************************************/

public class MapManager {

    private static File logs = new File("plugins/" + Main.getPlugin().getName() + "/", "maps.yml");
    private static YamlConfiguration locscfg = YamlConfiguration.loadConfiguration(logs);

    private static ArrayList<Map> maps = new ArrayList<Map>();

    public static boolean createMap(String src, String name, ItemStack icon, String builder, String description) {

        File file = new File("plugins/" + Main.getPlugin().getName() + "/maps/" + src);

        if(!file.exists()) return false;

        locscfg.set(src + ".name", name);
        locscfg.set(src + ".icon", icon);
        locscfg.set(src + ".builder", builder);
        locscfg.set(src + ".description", builder);
        save();

        maps.add(new Map(name, src, builder, description, icon));
        return true;
    }

    public static ArrayList<Map> getMaps() {
        return maps;
    }

    public static Map getMapFromName(String name) {
        for(Map map : getMaps()) {
            if(map.getName().equalsIgnoreCase(name)) return map;
        }
        return null;
    }

    public static Map getMapFromSrcName(String name) {
        for(Map map : getMaps()) {
            if(map.getSrc().equalsIgnoreCase(name)) return map;
        }
        return null;
    }

    public static boolean nameExists(String name) {
        for (Map maps : MapManager.getMaps()) {
            if(maps.getName().equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    public static boolean srcNameExists(String name) {
        for (Map maps : MapManager.getMaps()) {
            if(maps.getSrc().equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    public static void loadMaps() {
        for(String tops : locscfg.getConfigurationSection("").getKeys(false)) {
            System.out.println("[Map] Loaded Map " + tops);
            maps.add(new Map(locscfg.get(tops + ".name").toString(), tops, locscfg.get(tops + ".builder").toString(), locscfg.get(tops + ".description").toString(), (ItemStack) locscfg.get(tops + ".icon")));
        }
    }

    /**
     * TO MY FUTURE ME: HERE YOU CAN DEFINE THE PROPERTIES FOR A SETUP MAP
     * THIS COULD BE INCLUDING SPAWNPOINTS, COLLECTION OF SPAWNPOINTS OR PROPS
     * failed to find this snipped counter: 1
     * */
    public static ArrayList<Map> getSetUpMap() {
        ArrayList<Map> m = new ArrayList<>();
        maps.forEach(map -> {
            if(map.getAmountOfSpawnCollection("spawn") > 0) {
                m.add(map);
            }
        });
        return m;
    }

    public static Map getRandomMap() {
        ArrayList<Map> map = getSetUpMap();
        return map.get(new Random().nextInt(map.size()));
    }

    public static class Map {

        private String name, src, author, description;
        private ItemStack icon;
        private boolean loaded = false;
        private HashMap<String, Object> property = new HashMap<String, Object>();

        public Map(String name, String src, String author, String description, ItemStack icon) {
            this.name = name;
            this.src = src;
            this.author = author;
            this.description = description;
            this.icon = icon;

            for (World worlds : Bukkit.getWorlds()) {
                if(worlds.getName().equalsIgnoreCase(name)) {
                    loaded = true;
                    break;
                }
            }
        }

        public void setTmpProperty(String key, Object value) {
            property.put(key, value);
        }

        public Object getTmpProperty(String key) {
            return property.get(key);
        }

        public boolean tmpPropertyExists(String key) {
            return (property.get(key) != null);
        }

        public void tmpPropertyRemove(String key) {
            property.remove(key);
            return;
        }

        public Object getProperty(String name) {
            return locscfg.get(getName() + ".property." + name);
        }

        public void setProperty(String name, Object object) {
            locscfg.set(getName() + ".property." + name, object);
            save();
        }

        /**
         * SPAWN COLLECTION
         * */
        public int getAmountOfSpawnCollection(String name) {
            int i = 0;
            try {
                for (String elements : locscfg.getConfigurationSection(getName() + ".spawn").getKeys(false)) {
                    if (elements.startsWith(name)) {
                        i++;
                    }
                }
            } catch (Exception e) {}
            return i;
        }

        public void setLocationCollection(Location location, String name) {
            int amount = getAmountOfSpawnCollection(name);

            locscfg.set(getName() + ".spawn." + name + "-" + amount + ".x", Double.valueOf(location.getX()));
            locscfg.set(getName() + ".spawn." + name + "-" + amount +  ".y", Double.valueOf(location.getY()));
            locscfg.set(getName() + ".spawn." + name + "-" + amount +  ".z", Double.valueOf(location.getZ()));
            locscfg.set(getName() + ".spawn." + name + "-" + amount +  ".yaw", Float.valueOf(location.getYaw()));
            locscfg.set(getName() + ".spawn." + name + "-" + amount +  ".pitch", Float.valueOf(location.getPitch()));
            locscfg.set(getName() + ".spawn." + name + "-" + amount +  ".world", getSrc());

            save();
        }

        public ArrayList<Location> getLocationCollection(String name) {
            ArrayList<Location> locations = new ArrayList<>();
            for (int i = 0; i < getAmountOfSpawnCollection(name); i++) {
                locations.add(getLocation(name + "-" + i));
            }
            return locations;
        }

        public Location getRandomLocationCollection(String name) {
            ArrayList<Location> locations = getLocationCollection(name);
            return locations.get(new Random().nextInt(locations.size()));
        }

        public void teleportPlayerToRandomLocationCollection(Player player, String name) {
            ArrayList<Location> locations = getLocationCollection(name);
            Location location = locations.get(new Random().nextInt(locations.size()));
            player.teleport(location);

            if(Config.getBoolean("enable-spawn-protection")) {
                Main.getGame().spawnProtection.remove(player);
                Main.getGame().spawnProtection.add(player);
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        Main.getGame().spawnProtection.remove(player);
                        player.sendMessage(Messages.getString("player-spawnprotection-ends"));
                    }
                }, Config.getInteger("spawn-protection-duration") * 20);
            }
        }

        /**
         * STATIC SPAWN
         * */
        public void setLocation(Location location, String name) {
            locscfg.set(getName() + ".spawn." + name + ".x", Double.valueOf(location.getX()));
            locscfg.set(getName() + ".spawn." + name + ".y", Double.valueOf(location.getY()));
            locscfg.set(getName() + ".spawn." + name + ".z", Double.valueOf(location.getZ()));
            locscfg.set(getName() + ".spawn." + name + ".yaw", Float.valueOf(location.getYaw()));
            locscfg.set(getName() + ".spawn." + name + ".pitch", Float.valueOf(location.getPitch()));
            locscfg.set(getName() + ".spawn." + name + ".world", getSrc());

            save();
        }

        public Location getLocation(String name) {
            if (locscfg.get(getName() + ".spawn."  + name) != null) {
                Location loc = new Location(Bukkit.getWorld(getSrc()), 0, 0, 0);
                loc.setX(locscfg.getDouble(getName() + ".spawn."  + name + ".x"));
                loc.setY(locscfg.getDouble(getName() + ".spawn."  + name + ".y"));
                loc.setZ(locscfg.getDouble(getName() + ".spawn."  + name + ".z"));
                loc.setYaw((float)locscfg.getDouble(getName() + ".spawn." + name + ".yaw"));
                loc.setPitch((float)locscfg.getDouble(getName() + ".spawn." + name + ".pitch"));
                loc.setWorld(Bukkit.getWorld(locscfg.getString(getName() + ".spawn." + name + ".world")));
                return loc;
            } else {
                return null;
            }
        }

        public void deleteLocation(String name) {
            locscfg.set(getName() + ".spawn." + name, null);
        }

        /**
         * LOADED WORLD FROM CACHE
         * */
        public boolean loadMap() {
            if(!isLoaded()) {
                String source = "plugins/" + Main.getPlugin().getName() + "/maps/" + name;
                File srcDir = new File(source);

                if(!new File("plugins/../" + name).exists()) {
                    new File("plugins/../" + name).mkdirs();

                    String destination = "plugins/../" + name;
                    File destDir = new File(destination);

                    try {
                        FileUtils.copyDirectory(srcDir, destDir);
                        World world = new WorldCreator(name).createWorld();
                        setLoaded(true);
                        return true;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
            return false;
        }

        /*
         * UNLOADING WORLD FROM SERVER, DIRECTORY IS NOT BEING DELETED
         * */
        public boolean unloadMap() {
            if (isLoaded()) {
                try {
                    for (World world : Bukkit.getWorlds()) {
                        if(world.getName().equalsIgnoreCase(name)) {
                            for (Player all : world.getPlayers()) {
                                all.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
                            }
                            Bukkit.unloadWorld(world, true);
                            setLoaded(false);
                        }
                    }
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
            return false;
        }

        /*
         * DELETE WORLD FILE FROM ROOT DIRECTORY
         * */
        public boolean deleteMap() {
            try {
                Bukkit.unloadWorld(getName(), false);
                FileUtils.deleteDirectory(new File("plugins/../" + getName()));
                setLoaded(false);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        public boolean deleteMapFromConfig() {
            try {
                locscfg.set(name, null);
                save();
                loaded = false;
                maps.remove(this);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        public boolean resetMap() {
            unloadMap();
            deleteMap();
            loadMap();
            return true;
        }

        public boolean backUpMap(String backupWorld) {
            if(isLoaded() && !backupWorld.contains(".") && !backupWorld.contains("/")) {
                File srcDir = new File("plugins/../" + name);

                //SAVING WORLD BEFORE DELETING
                Bukkit.getWorld(name).save();

                if(!new File("plugins/" + Main.getPlugin().getName() + "/maps/" + backupWorld).exists()) {
                    new File("plugins/" + Main.getPlugin().getName() + "/maps/" + backupWorld).mkdirs();

                    File destDir = new File("plugins/" + Main.getPlugin().getName() + "/maps/" + backupWorld);

                    try {
                        FileUtils.copyDirectory(srcDir, destDir);
                        if(new File("plugins/" + Main.getPlugin().getName() + "/maps/" + backupWorld + "/uid.dat").delete());

                        createMap(backupWorld, backupWorld, new ItemBuilder(Material.SUGAR, backupWorld).checkout(), getAuthor(), "Copied from " + name + " on " + System.currentTimeMillis());
                        return true;
                    } catch (IOException e) {
                        return false;
                    }
                }
            }
            return false;
        }

        public boolean saveToCache() {
            if(isLoaded()) {
                File srcDir = new File("plugins/../" + name);

                //SAVING WORLD BEFORE DELETING
                Bukkit.getWorld(name).save();

                try {
                    FileUtils.deleteDirectory(new File("plugins/" + Main.getPlugin().getName() + "/maps/" + name));

                    if(!new File("plugins/" + Main.getPlugin().getName() + "/maps/" + name).exists()) {

                        new File("plugins/" + Main.getPlugin().getName() + "/maps/" + name).mkdirs();

                        File destDir = new File("plugins/" + Main.getPlugin().getName() + "/maps/" + name);

                        FileUtils.copyDirectory(srcDir, destDir);

                        if(new File("plugins/" + Main.getPlugin().getName() + "/maps/" + name + "/uid.dat").delete());

                        return true;
                    }
                } catch (Exception e) {
                    return false;
                }
            }
            return false;
        }

        public boolean teleportPlayerToWorld(Player player) {
            if(isLoaded()) {
                player.teleport(Bukkit.getWorld(name).getSpawnLocation());
                return true;
            } else {
                return false;
            }
        }

        public String getName() {
            return name;
        }

        public ItemStack getIcon() {
            return icon;
        }

        public String getAuthor() {
            return author;
        }

        public String getDescription() {
            return description;
        }

        public String getSrc() {
            return src;
        }

        public boolean isLoaded() {
            return loaded;
        }

        public void setLoaded(boolean loaded) {
            this.loaded = loaded;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setIcon(ItemStack icon) {
            this.icon = icon;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setSrc(String src) {
            this.src = src;
        }
    }

    public static class VotePlayer {

        private Player player;
        private String mapName;

        public VotePlayer(Player p, String map) {
            this.player = p;
            this.mapName = map;
        }

        public Player getPlayer() {
            return player;
        }

        public String getMapName() {
            return mapName;
        }
    }

    public static boolean create() {
        if(!logs.exists()) {
            try {
                logs.createNewFile();
                return true;
            }
            catch (Exception localException) {}
        }
        return false;
    }

    private static void save() {
        try { locscfg.save(logs); } catch (Exception e) {}
    }

}