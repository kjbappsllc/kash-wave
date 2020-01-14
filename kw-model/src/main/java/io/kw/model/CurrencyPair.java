package io.kw.model;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public final class CurrencyPair implements Serializable {
    private @NonNull Currency base;
    private @NonNull Currency quote;
    public String getPairName(String delimiter) {
        return base.toString() + delimiter + quote.toString();
    }
}
