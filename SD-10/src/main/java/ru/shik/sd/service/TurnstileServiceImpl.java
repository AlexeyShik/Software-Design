package ru.shik.sd.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ru.shik.sd.dao.TurnstileDao;
import ru.shik.sd.model.TurnstileEvent;

@Service
public class TurnstileServiceImpl implements TurnstileService {

    private final TurnstileDao turnstileDao;

    public TurnstileServiceImpl(TurnstileDao turnstileDao) {
        this.turnstileDao = turnstileDao;
    }

    @Override
    public Optional<TurnstileEvent> getRecentEvent(int ticketId) {
        return turnstileDao.getRecentEvent(ticketId);
    }

    @Override
    public void addEvent(int ticketId, TurnstileEvent.Action action, LocalDateTime time) {
        turnstileDao.addEvent(new TurnstileEvent(ticketId, time, action));
    }
}
