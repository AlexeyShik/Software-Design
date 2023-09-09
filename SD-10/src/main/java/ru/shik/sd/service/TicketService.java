package ru.shik.sd.service;

import java.time.LocalDateTime;
import java.util.Optional;

import ru.shik.sd.model.TicketState;

public interface TicketService {

    Optional<TicketState> getTicketState(long ticketId);

    void addTicket(int ticketId, LocalDateTime time, LocalDateTime expirationTime);

    void extendTicket(int ticketId, LocalDateTime time, LocalDateTime expirationTime);
}
