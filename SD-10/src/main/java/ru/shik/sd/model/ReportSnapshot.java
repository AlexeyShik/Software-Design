package ru.shik.sd.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

public class ReportSnapshot {

    private static final int SECONDS = 60;

    private final Map<LocalDate, Long> dailyVisits;
    private final Map<Integer, LocalDateTime> enterTime;
    private long durationSumSeconds;
    private LocalDateTime snapshotTime;

    public ReportSnapshot() {
        dailyVisits = new HashMap<>();
        enterTime = new HashMap<>();
    }

    public void addEvent(TurnstileEvent event) {
        if (event.getAction() == TurnstileEvent.Action.ENTER) {
            enterTime.put(event.getTicketId(), event.getTime());
        } else if (event.getAction() == TurnstileEvent.Action.EXIT) {
            if (!enterTime.containsKey(event.getTicketId())) {
                return;
            }
            LocalDateTime startTime = enterTime.get(event.getTicketId());
            LocalDateTime endTime = event.getTime();
            long duration = endTime.toEpochSecond(ZoneOffset.UTC) - startTime.toEpochSecond(ZoneOffset.UTC);
            durationSumSeconds += duration;
            dailyVisits.compute(endTime.toLocalDate(), (date, accum) -> accum == null ? 1 : accum + 1);
        }

        if (snapshotTime == null || snapshotTime.isBefore(event.getTime())) {
            snapshotTime = event.getTime();
        }
    }

    public Report buildReport() {
        long visitsCount = 0;
        long daysCount = 0;
        for (Map.Entry<LocalDate, Long> entry : dailyVisits.entrySet()) {
            visitsCount += entry.getValue();
            ++daysCount;
        }
        double averageVisits = daysCount == 0 ? 0 : (double) visitsCount / daysCount;
        double averageDuration = daysCount == 0 ? 0 : (double) durationSumSeconds / visitsCount / SECONDS;
        return new Report(dailyVisits, averageVisits, averageDuration);
    }

    public LocalDateTime getSnapshotTime() {
        return snapshotTime;
    }
}
