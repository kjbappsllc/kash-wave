package io.kw.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Builder
@Accessors(fluent = true, chain = true)
public class CurrencyPair {
    @NonNull Currency base;
    @NonNull Currency quote;
    int precision;
    int pipLocation;
    double marginRate;
    public String name(char delimiter) {
        return base.toString() + delimiter + quote.toString();
    }
}
