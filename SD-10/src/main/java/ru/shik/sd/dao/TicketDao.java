package ru.shik.sd.dao;

import java.util.List;

import ru.shik.sd.model.TicketEvent;

public interface TicketDao {

    List<TicketEvent> getEvents(long ticketId);

    void addEvent(TicketEvent event);
}
