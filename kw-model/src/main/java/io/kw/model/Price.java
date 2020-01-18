package io.kw.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Objects;

@AllArgsConstructor
@Builder
@Data
public final class Price {
    private @NonNull BigDecimal bid;
    private @NonNull BigDecimal ask;
    private @NonNull Instant timestamp;
    private @NonNull CurrencyPair currencyPair;
    private int precision;

    public BigDecimal getMid() {
        if (Objects.nonNull(getBid()) &&
                Objects.nonNull(getAsk())) {
            return (getBid().add(getAsk())).divide(BigDecimal.valueOf(2), RoundingMode.HALF_EVEN);
        }
        return BigDecimal.ZERO;
    }
}
