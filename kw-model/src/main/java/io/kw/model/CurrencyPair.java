package io.kw.model;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@RequiredArgsConstructor
@ToString
public final class CurrencyPair {
    private @NonNull Currency base;
    private @NonNull Currency quote;
    private @NonNull String name;
    private @NonNull int precision;
    private @NonNull int pipLocation;
    private @NonNull int marginRate;

    public String pairName(char delimiter) {
        return base.toString() + delimiter + quote.toString();
    }
}
