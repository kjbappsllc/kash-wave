package io.kw.model;

import java.util.HashMap;
import java.util.Map;

public enum Currency {
    USD("USD"),
    CAD("CAD"),
    GBP("GBP"),
    AUD("AUD"),
    JPY("JPY"),
    EUR("EUR");

    public final String label;
    private static final Map<String, Currency> LABEL_CACHE = new HashMap<>();

    static {
        for (Currency c: Currency.values()) {
            LABEL_CACHE.put(c.label, c);
        }
    }

    Currency(String currencyString) {
        this.label = currencyString;
    }

    public static Currency valueOfLabel(String label) {
        return LABEL_CACHE.get(label.toUpperCase());
    }

    @Override
    public String toString() {
        return label;
    }
}
