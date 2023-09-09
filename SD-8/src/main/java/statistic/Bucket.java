package statistic;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Bucket {

    private final Instant startTime;
    private final Instant endTime;
    private final List<Instant> events;

    public Bucket(Instant startTime) {
        this.startTime = startTime;
        this.endTime = startTime.plus(1, ChronoUnit.HOURS);
        this.events = new ArrayList<>();
    }

    public void addEvent(Instant time) {
        events.add(time);
    }

    public int getEvents(Instant from, Instant to) {
        return (int) events.stream().filter(time -> !from.isAfter(time) && time.isBefore(to)).count();
    }

    public boolean inCurrentBucket(Instant time) {
        return !startTime.isAfter(time) && time.isBefore(endTime);
    }
}
