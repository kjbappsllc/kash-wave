package io.kw.engine.indicators;

import io.kw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleMATest {

    private Tick.PriceBuilder priceBuilder;
    private Bar.BarBuilder barBuilder;

    @BeforeEach
    public void onInit() {
        priceBuilder = Tick.builder()
                .currencyPair(new CurrencyPair(
                        Currency.EUR,
                        Currency.USD,
                        "EURUSD",
                        5,
                        -4,
                        0.02
                ))
                .timestamp(Instant.now())
                .precision(5)
                .ask(BigDecimal.valueOf(1))
                .bid(BigDecimal.valueOf(1));

        barBuilder = Bar.builder()
                .close(priceBuilder.build())
                .high(priceBuilder.build())
                .low(priceBuilder.build())
                .open(priceBuilder.build())
                .timeframe(Timeframe.M5)
                .timestamp(priceBuilder.build().getTimestamp())
                .volume(100);
    }

    @Test
    @DisplayName("Test If Callbacks Are Correct")
    public void testSimpleMA() {
        Buffer<Bar> testBars = new Buffer<>();
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(2))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(3))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(4))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(5))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(6))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(7))));
    }

    @DisplayName("Test With Decimal Numbers Where Precision Matters")
    @Test
    public void testWithDecimalNumbers() {
        Buffer<Bar> testBars = new Buffer<>();
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), new BigDecimal("91.19000"))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), new BigDecimal("91.15000"))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), new BigDecimal("91.24000"))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), new BigDecimal("91.16000"))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), new BigDecimal("91.01000"))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), new BigDecimal("91.06000"))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), new BigDecimal("91.02000"))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), new BigDecimal("90.96000"))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), new BigDecimal("90.98000"))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), new BigDecimal("90.97000"))));
    }

    private Tick setP(Tick p, BigDecimal val) {
        p.setAsk(val);
        p.setBid(val);
        return p;
    }
}