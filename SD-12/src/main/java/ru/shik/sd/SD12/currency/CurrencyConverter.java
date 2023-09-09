package ru.shik.sd.SD12.currency;

import java.util.EnumMap;

public class CurrencyConverter {

    private static final EnumMap<Currency, Double> TO_RUB = new EnumMap<>(Currency.class);

    static {
        TO_RUB.put(Currency.RUB, 1.0);
        TO_RUB.put(Currency.EUR, 80.38);
        TO_RUB.put(Currency.USD, 76.27);
    }

    public double convertFromRUB(double from, Currency toCurrency) {
        return from / TO_RUB.get(toCurrency);
    }

    public double convertToRUB(double from, Currency fromCurrency) {
        return from * TO_RUB.get(fromCurrency);
    }
}
