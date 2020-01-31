package io.kw.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.apache.commons.math3.util.Precision;

import java.time.Instant;

@Value
@Builder(toBuilder = true)
@Accessors(fluent = true, chain = true)
public class Tick {
    double bid;
    double ask;
    double askVolume;
    double bidVolume;
    @NonNull Instant timestamp;

    public double getMid() {
        return (bid() + ask()) / 2;
    }
}
