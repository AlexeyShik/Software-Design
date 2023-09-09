package application;

import application.a.b.c.d.MultiplierHolder;

public class CounterApplication {

    private static final NestedClass NESTED_INSTANCE = new NestedClass();

    public static void main(String[] args) {
        CounterHolder counter = new CounterHolder();
        MultiplierHolder multiplier = new MultiplierHolder();

        for (int j = 0; j < 100; ++j) {
            counter.inc();
            multiplier.mul();

            if (j % 2 == 0) {
                NESTED_INSTANCE.add(counter);
            }

            if (j % 2 == 1) {
                NESTED_INSTANCE.mul(multiplier);
            }
        }
        NESTED_INSTANCE.get();
    }

    private static class NestedClass {

        int value;

        void add(CounterHolder counterHolder) {
            value += counterHolder.get();
        }

        void mul(MultiplierHolder multiplierHolder) {
            value = (int) (value * multiplierHolder.get());
        }

        int get() {
            return value;
        }
    }
}
