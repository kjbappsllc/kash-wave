package io.kw.model;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.Objects;

@Builder(toBuilder = true)
@Data
@Accessors(fluent = true)
public final class Bar {
    private long volume;
    private @NonNull Price open;
    private @NonNull Price close;
    private @NonNull Price high;
    private @NonNull Price low;
    private @NonNull Timeframe timeframe;
    private @NonNull Instant timestamp;

    public void high(final Price price) {
        if (Objects.nonNull(price) && high().getMid().compareTo(price.getMid()) < 0) {
            System.out.println("Price is higher than current high, updating to new high");
            high = price;
        }
    }

    public void low(final Price price) {
        if (Objects.nonNull(price) && low().getMid().compareTo(price.getMid()) > 0) {
            System.out.println("Price is lower than current low, updating to new low");
            low = price;
        }
    }
}