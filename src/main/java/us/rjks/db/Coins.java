package us.rjks.db;

import org.bukkit.entity.Player;
import us.rjks.game.Main;
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

    private String uuidrow = Config.getString("coins.uuid-row"), coinsrow = Config.getString("coins.coins-row"), table = Config.getString("coins.table");

    public void createUser(String uuid) {
        if(Config.getBoolean("database")) {
            Main.getGame().getMySQL().update("INSERT INTO " + table + "(" + uuidrow + ", " + coinsrow + ", date) VALUES ('"
                    + uuid + "','"
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

    public int getCoins(String uuid) {
        if(Config.getBoolean("database")) {
            ResultSet rs = Main.getGame().getMySQL().getResult("SELECT * FROM " + table + " WHERE " + uuidrow + "='" + uuid + "'");
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

    public void addCoins(String uuid, int amount) {
        if(Config.getBoolean("database")) {
            setCoins(uuid, getCoins(uuid) + amount);
        }
    }

    public void removeCoins(String uuid, int amount) {
        if(Config.getBoolean("database")) {
            if((getCoins(uuid) - amount) >= 0) {
                setCoins(uuid, getCoins(uuid) - amount);
            }
        }
    }

    public void setCoins(String uuid, int amount) {
        if(Config.getBoolean("database")) {
            Main.getGame().getMySQL().update("UPDATE " + table + " SET " + coinsrow + "='" + amount + "' WHERE UUID='" + uuid + "'");
        }
    }
}
