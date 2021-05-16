package us.rjks.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.rjks.game.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 25.04.2021 / 16:28
 *
 **************************************************************************/

public class KitManager {

    private static File logs = new File("plugins/" + Main.getPlugin().getName() + "/", "kit.yml");
    private static YamlConfiguration locscfg = YamlConfiguration.loadConfiguration(logs);

    private static ArrayList<Kit> kits = new ArrayList<>();

    public static void saveKit(String name, Player target, ItemStack icon, String description) {
        locscfg.set(name + ".icon", icon);
        locscfg.set(name + ".description", description);
        locscfg.set(name + ".hp", target.getMaxHealth());
        for(int i = 0 ; i < target.getInventory().getSize() ; i++) {
            if(target.getInventory().getItem(i) != null) {
                locscfg.set(name + "." + i, target.getInventory().getItem(i));
            }
        }
        for(int i = 0 ; i < target.getInventory().getArmorContents().length ; i++) {
            String pos = "";
            switch (i) {
                case 0: pos = "b"; break;
                case 1: pos = "l"; break;
                case 2: pos = "c"; break;
                case 3: pos = "h"; break;
            }
            locscfg.set(name + "." + pos, target.getInventory().getArmorContents()[i]);
        }
        save();
        kits.add(new Kit(name));
    }

    public static ArrayList<Kit> getKits() {
        return kits;
    }

    public static Kit getKitFromName(String name) {
        for(Kit kit : getKits()) {
            if(kit.getName().equalsIgnoreCase(name)) return kit;
        }
        return null;
    }

    public static void loadKits() {
        for(String tops : locscfg.getConfigurationSection("").getKeys(false)) {
            System.out.println("[KIT] Loaded Kit " + tops);
            kits.add(new Kit(tops));
        }
    }

    public static class Kit {

        private String description, name;
        private ItemStack icon;
        private double hp;

        private HashMap<String, ItemStack> inv = new HashMap<>();

        public Kit(String name) {
            this.name = name;
            for(String tops : locscfg.getConfigurationSection(name).getKeys(true)) {
                if(tops.equals("icon")) {this.icon = (ItemStack) locscfg.get(name + ".icon");continue;}
                if(tops.equals("description")) {this.description = locscfg.get(name + ".description").toString();continue;}
                if(tops.equals("hp")) {this.hp = locscfg.getDouble(name + ".hp");continue;}
                inv.put(tops, (ItemStack) locscfg.get(name + "." + tops));
            }
        }

        public void setKit(Player target) {
            target.getInventory().clear();
            target.setMaxHealth(hp);
            target.getInventory().setHeldItemSlot(0);
            for (String id : inv.keySet()) {
                if(id.equals("h")) {target.getInventory().setHelmet(inv.get(id)); continue;}
                if(id.equals("l")) {target.getInventory().setLeggings(inv.get(id)); continue;}
                if(id.equals("c")) {target.getInventory().setChestplate(inv.get(id)); continue;}
                if(id.equals("b")) {target.getInventory().setBoots(inv.get(id)); continue;}

                target.getInventory().setItem(Integer.parseInt(id), inv.get(id));
            }
        }

        public void deleteKit() {
            kits.remove(this);
            locscfg.set(getName(), null);
            save();
        }

        public String getDescription() {
            return description;
        }

        public ItemStack getIcon() {
            return icon;
        }

        public String getName() {
            return name;
        }

        public double getHp() {
            return hp;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setHp(double hp) {
            this.hp = hp;
        }

        public void setIcon(ItemStack icon) {
            this.icon = icon;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setInv(HashMap<String, ItemStack> inv) {
            this.inv = inv;
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
