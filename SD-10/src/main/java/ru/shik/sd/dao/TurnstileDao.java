package ru.shik.sd.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import ru.shik.sd.model.TurnstileEvent;

public interface TurnstileDao {

    List<TurnstileEvent> getEvents();

    List<TurnstileEvent> getEvents(long ticketId);

    List<TurnstileEvent> getEventsAfter(LocalDateTime time);

    Optional<TurnstileEvent> getRecentEvent(long ticketId);

    void addEvent(TurnstileEvent event);
}
