package us.rjks.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import us.rjks.utils.Type;

import java.util.ArrayList;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 25.04.2021 / 16:45
 *
 **************************************************************************/

public class CountDownEndsEvent extends Event {

    private Type type;
    private static final HandlerList HANDLERS = new HandlerList();

    public CountDownEndsEvent() {

    }

    public CountDownEndsEvent(Type countDownType) {
        this.type = countDownType;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Type getType() {
        return type;
    }
}
