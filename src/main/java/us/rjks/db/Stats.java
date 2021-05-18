package us.rjks.db;

import us.rjks.game.Main;
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

    private String uuidrow = Config.getString("stats.uuid-row"), killsrow = Config.getString("stats.kills-row"), deathsrow = Config.getString("stats.deaths-row"), table = Config.getString("stats.table");

    public void createUser(String uuid) {
        if(Config.getBoolean("database")) {
            Main.getGame().getMySQL().update("INSERT INTO " + table + "(" + uuidrow + ", " + killsrow + ", " + deathsrow + ", date) VALUES ('"
                    + uuid + "','"
                    + 0 + "','"
                    + 0 + "','"
                    + System.currentTimeMillis() + "')");
        }
    }

    public void deleteUser(String uuid) {
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

    public int getDeaths(String uuid) {
        if(Config.getBoolean("database")) {
            ResultSet rs = Main.getGame().getMySQL().getResult("SELECT * FROM " + table + " WHERE " + uuidrow + "='" + uuid + "'");
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

    public int getKills(String uuid) {
        if(Config.getBoolean("database")) {
            ResultSet rs = Main.getGame().getMySQL().getResult("SELECT * FROM " + table + " WHERE " + uuidrow + "='" + uuid + "'");
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

    public void setDeaths(String uuid, int amount) {
        if(Config.getBoolean("database")) {
            Main.getGame().getMySQL().update("UPDATE " + table + " SET " + deathsrow + "='" + amount + "' WHERE UUID='" + uuid + "'");
        }
    }

    public void setKills(String uuid, int amount) {
        if(Config.getBoolean("database")) {
            Main.getGame().getMySQL().update("UPDATE " + table + " SET " + killsrow + "='" + amount + "' WHERE UUID='" + uuid + "'");
        }
    }

    public void addDeaths(String uuid, int amount) {
        if(Config.getBoolean("database")) {
            setDeaths(uuid, getDeaths(uuid) + amount);
        }
    }

    public void addKills(String uuid, int amount) {
        if(Config.getBoolean("database")) {
            setKills(uuid, getKills(uuid) + amount);
        }
    }

    public int getRank(String uuid) {
        if(Config.getBoolean("database")) {
            ResultSet rs = Main.getGame().getMySQL().getResult("SELECT * FROM " + table + " ORDER BY (" + killsrow + ")");
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
