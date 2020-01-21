package io.kw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
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
        assertEquals(BigDecimal.valueOf(2), buffer.get(0, false));
        assertEquals(BigDecimal.valueOf(3), buffer.get(1, false));
        assertEquals(BigDecimal.valueOf(3), buffer.get(0, true));
        assertEquals(BigDecimal.valueOf(2), buffer.get(1, true));
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
        assertEquals(BigDecimal.valueOf(2), testBars.get(0, false).close().getBid());
        assertEquals(BigDecimal.valueOf(3), testBars.get(1, false).close().getBid());
        assertEquals(BigDecimal.valueOf(7), testBars.get(0, true).close().getBid());
        assertEquals(BigDecimal.valueOf(6), testBars.get(1, true).close().getBid());
    }

    @DisplayName("Test DataBuffer Integrity")
    @Test
    public void testDataBufferIntegrity() {
        DataBuffer<Integer> buffer = new DataBuffer<>();
        buffer.add(1);
        assertEquals(1, buffer.size());
        buffer.add(2);
        assertEquals(2, buffer.size());
        buffer.updateByIndex(1, 3, false);
        assertEquals(2, buffer.size());
        assertEquals(3, buffer.get(1, false));
        assertThrows(Exception.class, () -> buffer.updateByIndex(3, 4, false));
        assertDoesNotThrow(() -> buffer.updateByIndex(2, 5, false));
        assertEquals(3, buffer.size());
        assertEquals(5, buffer.get(2, false));
        assertEquals(5, buffer.get(0, true));
        assertDoesNotThrow(() -> buffer.updateByIndex(0, 9, true));
        assertEquals(9, buffer.get(0, true));
        assertThrows(Exception.class, () -> buffer.updateByIndex(3, 2, true));
        assertEquals(3, buffer.size());
        buffer.clear();
        assertEquals(0, buffer.size());
        assertDoesNotThrow(() -> buffer.updateByIndex(0, 4, false));
        assertDoesNotThrow(() -> buffer.updateByIndex(0, 7, true));
        assertEquals(1, buffer.size());
        assertEquals(7, buffer.get(0, false));
        buffer.clear();
        assertEquals(0, buffer.size());
        buffer.add(9);
        assertEquals(1, buffer.size());
        assertEquals(9, buffer.get(0, false));
        assertEquals(9, buffer.get(0, true));
        buffer.addAll(List.of(5,6,7,8,10));
        assertEquals(6, buffer.size());
        assertEquals(9, buffer.get(0, false));
        assertEquals(10, buffer.get(0, true));
        buffer.clear();
        buffer.addAll(
                Stream
                .generate(() -> 2)
                .limit(DataBuffer.STEP_SIZE)
                .collect(Collectors.toList())
        );
        assertDoesNotThrow(() -> buffer.updateByIndex(DataBuffer.STEP_SIZE, 5, false));
        assertEquals(DataBuffer.STEP_SIZE + 1, buffer.size());
    }

    private Price setP(Price p, BigDecimal val) {
        p.setAsk(val);
        p.setBid(val);
        return p;
    }
}