package application.a.b.c.d;

import java.util.concurrent.atomic.AtomicReference;

public class MultiplierHolder {

    private final AtomicReference<Double> value;

    public MultiplierHolder() {
        value = new AtomicReference<>(1.0);
    }

    public void mul() {
        while (true) {
            Double prev = value.get();
            if (value.compareAndSet(prev, prev * 1.001)) {
                return;
            }
        }
    }

    public double get() {
        return value.get();
    }
}
