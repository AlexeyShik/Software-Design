package ru.shik.sd.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TurnstileEvent {

    private int ticketId;
    private LocalDateTime time;
    private Action action;

    public enum Action {
        ENTER,
        EXIT,
    }

    public TurnstileEvent() {

    }

    public TurnstileEvent(int ticketId, LocalDateTime time, Action action) {
        this.ticketId = ticketId;
        this.time = time;
        this.action = action;
    }

    public TurnstileEvent(int ticketId, long time, String action) {
        this.ticketId = ticketId;
        this.time = LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC);
        this.action = Action.valueOf(action);
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
