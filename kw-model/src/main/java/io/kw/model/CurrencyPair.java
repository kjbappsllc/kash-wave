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
    String delimiter;
    int precision;
    int pipLocation;
    double marginRate;
    public String name() {
        String delimiterCoalesce = delimiter() != null ? delimiter() : "";
        return base.toString() + delimiterCoalesce + quote.toString();
}
}
