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

    private Tick.TickBuilder priceBuilder;
    private Bar.BarBuilder barBuilder;

    @BeforeEach
    public void setUp() {
        priceBuilder = Tick.builder()
                .timestamp(Instant.now())
                .ask(1)
                .bid(1);

        barBuilder = Bar.builder()
                .close(priceBuilder.build().getMid())
                .high(priceBuilder.build().getMid())
                .low(priceBuilder.build().getMid())
                .open(priceBuilder.build().getMid())
                .timestamp(priceBuilder.build().timestamp())
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
//        testBars.add(barBuilder.close(setP(priceBuilder.build(), 2));
//        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), 3)));
//        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), 4)));
//        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), 5)));
//        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), 6)));
//        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), 7)));
//        assertEquals(2.00000, testBars.at(0).close().bid());
//        assertEquals(3.00000, testBars.at(1).close().bid());
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

    private Tick setP(Tick p, double val) {
        Tick.TickBuilder tb = p.toBuilder();
        tb.ask(val);
        tb.bid(val);
        return tb.build();
    }
}