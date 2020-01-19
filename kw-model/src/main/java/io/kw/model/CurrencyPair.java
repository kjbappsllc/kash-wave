package io.kw.model;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@ToString
public final class CurrencyPair {
    private @NonNull Currency base;
    private @NonNull Currency quote;
    public String pairName(String delimiter) {
        return base.toString() + delimiter + quote.toString();
    }
}
