package us.rjks.game;

import us.rjks.utils.Counter;
import us.rjks.utils.MapManager;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 25.04.2021 / 16:27
 *
 **************************************************************************/

public class GameManager {

    private MapManager.Map currentMap;
    private Counter mapchange;
    private boolean setup;

    public GameManager() {

    }

    public MapManager.Map getCurrentMap() {
        return currentMap;
    }

    public Counter getMapchange() {
        return mapchange;
    }

    public boolean isSetup() {
        return setup;
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
