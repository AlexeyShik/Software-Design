package application;

import java.util.concurrent.atomic.AtomicInteger;

public class CounterHolder {

    private final AtomicInteger counter;

    public CounterHolder() {
        counter = new AtomicInteger(0);
    }

    public void inc() {
        counter.incrementAndGet();
    }

    public int get() {
        return counter.get();
    }
}
