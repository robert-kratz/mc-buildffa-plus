package us.rjks.utils;

import org.apache.commons.io.FileUtils;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import us.rjks.game.Main;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 19.05.2021 / 19:02
 *
 **************************************************************************/

public class Perks {

    private static File logs = new File("plugins/" + Main.getPlugin().getName() + "/", "shop.json");
    private static JSONObject locscfg = null;
    public static ArrayList<Perk> cache = new ArrayList<>();
    public static ArrayList<Category> cacheCategory = new ArrayList<>();

    public static Perk getPerkFromName(String name) {
        for (Perk perk : cache) {
            if (perk.getName().equalsIgnoreCase(name)) {
                return perk;
            }
        }
        return null;
    }

    public static ArrayList<Perk> getCategoryItems(String name) {
        ArrayList<Perk> perks = new ArrayList<>();
        for (Perk perk : cache) {
            if (perk.getCategory().equalsIgnoreCase(name)) {
                perks.add(perk);
            }
        }
        return perks;
    }

    public static ArrayList<Category> getCategorys() {
        return cacheCategory;
    }

    public static Material formatInventory(String uuid, Material material) {
        if (!Config.getBoolean("database")) return material;

        for (Category category : Perks.getCategorys()) {
            Perk perks = getPerkFromName(Main.getGame().getShop().getSelectedPerk(uuid, category.getConfig()));
            if (perks.getReplacement() != null) {
                if (material.equals(perks.getReplacement())) {
                    return perks.getType();
                }
            }
        }
        return material;
    }

    public static Category getCategoryByName(String name) {
        for (Category category : cacheCategory) {
            if (category.getName().equalsIgnoreCase(name) || category.getName().equalsIgnoreCase(name)) {
                return category;
            }
        }
        return null;
    }

    public static Perk getDefaultPerk(String category) {
        for (Perk perk : cache) {
            if (perk.getCategory().equalsIgnoreCase(category) && perk.isDefault()) {
                return perk;
            }
        }
        return null;
    }

    public static void loadPerks() {
        try {
            locscfg = (JSONObject) new JSONParser().parse(new FileReader("plugins/" + Main.getPlugin().getName() + "/shop.json"));
        } catch (Exception e) {}

        JSONArray objects = (JSONArray) locscfg.get("items");

        objects.forEach(jsonObject -> {
            JSONObject jo = (JSONObject) jsonObject;
            cache.add(new Perk(jo));
            System.out.println("[PERK] Loaded Perk " + jo.get("name").toString());
        });

        JSONArray categorys = (JSONArray) locscfg.get("categorys");

        categorys.forEach(jsonObject -> {
            JSONObject jo = (JSONObject) jsonObject;
            cacheCategory.add(new Category(jo));
            System.out.println("[CAT] Loaded category " + jo.get("config").toString());
        });
    }

    public static class Category {

        private String name, config, displayname;
        private Material type;
        private Integer damage;

        public Category(JSONObject object) {
            this.name = object.get("name").toString();
            this.config = object.get("config").toString();
            this.displayname = object.get("displayname").toString();

            JSONObject item = (JSONObject) object.get("item");
            this.type = Material.valueOf(item.get("type").toString());
            this.damage = Math.toIntExact((long)item.get("damage"));
        }

        public String getName() {
            return name;
        }

        public String getDisplayname() {
            return displayname;
        }

        public Material getType() {
            return type;
        }

        public Integer getDamage() {
            return damage;
        }

        public String getConfig() {
            return config;
        }
    }

    public static class Perk {

        private String name, ingame, category;
        private Material replacement, type;
        private Integer price, damage;
        private boolean alreadyBought = false, defaul = false;
        private HashMap<String, Object> data = new HashMap<>();

        public Perk(JSONObject object) {
            if (object == null) return;

            this.name = object.get("name").toString();
            this.ingame = object.get("ingame").toString();
            this.category = object.get("category").toString();

            this.price = Math.toIntExact((long) object.get("price"));

            JSONObject item = (JSONObject) object.get("item");

            this.damage = Math.toIntExact((long) item.get("damage"));
            this.type = Material.valueOf(item.get("type").toString());

            if (object.get("replace") != null) {
                try {
                    this.replacement = Material.valueOf(object.get("replace").toString());
                } catch (IllegalArgumentException e) {
                    //NOT SET
                }
            }

            JSONObject data = (JSONObject) object.get("data");

            if (data.get("already-bought") != null) {
                this.alreadyBought = (boolean)data.get("already-bought");
            }
            if (data.get("default") != null) {
                this.defaul = (boolean)data.get("default");
            }

            data.forEach((o, o2) -> {
                this.data.put(o.toString(), o2);
            });
        }

        public boolean playerHasPerk(String uuid) {
            if (alreadyBought) return true;
            return Main.getGame().getShop().hasItem(uuid, name);
        }

        public boolean selectPerk(String uuid) {
            if (!playerHasPerk(uuid)) return false;

            //TODO: SELECT PERK IN DATABASE

            return true;
        }

        public Object getDataValue(String key) {
            return data.get(key);
        }

        public HashMap<String, Object> getData() {
            return data;
        }

        public Material getType() {
            return type;
        }

        public Integer getDamage() {
            return damage;
        }

        public Integer getPrice() {
            return price;
        }

        public Material getReplacement() {
            return replacement;
        }

        public String getCategory() {
            return category;
        }

        public String getIngame() {
            return ingame;
        }

        public String getName() {
            return name;
        }

        public boolean isAlreadyBought() {
            return alreadyBought;
        }

        public boolean isDefault() {
            return defaul;
        }
    }

    public static boolean create() {
        if(!logs.exists()) {
            try {
                FileUtils.copyInputStreamToFile(Main.getPlugin().getResource("shop.json"), new File("plugins/" + Main.getPlugin().getName() + "/", "shop.json"));
                return true;
            }
            catch (Exception localException) {}
        }
        return false;
    }

}
