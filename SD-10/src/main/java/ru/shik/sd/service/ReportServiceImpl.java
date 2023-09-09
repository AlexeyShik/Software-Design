package ru.shik.sd.service;


import java.util.List;

import org.springframework.stereotype.Service;

import ru.shik.sd.dao.TurnstileDao;
import ru.shik.sd.model.Report;
import ru.shik.sd.model.ReportSnapshot;
import ru.shik.sd.model.TurnstileEvent;

@Service
public class ReportServiceImpl implements ReportService {

    private final TurnstileDao turnstileDao;

    private final ReportSnapshot snapshot;

    public ReportServiceImpl(TurnstileDao turnstileDao) {
        this.turnstileDao = turnstileDao;
        snapshot = new ReportSnapshot();
        turnstileDao.getEvents().forEach(snapshot::addEvent);
    }

    @Override
    public Report getReport() {
        List<TurnstileEvent> events;
        if (snapshot.getSnapshotTime() == null) {
            events = turnstileDao.getEvents();
        } else {
            events = turnstileDao.getEventsAfter(snapshot.getSnapshotTime());
        }
        events.forEach(snapshot::addEvent);
        return snapshot.buildReport();
    }

}
