package us.rjks.utils;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import us.rjks.game.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 18.04.2021 / 19:52
 *
 **************************************************************************/

public class Config {

    private static File logs = new File("plugins/" + Main.getPlugin().getName() + "/", "config.yml");
    private static YamlConfiguration locscfg = YamlConfiguration.loadConfiguration(logs);

    public static String getString(String name) {
        try {
            return ChatColor.translateAlternateColorCodes('&', locscfg.getString(name));
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

    public static Double getDouble(String name) {
        try {
            return locscfg.getDouble(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public static List<Object> getList(String name) {
        try {
            return (List<Object> ) locscfg.get(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ItemStack getItemStack(String name) {
        try {
            return (ItemStack) locscfg.get(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<ItemStack> getItemStackList(String name) {
        try {
            ArrayList<ItemStack> stacks = new ArrayList<>();

            ((ArrayList<ItemStack>)locscfg.get(name)).forEach(itemStack -> {
                String display = "";
                if(itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName() != null) {
                    display = itemStack.getItemMeta().getDisplayName();
                }
                ItemBuilder itemBuilder = new ItemBuilder(itemStack.getType(), ChatColor.translateAlternateColorCodes('&', display));
                stacks.add(itemBuilder.checkout());
            });
            return stacks;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<PotionEffect> getPotionEffectList(String name) {
        try {
            return new ArrayList<PotionEffect>((ArrayList<PotionEffect>)locscfg.get(name));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String> getStringList(String name) {
        try {
            return new ArrayList<>(locscfg.getStringList(name));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Integer> getIntegerList(String name) {
        try {
            return new ArrayList<Integer>(locscfg.getIntegerList(name));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean create() {
        if(!logs.exists()) {
            try {
                FileUtils.copyInputStreamToFile(Main.getPlugin().getResource("config.yml"), new File("plugins/" + Main.getPlugin().getName() + "/", "config.yml"));
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
