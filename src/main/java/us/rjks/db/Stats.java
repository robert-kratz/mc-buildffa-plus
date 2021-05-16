package us.rjks.db;

import us.rjks.utils.Config;

import java.sql.ResultSet;
import java.sql.SQLException;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 25.04.2021 / 16:29
 *
 **************************************************************************/

public class Stats {

    private static String uuidrow = Config.getString("stats.uuid-row"), killsrow = Config.getString("stats.kills-row"), deathsrow = Config.getString("stats.deaths-row"), table = Config.getString("stats.table");

    public static void createUser(String uuid) {
        if(Config.getBoolean("database")) {
            MySQL.update("INSERT INTO " + table + "(" + uuidrow + ", " + killsrow + ", " + deathsrow + ", date) VALUES ('"
                    + uuid + "','"
                    + 0 + "','"
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

    public static int getDeaths(String uuid) {
        if(Config.getBoolean("database")) {
            ResultSet rs = MySQL.getResult("SELECT * FROM " + table + " WHERE " + uuidrow + "='" + uuid + "'");
            try {
                while (rs.next()) {
                    return rs.getInt(deathsrow);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static int getKills(String uuid) {
        if(Config.getBoolean("database")) {
            ResultSet rs = MySQL.getResult("SELECT * FROM " + table + " WHERE " + uuidrow + "='" + uuid + "'");
            try {
                while (rs.next()) {
                    return rs.getInt(killsrow);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static void setDeaths(String uuid, int amount) {
        if(Config.getBoolean("database")) {
            MySQL.update("UPDATE " + table + " SET " + deathsrow + "='" + amount + "' WHERE UUID='" + uuid + "'");
        }
    }

    public static void setKills(String uuid, int amount) {
        if(Config.getBoolean("database")) {
            MySQL.update("UPDATE " + table + " SET " + killsrow + "='" + amount + "' WHERE UUID='" + uuid + "'");
        }
    }

    public static void addDeaths(String uuid, int amount) {
        if(Config.getBoolean("database")) {
            setDeaths(uuid, getDeaths(uuid) + amount);
        }
    }

    public static void addKills(String uuid, int amount) {
        if(Config.getBoolean("database")) {
            setKills(uuid, getKills(uuid) + amount);
        }
    }

    public static int getRank(String uuid) {
        if(Config.getBoolean("database")) {
            ResultSet rs = MySQL.getResult("SELECT * FROM " + table + " ORDER BY (" + killsrow + ")");
            int i = 1;
            try {
                while (rs.next()) {
                    if(rs.getString(uuidrow).equalsIgnoreCase(uuid)) {
                        return i;
                    }
                    i++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return -1;
        }
        return -1;
    }

}
