package io.kw.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

import java.time.Instant;

@Value
@Builder(toBuilder = true)
@Accessors(fluent = true, chain = true)
public class Bar {
    long volume;
    double open;
    double close;
    double high;
    double low;
    double value;
    @NonNull Instant timestamp;
}