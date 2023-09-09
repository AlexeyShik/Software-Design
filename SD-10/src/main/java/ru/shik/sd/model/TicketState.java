package ru.shik.sd.model;

import java.time.LocalDateTime;

public class TicketState {

    private long ticketId;
    private LocalDateTime expirationTime;

    public TicketState() {

    }

    public TicketState(long ticketId, LocalDateTime expirationTime) {
        this.ticketId = ticketId;
        this.expirationTime = expirationTime;
    }

    public void accept(TicketEvent event) {
        if (event.getAction() == TicketEvent.Action.CREATE) {
            setTicketId(event.getTicketId());
            setExpirationTime(event.getExpirationTime());
        } else if (event.getAction() == TicketEvent.Action.EXTEND) {
            setExpirationTime(event.getExpirationTime());
        }
    }

    public long getTicketId() {
        return ticketId;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setTicketId(long ticketId) {
        this.ticketId = ticketId;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }
}
