package us.rjks.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 14.05.2021 / 12:43
 *
 **************************************************************************/

public class InventoryManager implements Listener {

    private String configname, displayname;
    private Integer rows, slots;
    private InventoryType type;
    private HashMap<ItemSchem, Integer> inventory = new HashMap<>();

    private static ArrayList<InventoryManager> cache = new ArrayList<>();

    public InventoryManager(String name, String configname, int rows, InventoryType type) {

        this.rows = rows;
        this.slots = rows * 9;
        this.type = type;
        this.configname = configname;
        this.displayname = name;
    }

    public void setItem(ItemSchem item, int slot, Action action) {
        inventory.put(item, slot);
    }

    /**
     * CREATING THE INVENTORY FOR THE USER
     * */
    public Inventory buildInventory(Player player, Replacement replacement) {
        Inventory inv = Bukkit.createInventory(null, slots, replacement.parseField(displayname));

        inventory.forEach(((itemSchem, slot) -> {
            ItemBuilder builder = new ItemBuilder(itemSchem.getType(), replacement.parseField(itemSchem.getDisplay()));
            builder.setDamage(itemSchem.getDamage());

            List<String> list = new ArrayList<>();
            itemSchem.getLore().forEach(item -> {
                list.add(replacement.parseField(item));
            });
            builder.setLore(list);

            ItemStack stack = builder.checkout();
            itemSchem.getEnchantments().forEach((enchantment, integer) -> {
                stack.addUnsafeEnchantment(enchantment, integer);
            });

            inv.setItem(slot, stack);
        }));
        return inv;
    }

    @EventHandler
    private void onInvClick(InventoryClickEvent event) {

    }

    public static void loadInvs() {
        try {
            JSONObject jsonObject = (JSONObject) readJsonSimpleDemo("plugin/inventory.json");
            JSONArray inventories = (JSONArray) jsonObject.get("inventories");

            inventories.forEach(listinv -> {
                JSONObject in = (JSONObject) listinv;

                if(in.get("type").toString().equalsIgnoreCase("STATIC_INV")) {
                    in.get("name");
                    in.get("rows");
                    in.get("type");

                    JSONArray items = (JSONArray) in.get("items");

                    items.forEach(listitem -> {
                        JSONObject itemformat = (JSONObject) listitem;

                        JSONObject props = (JSONObject) itemformat.get("props");

                        JSONObject item = (JSONObject) itemformat.get("item");

                        item.get("damage");
                        item.get("type");

                        JSONObject itemmeta = (JSONObject) item.get("meta");

                        itemmeta.get("meta-type");
                        itemmeta.get("displayname");

                        JSONArray itemlore = (JSONArray) itemmeta.get("lore");

                        JSONArray itemenchants = (JSONArray) itemmeta.get("enchants");
                    });

                } else if(in.get("type").toString().equalsIgnoreCase("DYNAMIC_INV")) {
                    in.get("name");
                    in.get("rows");
                    in.get("type");

                    in.get("name");
                }

//                cache.add(new InventoryManager());
            });

        } catch (Exception e) {
            Bukkit.getLogger().log(Level.WARNING, "ERROR WHILE COMPILING inventory.json");
            e.printStackTrace();
        }
    }

    private static Object readJsonSimpleDemo(String filename) throws Exception {
        FileReader reader = new FileReader(filename);
        JSONParser jsonParser = new JSONParser();
        return jsonParser.parse(reader);
    }


    public enum InventoryType {
        DYNAMIC_INV,
        STATIC_INV
    }

    public static class Action {

        private ItemStack itemStack;
        private ArrayList<String> props = new ArrayList<>();

        public Action(ItemStack item) {
            itemStack = item;
        }

        public void addProperty(String string) {
            props.add(string);
        }

        public boolean hasProperty(String string) {
            return props.contains(string);
        }

        public ItemStack getItemStack() {
            return itemStack;
        }
    }

    public static class Replacement {

        private HashMap<String, Object> replacements = new HashMap<>();

        public Replacement() {

        }

        public void addReplacement(String replacement, Object replace) {
            replacements.put(replacement, replace);
        }

        public String parseField(String string) {
            String fin = string;
            for (String entry : replacements.keySet()) {
                fin.replaceAll(entry, replacements.get(entry).toString());
            }

            return string;
        }

        public boolean hasProperty(String string) {
            return replacements.containsKey(string);
        }
    }

    public static class ItemSchem {

        private short damage;
        private String display;
        private Material type;
        private HashMap<Enchantment, Integer> enchantments = new HashMap<>();
        private ArrayList<String> lore = new ArrayList<>();
        private Action property;

        public ItemSchem() {

        }

        public Action getProperty() {
            return property;
        }

        public HashMap<Enchantment, Integer> getEnchantments() {
            return enchantments;
        }

        public ArrayList<String> getLore() {
            return lore;
        }

        public Material getType() {
            return type;
        }

        public short getDamage() {
            return damage;
        }

        public String getDisplay() {
            return display;
        }

        public void setDamage(short damage) {
            this.damage = damage;
        }

        public void setDisplay(String display) {
            this.display = display;
        }

        public void addEnchantments(Enchantment enchantment, Integer value) {
            enchantments.put(enchantment, value);
        }

        public void addLore(String lor) {
            lore.add(lor);
        }

        public void setProperty(Action property) {
            this.property = property;
        }

        public void setType(Material type) {
            this.type = type;
        }
    }

    public static ArrayList<InventoryManager> getCache() {
        return cache;
    }

    public HashMap<ItemSchem, Integer> getInventory() {
        return inventory;
    }

    public Integer getRows() {
        return rows;
    }

    public Integer getSlots() {
        return slots;
    }

    public InventoryType getType() {
        return type;
    }

    public String getConfigname() {
        return configname;
    }

    public String getDisplayname() {
        return displayname;
    }
}
