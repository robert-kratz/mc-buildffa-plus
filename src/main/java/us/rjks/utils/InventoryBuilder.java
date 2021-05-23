package us.rjks.utils;

import org.apache.commons.io.FileUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import us.rjks.game.Main;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 23.05.2021 / 12:46
 *
 **************************************************************************/

public class InventoryBuilder {

    private static File logs = new File("plugins/" + Main.getPlugin().getName() + "/", "inventory.json");
    private static JSONObject locscfg = null;
    public static ArrayList<Inventory> cache = new ArrayList<>();

    public static void loadInventorys() {
        try {
            locscfg = (JSONObject) new JSONParser().parse(new FileReader("plugins/" + Main.getPlugin().getName() + "/inventory.json"));
        } catch (Exception e) {}
        JSONArray objects = (JSONArray) locscfg.get("inventorys");
        objects.forEach(o -> {
            JSONObject inv = (JSONObject) o;
            Inventory inventory = new Inventory(inv);
            cache.add(inventory);
            System.out.println("[INV] Loaded Inventory " + inventory.getName());
        });

    }

    public static Inventory getInventoryByName(String name) {
        for (Inventory inventory : cache) {
            if (inventory.getName().equalsIgnoreCase(name) || inventory.getDisplayname().equalsIgnoreCase(name)) {
                return inventory;
            }
        }
        return null;
    }

    public static class Inventory {

        private String name, displayname;
        private Integer rows;
        private ArrayList<Data> data = new ArrayList<>();
        private ArrayList<Templates> templates = new ArrayList<>();
        private ArrayList<Distribution> distribution = new ArrayList<>();
        private HashMap<String, Object> properties = new HashMap<>();

        public Inventory() {}

        public Inventory(JSONObject object) {
            if (object == null) return;
            this.name = object.get("name").toString();
            this.displayname = object.get("displayname").toString();
            this.rows = Math.toIntExact((long)object.get("rows"));

            //DATA
            JSONObject data = (JSONObject) object.get("data");
            data.forEach((o, o2) -> {
                this.data.add(new Data(o.toString(), o2));
            });

            JSONArray templates = (JSONArray) object.get("templates");
            templates.forEach(o -> {
                JSONObject tem = (JSONObject) o;
                this.templates.add(new Templates(tem));
            });

            JSONArray distribution = (JSONArray) object.get("distribution");
            distribution.forEach(o -> {
                JSONObject dis = (JSONObject) o;
                Distribution distribution1 = new Distribution(dis.get("name").toString());
                JSONArray num = (JSONArray) dis.get("range");
                num.forEach(o1 -> {
                    distribution1.addSlot(Math.toIntExact((long)o1));
                });
                this.distribution.add(distribution1);
            });
        }

        public Templates getTemplateFromName(String name) {
            for (Templates template : templates) {
                if (template.getName().equalsIgnoreCase(name)) {
                    return template;
                }
            }
            return null;
        }

        public Distribution getDistributionFromName(String name) {
            for (Distribution distribution : distribution) {
                if (distribution.getName().equalsIgnoreCase(name)) {
                    return distribution;
                }
            }
            return null;
        }

        public void addPropertie(String props, Object value) {
            properties.put(props, value);
        }

        public Object getPropertie(String props) {
            return properties.get(props);
        }

        public String getDisplayname() {
            return ChatColor.translateAlternateColorCodes('&', displayname);
        }

        public String getName() {
            return name;
        }

        public Integer getRows() {
            return rows;
        }

        public ArrayList<Data> getData() {
            return data;
        }

        public ArrayList<Distribution> getDistribution() {
            return distribution;
        }

        public ArrayList<Templates> getTemplates() {
            return templates;
        }
    }

    public static class Data {

        private String name;
        private Object value;

        public Data(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValueString() {
            return value.toString();
        }

        public Boolean getValueBoolean() {
            return (boolean)value;
        }

        public Integer getValueInteger() {
            return (int)value;
        }
    }

    public static class Templates {

        private String name, tag;
        private ItemFormat itemFormat;

        public Templates(String name, String tag) {
            this.name = name;
            this.tag = tag;
        }

        public Templates(JSONObject jsonObject) {
            this.name = jsonObject.get("name").toString();
            this.tag = jsonObject.get("tag").toString();
            this.itemFormat = new ItemFormat(jsonObject);
        }

        public ItemFormat getItemFormat() {
            return itemFormat;
        }

        public void addReplacement(String key, Object value) {
            itemFormat.addReplacement(key, value);
        }

