package io.kw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @DisplayName("Test DataBuffer: Simple Values")
    @Test
    public void testDataBufferRegularValues() {
        DataBuffer<BigDecimal> buffer = new DataBuffer<>();
        buffer.add(BigDecimal.valueOf(2));
        buffer.add(BigDecimal.valueOf(3));
        assertEquals(BigDecimal.valueOf(2), buffer.get(0));
        assertEquals(BigDecimal.valueOf(3), buffer.get(1));
    }

    @DisplayName("Test DataBuffer: Complex Values")
    @Test
    public void testDataBufferBarValues() {
        DataBuffer<Bar> testBars = new DataBuffer<>();
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(2))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(3))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(4))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(5))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(6))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(7))));
        assertEquals(BigDecimal.valueOf(2), testBars.get(0).close().getBid());
        assertEquals(BigDecimal.valueOf(3), testBars.get(1).close().getBid());
    }

    @DisplayName("Test DataBuffer Integrity")
    @Test
    public void testDataBufferIntegrity() {
        DataBuffer<Integer> buffer = new DataBuffer<>();
        buffer.add(1);
        assertEquals(1, buffer.size());
        buffer.add(2);
        assertEquals(2, buffer.size());
        buffer.addUpdate(1, 3);
        assertEquals(2, buffer.size());
        assertEquals(3, buffer.get(1));
        assertFalse(buffer.addUpdate(3, 4));
        assertTrue(buffer.addUpdate(2, 5));
        assertEquals(3, buffer.size());
        assertEquals(5, buffer.get(2));
        buffer.clear();
        assertEquals(0, buffer.size());
        assertTrue(buffer.addUpdate(0, 4));
        assertEquals(1, buffer.size());
        assertEquals(4, buffer.get(0));
        buffer.clear();
        assertEquals(0, buffer.size());
        buffer.add(9);
        assertEquals(1, buffer.size());
        assertEquals(9, buffer.get(0));
        assertTrue(buffer.addAll(List.of(5,6,7,8,10)));
        assertEquals(6, buffer.size());
        assertEquals(9, buffer.get(0));
        buffer.clear();
        buffer.addAll(
                Stream
                .generate(() -> 2)
                .limit(DataBuffer.STEP_SIZE)
                .collect(Collectors.toList())
        );
        assertTrue(buffer.addUpdate(DataBuffer.STEP_SIZE, 5));
        assertEquals(DataBuffer.STEP_SIZE + 1, buffer.size());
    }

    @DisplayName("Test DataBuffer: ToString Exclude Nulls")
    @Test
    public void testToString() {
        DataBuffer<Integer> buffer = new DataBuffer<>();
        buffer.addUpdate(0, 1);
        assertFalse(buffer.toString().contains("null"));
    }

    private Price setP(Price p, BigDecimal val) {
        p.setAsk(val);
        p.setBid(val);
        return p;
    }
}