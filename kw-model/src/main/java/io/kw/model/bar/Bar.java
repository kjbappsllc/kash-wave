package io.kw.model.bar;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Builder
@Data
public class Bar implements Serializable {
    private long volume;
    private @NonNull Price open;
    private @NonNull Price close;
    private @NonNull Price high;
    private @NonNull Price low;
    private @NonNull Timeframe timeframe;
    private @NonNull Instant timestamp;

    public void setHigh(final Price price) {
        if (Objects.nonNull(price) && getHigh().getMid().compareTo(price.getMid()) < 0) {
            System.out.println("Price is higher than current high, updating to new high");
            high = price;
        }
    }

    public void setLow(final Price price) {
        if (Objects.nonNull(price) && getLow().getMid().compareTo(price.getMid()) > 0) {
            System.out.println("Price is lower than current low, updating to new low");
            low = price;
        }
    }
}