        public ItemStack getItemStack() {
            if (itemFormat == null) return null;
            ItemBuilder itemBuilder = new ItemBuilder(itemFormat.getType(), itemFormat.getDisplayname());

            itemBuilder.setLore(itemFormat.getLore());
            itemBuilder.setDamage((short)itemFormat.getDamage().shortValue());
            itemFormat.getData().forEach((s, o) -> {
                switch (s) {
                    case "hide-enchantment":
                        itemBuilder.addItemFlag(ItemFlag.HIDE_ENCHANTS);
                        break;
                }
            });
            ItemStack stack = itemBuilder.checkout();
            itemFormat.getEnchantments().forEach((enchantment, integer) -> {
                System.out.println(enchantment);
                System.out.println(integer);
                stack.addUnsafeEnchantment(enchantment, integer);
            });
            return stack;

        }

        public String getName() {
            return name;
        }

        public String getTag() {
            return tag;
        }
    }

    public static class Distribution {

        private ArrayList<Integer> range = new ArrayList<>();
        private String name;

        public Distribution(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void addSlot(int slot) {
            range.add(slot);
        }

        public ArrayList<Integer> getRange() {
            return range;
        }
    }

    public static class ItemFormat {

        private String displayname;
        private Material type;
        private Integer damage;
        private ArrayList<String> lore = new ArrayList<>();
        private HashMap<Enchantment, Integer> enchantments = new HashMap<>();
        private HashMap<String, Object> replacememts = new HashMap<>();
        private HashMap<String, Object> data = new HashMap<>();

        public ItemFormat() {}

        public ItemFormat(JSONObject object) {
            if (object == null) return;

            JSONObject item = (JSONObject) object.get("item");

            //PLACEHOLDER, IF TAG IS SET TO none or other, it will be replace later on
            try {
                this.displayname = object.get("displayname").toString();
                this.type = Material.valueOf(item.get("type").toString());
                this.damage = Math.toIntExact((long)item.get("damage"));
            } catch (Exception e) {

            }

            //LORES
            JSONArray lores = (JSONArray) item.get("lore");
            for (Object o : lores) {
                lore.add(o.toString());
            }

            //ENCHANTS
            JSONArray enchants = (JSONArray) item.get("entchantments");
            for (Object o : enchants) {
                JSONObject ent = (JSONObject) o;
                enchantments.put(Enchantment.getByName(ent.get("name").toString()), Math.toIntExact((long) ent.get("value")));
            }

            //DATA
            JSONObject data = (JSONObject) item.get("data");
            data.forEach((o, o2) -> {
                this.data.put(o.toString(), o2);
            });
        }

        public Integer getDamage() {
            return damage;
        }

        public Material getType() {
            return type;
        }

        public String getDisplayname() {
            String replacement = displayname;
            for (String rep : replacememts.keySet()) {
                replacement = replacement.replaceAll(rep, Matcher.quoteReplacement(replacememts.get(rep) + ""));
            }
            return ChatColor.translateAlternateColorCodes('&', replacement);
        }

        public ArrayList<String> getLore() {
            ArrayList<String> replacement = new ArrayList<>();
            for (String s : lore) {
                String repa = s;
                for (String rep : replacememts.keySet()) {
                    repa = repa.replaceAll(rep, replacememts.get(rep) + "");
                }
                replacement.add(ChatColor.translateAlternateColorCodes('&', repa));
            }
            return replacement;
        }

        public HashMap<Enchantment, Integer> getEnchantments() {
            return enchantments;
        }

        public HashMap<String, Object> getData() {
            return data;
        }

        public HashMap<String, Object> getReplacememts() {
            return replacememts;
        }

        public void addReplacement(String key, Object value) {
            replacememts.put(key, value);
        }

        public void setType(Material type) {
            this.type = type;
        }

        public void setLore(ArrayList<String> lore) {
            this.lore = lore;
        }

        public void setEnchantments(HashMap<Enchantment, Integer> enchantments) {
            this.enchantments = enchantments;
        }

        public void setDamage(Integer damage) {
            this.damage = damage;
        }

        public void setData(HashMap<String, Object> data) {
            this.data = data;
        }

        public void setDisplayname(String displayname) {
            this.displayname = displayname;
        }

        public void setReplacememts(HashMap<String, Object> replacememts) {
            this.replacememts = replacememts;
        }
    }

    public static boolean create() {
        if(!logs.exists()) {
            try {
                FileUtils.copyInputStreamToFile(Main.getPlugin().getResource("inventory.json"), new File("plugins/" + Main.getPlugin().getName() + "/", "inventory.json"));
                return true;
            }
            catch (Exception localException) {}
        }
        return false;
    }

}
