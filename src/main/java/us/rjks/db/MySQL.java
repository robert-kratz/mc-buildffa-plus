package us.rjks.db;

import us.rjks.utils.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 25.04.2021 / 16:29
 *
 **************************************************************************/

public class MySQL {

    private static String username = Config.getString("mysql.user");
    private static String password = Config.getString("mysql.password");
    private static String database = Config.getString("mysql.database");
    private static String host = Config.getString("mysql.host");
    private static String port = Config.getString("mysql.port");

    public static Connection con;

    public static void connect() {
        if(!isConnected()) {
            try {
                System.out.println("jdbc:mysql://" + host +":" + port + "/" + database + "?autoReconnect=true:" + username + ":" + password);
                con = DriverManager.getConnection("jdbc:mysql://" + host +":" + port + "/" + database + "?autoReconnect=true", username, password);
                System.out.println("[DB] Connected successfully to SQL Database");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void disconnect() {
        if(isConnected()) {
            try {
                con.close();
                System.out.println("[DB] Disconnected successfully to SQL Database");
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    public static boolean isConnected() {
        return con != null;
    }

    public static void createTable() {

        if(isConnected()) {
            try {
                con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS " + Config.getString("coins.table") + " (" + Config.getString("coins.uuid-row") + " VARCHAR(255), " + Config.getString("coins.coins-row") + " VARCHAR(255), date VARCHAR(255))");
                con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS " + Config.getString("stats.table") + " (" + Config.getString("stats.uuid-row") + " VARCHAR(255), " + Config.getString("stats.kills-row") + " VARCHAR(255), " + Config.getString("stats.deaths-row") + " VARCHAR(255), date VARCHAR(255))");
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public static void update(String qry) {
        if(isConnected()) {
            try {
                con.createStatement().executeUpdate(qry);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ResultSet getResult(String qry) {
        if(isConnected()) {
            try {
                return con.createStatement().executeQuery(qry);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
