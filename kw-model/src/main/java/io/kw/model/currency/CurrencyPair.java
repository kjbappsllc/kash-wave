package io.kw.model.currency;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter @ToString
public class CurrencyPair implements Serializable {
    private @NonNull Currency base;
    private @NonNull Currency quote;
    private BigDecimal marginRate;

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
