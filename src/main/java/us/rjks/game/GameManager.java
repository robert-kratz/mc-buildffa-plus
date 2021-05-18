package us.rjks.game;

import org.bukkit.entity.Player;
import us.rjks.utils.*;

import java.util.ArrayList;

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
    public ArrayList<Player> ingame = new ArrayList<>();
    private boolean setup;

    public GameManager() {
        mapchange = new Counter(Type.MAP, Config.getInteger("map-change-counter"));
        scoreBoard = new ScoreBoard();
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
