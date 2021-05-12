package us.rjks.utils;

import org.apache.commons.io.FileUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import us.rjks.game.Main;

import java.io.File;
import java.util.ArrayList;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 11.05.2021 / 16:11
 *
 **************************************************************************/

public class Messages {

    private static File logs = new File("plugins/" + Main.getPlugin().getName() + "/", "messages.yml");
    private static YamlConfiguration locscfg = YamlConfiguration.loadConfiguration(logs);

    public static String getString(String name) {
        try {
            return String.valueOf(ChatColor.translateAlternateColorCodes('&', locscfg.get(name).toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean getBoolean(String name) {
        try {
            return locscfg.getBoolean(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Integer getInteger(String name) {
        try {
            return locscfg.getInt(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static ArrayList<String> getStringList(String name) {
        try {
            ArrayList<String> list = new ArrayList<>();
            locscfg.getStringList(name).forEach(line -> {
                list.add(ChatColor.translateAlternateColorCodes('&', line));
            });

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean create() {
        if(!logs.exists()) {
            try {
                FileUtils.copyInputStreamToFile(Main.getPlugin().getResource("messages.yml"), new File("plugins/" + Main.getPlugin().getName() + "/", "messages.yml"));
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
