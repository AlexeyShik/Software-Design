import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import clock.SetableClock;
import statistic.EventStatisticImpl;
import statistic.EventsStatistic;

public class EventStatisticTest {

    private static final String METHOD_NAME1 = "a.b.ClassName1#method1";
    private static final String METHOD_NAME2 = "c.d.ClassName2#method2";
    private static final String PACKAGE_CLASS = "p.q.r.ClassName";
    private static final String METHOD = "#doSomething";
    private static final double DELTA = 1e-8;

    @Test
    public void testNoEvents() {
        Instant startTime = Instant.ofEpochMilli(0);
        SetableClock clock = new SetableClock();
        EventsStatistic stat = new EventStatisticImpl(clock);

        clock.setNow(startTime);
        Assert.assertEquals(0, stat.getEventStatisticByName(METHOD_NAME1), 1e-5);

        Map<String, Double> map = stat.getAllEventStatistic();
        Assert.assertNotNull(map);
        Assert.assertEquals(0, map.size());
    }

    @Test
    public void testOneEvent() {
        Instant startTime = Instant.ofEpochMilli(0);
        SetableClock clock = new SetableClock();
        EventsStatistic stat = new EventStatisticImpl(clock);

        clock.setNow(startTime);
        stat.incEvent(METHOD_NAME1);

        clock.setNow(startTime.plus(5, ChronoUnit.MINUTES));
        stat.incEvent(METHOD_NAME2);

        clock.setNow(startTime);
        // так как час я считаю [now - hour, now).
        Assert.assertEquals(0, stat.getEventStatisticByName(METHOD_NAME1), DELTA);
        Assert.assertEquals(0, stat.getEventStatisticByName(METHOD_NAME2), DELTA);
        Assert.assertEquals(Map.of(METHOD_NAME1, 0.0, METHOD_NAME2, 0.0), stat.getAllEventStatistic());

        clock.setNow(startTime.plus(1, ChronoUnit.MINUTES));
        Assert.assertEquals(1.0 / 60, stat.getEventStatisticByName(METHOD_NAME1), DELTA);
        Assert.assertEquals(0, stat.getEventStatisticByName(METHOD_NAME2), DELTA);
        Assert.assertEquals(Map.of(METHOD_NAME1, 1.0 / 60, METHOD_NAME2, 0.0), stat.getAllEventStatistic());

        clock.setNow(startTime.plus(5, ChronoUnit.MINUTES));
        Assert.assertEquals(1.0 / 60, stat.getEventStatisticByName(METHOD_NAME1), DELTA);
        Assert.assertEquals(0, stat.getEventStatisticByName(METHOD_NAME2), DELTA);
        Assert.assertEquals(Map.of(METHOD_NAME1, 1.0 / 60, METHOD_NAME2, 0.0), stat.getAllEventStatistic());

        clock.setNow(startTime.plus(6, ChronoUnit.MINUTES));
        Assert.assertEquals(1.0 / 60, stat.getEventStatisticByName(METHOD_NAME1), DELTA);
        Assert.assertEquals(1.0 / 60, stat.getEventStatisticByName(METHOD_NAME2), DELTA);
        Assert.assertEquals(Map.of(METHOD_NAME1, 1.0 / 60, METHOD_NAME2, 1.0 / 60), stat.getAllEventStatistic());

        clock.setNow(startTime.plus(1, ChronoUnit.HOURS));
        Assert.assertEquals(1.0 / 60, stat.getEventStatisticByName(METHOD_NAME1), DELTA);
        Assert.assertEquals(1.0 / 60, stat.getEventStatisticByName(METHOD_NAME2), DELTA);
        Assert.assertEquals(Map.of(METHOD_NAME1, 1.0 / 60, METHOD_NAME2, 1.0 / 60), stat.getAllEventStatistic());

        clock.setNow(startTime.plus(1, ChronoUnit.HOURS).plus(1, ChronoUnit.MINUTES));
        Assert.assertEquals(0, stat.getEventStatisticByName(METHOD_NAME1), DELTA);
        Assert.assertEquals(1.0 / 60, stat.getEventStatisticByName(METHOD_NAME2), DELTA);
        Assert.assertEquals(Map.of(METHOD_NAME1, 0.0, METHOD_NAME2, 1.0 / 60), stat.getAllEventStatistic());

        clock.setNow(startTime.plus(1, ChronoUnit.HOURS).plus(5, ChronoUnit.MINUTES));
        Assert.assertEquals(0, stat.getEventStatisticByName(METHOD_NAME1), DELTA);
        Assert.assertEquals(1.0 / 60, stat.getEventStatisticByName(METHOD_NAME2), DELTA);
        Assert.assertEquals(Map.of(METHOD_NAME1, 0.0, METHOD_NAME2, 1.0 / 60), stat.getAllEventStatistic());

        clock.setNow(startTime.plus(1, ChronoUnit.HOURS).plus(6, ChronoUnit.MINUTES));
        Assert.assertEquals(0, stat.getEventStatisticByName(METHOD_NAME1), DELTA);
        Assert.assertEquals(0, stat.getEventStatisticByName(METHOD_NAME2), DELTA);
        Assert.assertEquals(Map.of(METHOD_NAME1, 0.0, METHOD_NAME2, 0.0), stat.getAllEventStatistic());
    }

    @Test
    public void testManyEvents() {
        Instant startTime = Instant.ofEpochMilli(0);
        SetableClock clock = new SetableClock();
        EventsStatistic stat = new EventStatisticImpl(clock);

        for (int i = 0; i < 120; ++i) {
            int times = i < 60 ? 1 : 2;
            clock.setNow(startTime.plus(i, ChronoUnit.MINUTES));
            for (int j = 0; j < times; ++j) {
                stat.incEvent(METHOD_NAME1);
            }
        }

        for (int i = 0; i < 120; ++i) {
            clock.setNow(startTime.plus(i, ChronoUnit.MINUTES));
            Assert.assertEquals((double) i / 60, stat.getEventStatisticByName(METHOD_NAME1), DELTA);
        }

        for (int i = 120; i < 180; ++i) {
            clock.setNow(startTime.plus(i, ChronoUnit.MINUTES));
            Assert.assertEquals((double) (180 - i) * 2 / 60, stat.getEventStatisticByName(METHOD_NAME1), DELTA);
        }
    }

    @Test
    public void testPrintStatistics() {
        Instant startTime = Instant.ofEpochMilli(0);
        SetableClock clock = new SetableClock();
        EventsStatistic stat = new EventStatisticImpl(clock);

        clock.setNow(startTime);
        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < (i + 1) * 60; ++j) {
                stat.incEvent(PACKAGE_CLASS + (i + 1) + METHOD);
            }
        }

        clock.setNow(startTime.plus(1, ChronoUnit.HOURS));
        stat.printStatistic();
    }

}
