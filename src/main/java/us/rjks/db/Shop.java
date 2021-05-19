package us.rjks.db;

import us.rjks.game.Main;
import us.rjks.utils.Config;

import java.sql.ResultSet;
import java.sql.SQLException;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 16.05.2021 / 19:30
 *
 **************************************************************************/

public class Shop {

    private String uuidrow = Config.getString("shop.uuid-row"), itemrow = Config.getString("shop.item-row"), table = Config.getString("shop.table");

    public void addItem(String uuid, String item) {
        if(Config.getBoolean("database")) {
            Main.getGame().getMySQL().update("INSERT INTO " + table + "(" + uuidrow + ", " + itemrow + ", date) VALUES ('"
                    + uuid + "','"
                    + item + "','"
                    + System.currentTimeMillis() + "')");
        }
    }

    public void removeItem(String uuid, String item) {
        if(Config.getBoolean("database")) {
            Main.getGame().getMySQL().update("DELETE FROM " + table + " WHERE " + uuidrow + "='" + uuid + "' AND " + item + "'" + item + "'");
        }
    }

    public boolean hasItem(String uuid, String item) {
        if(Config.getBoolean("database")) {
            ResultSet rs = Main.getGame().getMySQL().getResult("SELECT * FROM " + table + " WHERE " + uuidrow + "='" + uuid + "' AND " + itemrow + "='" + item + "'");
            try {
                return rs.next();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
