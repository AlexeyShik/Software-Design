package ru.shik.sd.model;

import java.time.LocalDateTime;

public class TicketEvent {

    private int ticketId;
    private LocalDateTime time;
    private LocalDateTime expirationTime;
    private Action action;

    public enum Action {
        CREATE,
        EXTEND,
    }

    public TicketEvent() {

    }

    public TicketEvent(int ticketId, LocalDateTime time, LocalDateTime expirationTime, Action action) {
        this.ticketId = ticketId;
        this.time = time;
        this.expirationTime = expirationTime;
        this.action = action;
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

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
