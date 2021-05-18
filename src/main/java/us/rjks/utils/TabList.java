package us.rjks.utils;

import com.avaje.ebean.LogLevel;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import us.rjks.game.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 25.04.2021 / 16:27
 *
 **************************************************************************/

public class TabList {

    private static File logs = new File("plugins/" + Main.getPlugin().getName() + "/", "tablist.yml");
    private static YamlConfiguration locscfg = YamlConfiguration.loadConfiguration(logs);

    public static HashMap<Player, Rank> cache = new HashMap<>();

    private static Scoreboard scoreboard;
    private static ArrayList<Rank> ranks = new ArrayList<Rank>();

    public static void createNewRank(String name, String permission, String color, String tabprefix, String tabsuffix, String chatformat, String hex, Integer power, boolean defaultRank) {
        locscfg.set(name + ".permission", permission);
        locscfg.set(name + ".color", color);
        locscfg.set(name + ".tabprefix", tabprefix);
        locscfg.set(name + ".tabsuffix", tabsuffix);
        locscfg.set(name + ".chatformat", chatformat);
        locscfg.set(name + ".hex", hex);
        locscfg.set(name + ".defaultRank", defaultRank);

        save();

        new Rank(name, permission, color, tabprefix, tabsuffix, chatformat, hex, power, defaultRank);
    }

    public static Rank getDefaultRank() {
        for (Rank all : ranks) {
            if (all.isDefaultRank()) {
                return all;
            }
        }
        return null;
    }

    public static Rank getRankByName(String name) {
        for (Rank rank : ranks) {
            if (rank.getName().equalsIgnoreCase(name)) {
                System.out.println(name);
                return rank;
            }
        }

        return getDefaultRank();
    }

    public static Rank getRankByPlayer(Player player) {
        if(cache.containsKey(player)) {
            return cache.get(player);
        }
        System.out.println();
        if(getDefaultRank() == null) {
            Main.getPlugin().getLogger().log(Level.WARNING, "THERE IS NOT DEFAULT RANK SET, LOCATE THE tablist.yml AND SET THE DEFAULT ROLE TO default > true");
        }
        return getDefaultRank();
    }

    public static void loadTablist() {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        for (String rank : locscfg.getConfigurationSection("").getKeys(false)) {
            ranks.add(new Rank(rank));
        }

        for (Rank rank : ranks) {
            System.out.println("[RANK] Loaded rank " + rank.getName());
            Team team = scoreboard.registerNewTeam((100000 - rank.getPower()) + rank.getName());
            team.setPrefix(ChatColor.translateAlternateColorCodes('&', rank.getTabprefix()));
            team.setSuffix(ChatColor.translateAlternateColorCodes('&', rank.getTabsuffix()));
        }
    }

    public static synchronized void setTabList(Player player) {

        Rank team = null;

        for (Rank rank : ranks) {
            if (player.hasPermission(rank.getPermission())) {
                team = rank;
            }
        }

        if(getDefaultRank() != null) {
            if(team == null) team = getDefaultRank();

            scoreboard.getTeam((100000 - team.getPower()) + team.getName()).addEntry(player.getName());
            player.setDisplayName(scoreboard.getTeam((100000 - team.getPower()) + team.getName()).getPrefix() + player.getName() + "§r");

            cache.remove(player);
            cache.put(player, team);
            for (Player all : Bukkit.getOnlinePlayers()) {
                all.setScoreboard(scoreboard);
            }
        } else {
            Main.getPlugin().getLogger().log(Level.WARNING, "THERE IS NOT DEFAULT RANK SET, LOCATE THE tablist.yml AND SET THE DEFAULT ROLE TO default > true");
        }
    }

    public static class Rank {

        private String name, permission, color, tabprefix, tabsuffix, chatformat, hex;
        private boolean defaultRank;
        private Integer power;

        public Rank(String name) {
            this.name = name;
            this.permission = locscfg.getString(name + ".permission");
            this.color = locscfg.getString(name + ".color");
            this.tabprefix = locscfg.getString(name + ".tabprefix");
            this.tabsuffix = locscfg.getString(name + ".tabsuffix");
            this.chatformat = locscfg.getString(name + ".chatformat");
            this.hex = locscfg.getString(name + ".hex");
            this.power = locscfg.getInt(name + ".power");
            this.defaultRank = locscfg.getBoolean(name + ".defaultRank");
        }

        public Rank(String name, String permission, String color, String tabprefix, String tabsuffix, String chatformat, String hex, Integer power, boolean defaultRank) {
            this.name = name;
            this.permission = permission;
            this.color = color;
            this.tabprefix = tabprefix;
            this.tabsuffix = tabsuffix;
            this.chatformat = chatformat;
            this.defaultRank = defaultRank;
            this.hex = hex;
            this.power = power;

            ranks.add(this);
        }

        public String getHex() {
            return hex;
        }

        public String getName() {
            return name;
        }

        public String getChatformat() {
            return ChatColor.translateAlternateColorCodes('&', chatformat);
        }

        public String getColor() {
            return color;
        }

        public String getPermission() {
            return permission;
        }

        public String getTabprefix() {
            return ChatColor.translateAlternateColorCodes('&', tabprefix);
        }

        public String getTabsuffix() {
            return ChatColor.translateAlternateColorCodes('&', tabsuffix);
        }

        public Integer getPower() {
            return power;
        }

        public boolean isDefaultRank() {
            return defaultRank;
        }
    }

    public static boolean create() {
        if(!logs.exists()) {
            try {
                FileUtils.copyInputStreamToFile(Main.getPlugin().getResource("tablist.yml"), new File("plugins/" + Main.getPlugin().getName() + "/", "tablist.yml"));
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
