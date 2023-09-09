package statistic;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class MethodStatistic {

    private Bucket current;
    private Bucket last;

    public MethodStatistic() {
        last = new Bucket(Instant.ofEpochMilli(0));
        current = new Bucket(Instant.ofEpochMilli(0));
    }

    public void incEvent(Instant time) {
        if (!current.inCurrentBucket(time)) {
            last = current;
            current = new Bucket(time);
        }
        current.addEvent(time);
    }

    public double getRpm(Instant to) {
        Instant from = to.minus(1, ChronoUnit.HOURS);
        return (double) (last.getEvents(from, to) + current.getEvents(from, to)) / 60;
    }
}
