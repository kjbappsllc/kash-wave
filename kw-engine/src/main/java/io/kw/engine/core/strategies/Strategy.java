package io.kw.engine.core.strategies;

import io.kw.engine.core.indicators.Indicator;
import io.kw.model.Bar;
import io.kw.model.CurrencyPair;
import io.kw.model.Buffer;
import io.kw.model.Timeframe;
import io.vavr.control.Try;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class Strategy {
    @Getter
    private UUID guid = UUID.randomUUID();
}
