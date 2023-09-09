package ru.shik.sd.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ru.shik.sd.dao.TicketDao;
import ru.shik.sd.model.TicketEvent;
import ru.shik.sd.model.TicketState;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketDao ticketDao;

    public TicketServiceImpl(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    @Override
    public Optional<TicketState> getTicketState(long ticketId) {
        List<TicketEvent> events = ticketDao.getEvents(ticketId);
        if (events.isEmpty()) {
            return Optional.empty();
        }

        TicketState state = new TicketState();
        for (TicketEvent event : events) {
            state.accept(event);
        }
        return Optional.of(state);
    }

    @Override
    public void addTicket(int ticketId, LocalDateTime time, LocalDateTime expirationTime) {
        ticketDao.addEvent(new TicketEvent(ticketId, time, expirationTime, TicketEvent.Action.CREATE));
    }

    @Override
    public void extendTicket(int ticketId, LocalDateTime time, LocalDateTime expirationTime) {
        ticketDao.addEvent(new TicketEvent(ticketId, time, expirationTime, TicketEvent.Action.EXTEND));
    }
}
