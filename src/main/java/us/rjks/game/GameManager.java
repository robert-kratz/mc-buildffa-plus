package us.rjks.game;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import us.rjks.db.*;
import us.rjks.utils.*;

import java.util.ArrayList;
import java.util.HashMap;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 25.04.2021 / 16:27
 *
 **************************************************************************/

public class GameManager {

    private MapManager.Map currentMap;
    private MapManager.Map forcemap;
    private Counter mapchange;
    private ScoreBoard scoreBoard;

    private MySQL mySQL;
    private Sort sort;
    private Coins coins;
    private Shop shop;
    private Stats stats;
    private Inventory inventory;

    public ArrayList<Player> ingame = new ArrayList<>();
    public HashMap<Projectile, String> projectiles = new HashMap<>();
    private boolean setup;

    public GameManager() {
        mapchange = new Counter(Type.MAP, Config.getInteger("map-change-counter"));
        scoreBoard = new ScoreBoard();

        inventory = new Inventory();

        mySQL = new MySQL();
        sort = new Sort();
        coins = new Coins();
        shop = new Shop();
        stats = new Stats();
    }

    public MapManager.Map getCurrentMap() {
        return currentMap;
    }

    public Counter getMapchange() {
        return mapchange;
    }

    public MapManager.Map getForcemap() {
        return forcemap;
    }

    public ScoreBoard getScoreBoard() {
        return scoreBoard;
    }

    public Coins getCoins() {
        return coins;
    }

    public ArrayList<Player> getIngame() {
        return ingame;
    }

    public Shop getShop() {
        return shop;
    }

    public Sort getSort() {
        return sort;
    }

    public Stats getStats() {
        return stats;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public HashMap<Projectile, String> getProjectiles() {
        return projectiles;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public boolean isSetup() {
        return setup;
    }

    public void setForcemap(MapManager.Map forcemap) {
        this.forcemap = forcemap;
    }

    public void setSetup(boolean setup) {
        this.setup = setup;
    }

    public void setMapchange(Counter mapchange) {
        this.mapchange = mapchange;
    }

    public void setCurrentMap(MapManager.Map currentMap) {
        this.currentMap = currentMap;
    }
}
