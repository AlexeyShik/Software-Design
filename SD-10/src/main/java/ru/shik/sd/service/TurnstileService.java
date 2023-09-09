package ru.shik.sd.service;

import java.time.LocalDateTime;
import java.util.Optional;

import ru.shik.sd.model.TurnstileEvent;

public interface TurnstileService {

    Optional<TurnstileEvent> getRecentEvent(int ticketId);

    void addEvent(int ticketId, TurnstileEvent.Action action, LocalDateTime time);
}
