package us.rjks.db;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import us.rjks.game.Main;
import us.rjks.utils.Config;
import us.rjks.utils.KitManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 18.05.2021 / 14:35
 *
 **************************************************************************/

public class Sort {

    private static String uuidrow = Config.getString("sort.uuid-row"), datarow = Config.getString("sort.data-row"), table = Config.getString("sort.table");

    private HashMap<Player, HashMap<Integer, Integer>> cache = new HashMap<>();

    public void createUser(String uuid) {
        if(!Config.getBoolean("database")) return;

        Main.getGame().getMySQL().update("INSERT INTO " + table + "(" + uuidrow + ", " + datarow + ", date) VALUES ('"
                + uuid + "','"
                + getStandartFormat() + "','"
                + System.currentTimeMillis() + "')");
    }

    public static void deleteUser(String uuid) {
        if(Config.getBoolean("database")) {
            Main.getGame().getMySQL().update("DELETE FROM " + table + " WHERE " + uuidrow + "='" + uuid + "'");
        }
    }

    public boolean userExists(String uuid) {
        if(Config.getBoolean("database")) {
            ResultSet rs = Main.getGame().getMySQL().getResult("SELECT * FROM " + table + " WHERE " + uuidrow + "='" + uuid + "'");
            try {
                return rs.next();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public String getData(String uuid) {
        if(Config.getBoolean("database")) {
            ResultSet rs = Main.getGame().getMySQL().getResult("SELECT * FROM " + table + " WHERE " + uuidrow + "='" + uuid + "'");
            try {
                while (rs.next()) {
                    return rs.getString(datarow);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public HashMap<Integer, Integer> getInventorySort(String uuid) {
        if(Config.getBoolean("database")) {
            try {
                HashMap<Integer, Integer> map = new HashMap<>();
                JSONObject jo = null;

                if (getData(uuid) == null) {
                    jo = (JSONObject) new JSONParser().parse(getStandartFormat());
                } else {
                    jo = (JSONObject) new JSONParser().parse(getData(uuid));
                }

                for (int i = 0; i < 9; i++) {
                    if (jo.get("" + i) != null) {
                        long a = (long)jo.get("" + i);
                        map.put(i, Math.toIntExact(a));
                    }
                }
                return map;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public HashMap<Integer, ItemStack> getInventory(String uuid) {
        if(Config.getBoolean("database")) {
            HashMap<Integer, ItemStack> ret = new HashMap<>();

            HashMap<Integer, ItemStack> stack =  KitManager.getKitFromName("startInv").getHotbarItems();
            Main.getGame().getSort().getInventorySort(uuid).forEach((integer, integer2) -> {
                ret.put(integer, stack.get(integer2));
            });
            return ret;
        }
        return null;
    }

    public void setInventorySort(String uuid, HashMap<Integer, Integer> sort) {
        if(Config.getBoolean("database")) {
            JSONObject jsonObject = new JSONObject();
            sort.forEach((integer, integer2) -> {
                jsonObject.put(integer, integer2);
            });

            Main.getGame().getMySQL().update("UPDATE " + table + " SET " + datarow + "='" + jsonObject.toJSONString() + "' WHERE UUID='" + uuid + "'");
        }
    }

    public String getStandartFormat() {
        JSONObject jsonObject = new JSONObject();

        KitManager.getKitFromName("startInv").getHotbarItems().forEach((i, itemStack) -> {
            jsonObject.put(i, i);
        });

        return jsonObject.toJSONString();
    }
}
