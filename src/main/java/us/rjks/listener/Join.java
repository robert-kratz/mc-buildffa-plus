package us.rjks.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import us.rjks.game.GameManager;
import us.rjks.game.Main;
import us.rjks.utils.Config;
import us.rjks.utils.MapManager;
import us.rjks.utils.ScoreBoard;
import us.rjks.utils.TabList;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 25.04.2021 / 16:27
 *
 **************************************************************************/

public class Join implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (Config.getBoolean("enable-rank-system") && Config.getBoolean("enable-tab-rank")) {
            TabList.setTabList(event.getPlayer());
        }

        if(Config.getBoolean("show-score-board")) {
            ScoreBoard.setScoreBoard(event.getPlayer());
        }

        if (Main.getGame().getCurrentMap() != null) {
            System.out.println(Main.getGame().getCurrentMap().getAmountOfSpawnCollection("spawn"));
            event.getPlayer().teleport(Main.getGame().getCurrentMap().getRandomLocationCollection("spawn"));
        }

    }

}
