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

class BufferTest {

    private Price.PriceBuilder priceBuilder;
    private Bar.BarBuilder barBuilder;

    @BeforeEach
    public void setUp() {
        priceBuilder = Price.builder()
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

    @DisplayName("Test DataBuffer: Simple Values")
    @Test
    public void testDataBufferRegularValues() {
        Buffer<BigDecimal> buffer = new Buffer<>();
        buffer.add(BigDecimal.valueOf(2));
        buffer.add(BigDecimal.valueOf(3));
        assertEquals(BigDecimal.valueOf(2), buffer.at(0));
        assertEquals(BigDecimal.valueOf(3), buffer.at(1));
    }

    @DisplayName("Test DataBuffer: Complex Values")
    @Test
    public void testDataBufferBarValues() {
        Buffer<Bar> testBars = new Buffer<>();
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(2))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(3))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(4))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(5))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(6))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(7))));
        assertEquals(BigDecimal.valueOf(2), testBars.at(0).close().getBid());
        assertEquals(BigDecimal.valueOf(3), testBars.at(1).close().getBid());
    }

    @DisplayName("Test DataBuffer Integrity")
    @Test
    public void testDataBufferIntegrity() {
        Buffer<Integer> buffer = new Buffer<>();
        buffer.add(1);
        assertEquals(1, buffer.getSize());
        buffer.add(2);
        assertEquals(2, buffer.getSize());
        buffer.addUpdate(1, 3);
        assertEquals(2, buffer.getSize());
        assertEquals(3, buffer.at(1));
        assertFalse(buffer.addUpdate(3, 4));
        assertTrue(buffer.addUpdate(2, 5));
        assertEquals(3, buffer.getSize());
        assertEquals(5, buffer.at(2));
        buffer.clear();
        assertEquals(0, buffer.getSize());
        assertTrue(buffer.addUpdate(0, 4));
        assertEquals(1, buffer.getSize());
        assertEquals(4, buffer.at(0));
        buffer.clear();
        assertEquals(0, buffer.getSize());
        buffer.add(9);
        assertEquals(1, buffer.getSize());
        assertEquals(9, buffer.at(0));
        assertTrue(buffer.addAll(List.of(5,6,7,8,10)));
        assertEquals(6, buffer.getSize());
        assertEquals(9, buffer.at(0));
        buffer.clear();
        buffer.addAll(
                Stream
                .generate(() -> 2)
                .limit(buffer.getStep())
                .collect(Collectors.toList())
        );
        assertTrue(buffer.addUpdate(buffer.getStep(), 5));
        assertEquals(buffer.getStep() + 1, buffer.getSize());
    }

    @DisplayName("Test DataBuffer: Copy Correctly")
    @Test
    public void testCopying() {
        Buffer<Integer> buffer = new Buffer<>();
        buffer.addUpdate(0, 1);
        buffer.add(2);
        buffer.add(3);
        Buffer<Integer> newBuffer = buffer.copy();
        assertEquals(3, newBuffer.getSize());
        assertEquals(1, newBuffer.at(0));
        assertEquals(2, newBuffer.at(1));
        assertEquals(3, newBuffer.at(2));
        buffer.add(4);
        assertThrows(Exception.class, () -> newBuffer.at(3));
        buffer = new Buffer<>();
        Buffer<Integer> newBuffer2 = buffer.copy();
        assertTrue(newBuffer2.addUpdate(0, 2));
        assertEquals(1, newBuffer2.getSize());
        Buffer<Bar> barBuffer = new Buffer<>();
        barBuffer.add(barBuilder.build());
        Buffer<Bar> barBufferCopy = barBuffer.copy();
        barBufferCopy.at(0).close(priceBuilder.precision(10).build());
        System.out.println(barBuffer.at(0).close());
        System.out.println(barBufferCopy.at(0).close());
        assertNotEquals(barBufferCopy.at(0).close(), barBuffer.at(0).close());
    }

    private Price setP(Price p, BigDecimal val) {
        p.setAsk(val);
        p.setBid(val);
        return p;
    }
}