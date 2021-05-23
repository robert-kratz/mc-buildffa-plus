package us.rjks.db;

import us.rjks.game.Main;
import us.rjks.utils.Config;
import us.rjks.utils.Perks;

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

    //SHOP ITEM ROW

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

    //SELECTED ROW

    private String uuid = Config.getString("selected.uuid-row"), category = Config.getString("selected.category-row"), data = Config.getString("selected.data-row"), tablename = Config.getString("selected.table");

    public String getSelectedPerk(String uuid, String category) {
        if(Config.getBoolean("database")) {
            ResultSet rs = Main.getGame().getMySQL().getResult("SELECT * FROM " + this.tablename + " WHERE " + this.uuid + "='" + uuid + "' AND " + this.category + "='" + category + "'");
            try {
                while (rs.next()) {
                    return rs.getString(data);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return Perks.getDefaultPerk(category).getName();
    }

    public void unSelectCategory(String uuid, String category) {
        if(Config.getBoolean("database")) {
            Main.getGame().getMySQL().update("DELETE FROM " + tablename + " WHERE " + this.uuid + "='" + uuid + "' AND " + this.category + "='" + category + "'");
        }
    }

    public void unSelectItem(String uuid, String category) {
        if(Config.getBoolean("database")) {
            Main.getGame().getMySQL().update("DELETE FROM " + tablename + " WHERE " + this.uuid + "='" + uuid + "' AND " + this.category + "='" + category + "'");
        }
    }

    public void selectItem(String uuid, String category, String item) {
        if(Config.getBoolean("database")) {
            Main.getGame().getMySQL().update("INSERT INTO " + tablename + "(" + uuid + ", " + category + ", " + data + ", date) VALUES ('"
                    + uuid + "','"
                    + category + ","
                    + item + "','"
                    + System.currentTimeMillis() + "')");
        }
    }

    public boolean hasSelected(String uuid, String category, String item) {
        if(Config.getBoolean("database")) {
            ResultSet rs = Main.getGame().getMySQL().getResult("SELECT * FROM " + this.tablename + " WHERE " + this.uuid + "='" + uuid + "' AND " + this.category + "='" + category + "' AND " + this.data + "='" + item + "'");
            try {
                return rs.next();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
