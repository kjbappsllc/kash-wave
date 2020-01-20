package io.kw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class DataBufferTest {

    private Price.PriceBuilder priceBuilder;
    private Bar.BarBuilder barBuilder;

    @BeforeEach
    public void setUp() {
        priceBuilder = Price.builder()
                .currencyPair(new CurrencyPair(Currency.EUR, Currency.USD))
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

    @DisplayName("Test DataBuffer: Regular Values")
    @Test
    public void testDataBufferRegularValues() {
        DataBuffer<BigDecimal> buffer = new DataBuffer<>();
        buffer.add(BigDecimal.valueOf(2));
        buffer.add(BigDecimal.valueOf(3));
        assertEquals(BigDecimal.valueOf(2), buffer.getN(0));
        assertEquals(BigDecimal.valueOf(3), buffer.getN(1));
        assertEquals(BigDecimal.valueOf(3), buffer.get(0));
        assertEquals(BigDecimal.valueOf(2), buffer.get(1));
    }

    @DisplayName("Test DataBuffer: Bar Values")
    @Test
    public void testDataBufferBarValues() {
        DataBuffer<Bar> testBars = new DataBuffer<>();
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(2))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(3))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(4))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(5))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(6))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(7))));
        assertEquals(BigDecimal.valueOf(2), testBars.getN(0).close().getBid());
        assertEquals(BigDecimal.valueOf(3), testBars.getN(1).close().getBid());
        assertEquals(BigDecimal.valueOf(7), testBars.get(0).close().getBid());
        assertEquals(BigDecimal.valueOf(6), testBars.get(1).close().getBid());
    }

    private Price setP(Price p, BigDecimal val) {
        p.setAsk(val);
        p.setBid(val);
        return p;
    }
}