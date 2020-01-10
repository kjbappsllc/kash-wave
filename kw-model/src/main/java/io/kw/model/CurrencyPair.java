package io.kw.model;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter @ToString
public final class CurrencyPair implements Serializable {
    private @NonNull Currency base;
    private @NonNull Currency quote;

    public CurrencyPair setBase(Currency base) {
        this.base = base;
        return this;
    }

    public CurrencyPair setQuote(Currency quote) {
        this.quote = quote;
        return this;
    }

    public String getPairName(String delimiter) {
        return base.toString() + delimiter + quote.toString();
    }
}
