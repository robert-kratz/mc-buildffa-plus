package us.rjks.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import us.rjks.event.CountDownEndsEvent;
import us.rjks.event.CountDownTimeEvent;
import us.rjks.utils.Type;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 25.04.2021 / 17:11
 *
 **************************************************************************/

public class CountDown implements Listener {

    @EventHandler
    public void onCountDownEnds(CountDownEndsEvent event) {
        if (event.getType().equals(Type.MAP)) {

        }
    }

    @EventHandler
    public void onCountDownTime(CountDownTimeEvent event) {
        if (event.getType().equals(Type.MAP)) {

        }
    }

}
