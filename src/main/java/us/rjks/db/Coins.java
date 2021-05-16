package us.rjks.db;

import org.bukkit.entity.Player;
import us.rjks.utils.Config;

import java.sql.ResultSet;
import java.sql.SQLException;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 13.05.2021 / 13:33
 *
 **************************************************************************/

public class Coins {

    private static String uuidrow = Config.getString("coins.uuid-row"), coinsrow = Config.getString("coins.coins-row"), table = Config.getString("coins.table");

    public static void createUser(String uuid) {
        if(Config.getBoolean("database")) {
            MySQL.update("INSERT INTO " + table + "(" + uuidrow + ", " + coinsrow + ", date) VALUES ('"
                    + uuid + "','"
                    + 0 + "','"
                    + System.currentTimeMillis() + "')");
        }
    }

    public static void deleteUser(String uuid) {
        if(Config.getBoolean("database")) {
            MySQL.update("DELETE FROM " + table + " WHERE " + uuidrow + "='" + uuid + "'");
        }
    }

    public static boolean userExists(String uuid) {
        if(Config.getBoolean("database")) {
            ResultSet rs = MySQL.getResult("SELECT * FROM " + table + " WHERE " + uuidrow + "='" + uuid + "'");
            try {
                return rs.next();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static int getCoins(String uuid) {
        if(Config.getBoolean("database")) {
            ResultSet rs = MySQL.getResult("SELECT * FROM " + table + " WHERE " + uuidrow + "='" + uuid + "'");
            try {
                while (rs.next()) {
                    return rs.getInt(coinsrow);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static void addCoins(String uuid, int amount) {
        if(Config.getBoolean("database")) {
            setCoins(uuid, getCoins(uuid) + amount);
        }
    }

    public static void removeCoins(String uuid, int amount) {
        if(Config.getBoolean("database")) {
            if((getCoins(uuid) - amount) >= 0) {
                setCoins(uuid, getCoins(uuid) - amount);
            }
        }
    }

    public static void setCoins(String uuid, int amount) {
        if(Config.getBoolean("database")) {
            MySQL.update("UPDATE " + table + " SET " + coinsrow + "='" + amount + "' WHERE UUID='" + uuid + "'");
        }
    }
}
