package us.rjks.listener;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import us.rjks.db.Coins;
import us.rjks.db.Stats;
import us.rjks.game.GameManager;
import us.rjks.game.Main;
import us.rjks.utils.*;

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
        if(!Coins.userExists(event.getPlayer().getUniqueId().toString())) {
            Coins.createUser(event.getPlayer().getUniqueId().toString());
        }
        if(!Stats.userExists(event.getPlayer().getUniqueId().toString())) {
            Stats.createUser(event.getPlayer().getUniqueId().toString());
        }

        if(Config.getBoolean("enable-player-joins-message")) {
            event.setJoinMessage(Messages.getString("player-joins-message").replaceAll("%player%", event.getPlayer().getName()));
        }

        if (Config.getBoolean("enable-rank-system") && Config.getBoolean("enable-tab-rank")) {
            TabList.setTabList(event.getPlayer());
        }

        if(Config.getBoolean("show-score-board")) {
            Main.getGame().getScoreBoard().setScoreBoard(event.getPlayer()); }

        if (Main.getGame().getCurrentMap() != null) {
            System.out.println(Main.getGame().getCurrentMap().getAmountOfSpawnCollection("spawn"));
            event.getPlayer().teleport(Main.getGame().getCurrentMap().getRandomLocationCollection("spawn"));
        }

        event.getPlayer().setMaxHealth(Config.getDouble("hearts-when-join"));
        event.getPlayer().setHealthScale(Config.getDouble("hearts-when-join"));
        event.getPlayer().setLevel(0);
        event.getPlayer().setGameMode(GameMode.SURVIVAL);

        event.getPlayer().sendMessage(Messages.getString("player-welcome-message"));

        event.getPlayer().getInventory().clear();
        /*if(KitManager.getKitFromName("startInv") != null) {
            KitManager.getKitFromName("startInv").setKit(event.getPlayer());
        }*/
        Inventory.loadInvSelect(event.getPlayer());

    }

}
