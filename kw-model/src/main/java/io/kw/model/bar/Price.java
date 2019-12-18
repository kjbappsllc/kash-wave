package io.kw.model.bar;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Price implements Serializable {
    private @NonNull BigDecimal bid;
    private @NonNull BigDecimal ask;
    private @NonNull Instant timestamp;
    private int precision;

    public BigDecimal getMid() {
        if (Objects.nonNull(getBid()) &&
                Objects.nonNull(getAsk())) {
            return (getBid().add(getAsk())).divide(BigDecimal.valueOf(2), RoundingMode.HALF_EVEN);
        }
        return BigDecimal.ZERO;
    }
}
