package statistic;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import clock.Clock;

public class EventStatisticImpl implements EventsStatistic {

    private final Clock clock;
    private final Map<String, MethodStatistic> statisticMap;

    public EventStatisticImpl(Clock clock) {
        this.clock = clock;
        this.statisticMap = new HashMap<>();
    }

    @Override
    public void incEvent(String name) {
        Instant now = clock.now();
        statisticMap
            .computeIfAbsent(name, x -> new MethodStatistic())
            .incEvent(now);
    }

    @Override
    public double getEventStatisticByName(String name) {
        Instant now = clock.now();
        return statisticMap
            .getOrDefault(name, new MethodStatistic())
            .getRpm(now);
    }

    @Override
    public Map<String, Double> getAllEventStatistic() {
        Instant now = clock.now();
        return statisticMap
            .entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getRpm(now)));
    }

    @Override
    public void printStatistic() {
        Map<String, Double> stats = getAllEventStatistic();
        for (Map.Entry<String, Double> entry : stats.entrySet()) {
            System.out.println("Rpm for method '" + entry.getKey() + "': " + entry.getValue());
        }
    }
}
