package us.rjks.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import us.rjks.utils.Type;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 25.04.2021 / 16:52
 *
 **************************************************************************/

public class CountDownTimeEvent extends Event {

    private Type type;
    private int time;
    private static final HandlerList HANDLERS = new HandlerList();

    public CountDownTimeEvent() {

    }

    public CountDownTimeEvent(Type countDownType, int time) {
        this.type = countDownType;
        this.time = time;
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

    public int getTime() {
        return time;
    }
}
