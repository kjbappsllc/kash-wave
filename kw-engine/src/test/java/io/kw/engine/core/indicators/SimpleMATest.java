package io.kw.engine.core.indicators;

import io.kw.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.awt.image.ImageFilter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleMATest {

    private Price.PriceBuilder priceBuilder;
    private Bar.BarBuilder barBuilder;

    @BeforeEach
    public void onInit() {
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

    @Test
    @DisplayName("Test If Callbacks Are Correct")
    public void testSimpleMA() {
        SimpleMA simpleMA = new SimpleMA(3);
        DataBuffer<Bar> testBars = new DataBuffer<>();
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(2))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(3))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(4))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(5))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(6))));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(7))));
        simpleMA.onInit(testBars);
        DataBuffer<BigDecimal> maBuf = simpleMA.getLineBuffers().get(0);
        assertEquals(new BigDecimal("0.00000"), maBuf.get(0, false));
        assertEquals(new BigDecimal("0.00000"), maBuf.get(1, false));
        assertEquals(new BigDecimal("3.00000"), maBuf.get(2, false));
        assertEquals(new BigDecimal("4.00000"), maBuf.get(3, false));
        assertEquals(new BigDecimal("5.00000"), maBuf.get(4, false));
        assertEquals(new BigDecimal("6.00000"), maBuf.get(5, false));
        testBars.updateByIndex(testBars.size() - 1, barBuilder.build().close(setP(priceBuilder.build(), new BigDecimal(20))), false);
        simpleMA.onTick(testBars);
        assertEquals(new BigDecimal("10.33333"), maBuf.get(0, true));
        testBars.add(barBuilder.build().close(setP(priceBuilder.build(), BigDecimal.valueOf(30))));
        simpleMA.onTick(testBars);
        assertEquals(new BigDecimal("18.66667"), maBuf.get(0, true));
        assertEquals(new BigDecimal("10.33333"), maBuf.get(1, true));
    }

    @DisplayName("Test With Decimal Numbers Where Precision Matters")
    @Test
    public void testWithDecimalNumbers() {
        SimpleMA simpleMA = new SimpleMA(3);
        DataBuffer<Bar> testBars = new DataBuffer<>();
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
        simpleMA.onInit(testBars);
        DataBuffer<BigDecimal> maBuf = simpleMA.getLineBuffers().get(0);
        System.out.println(maBuf);
        assertEquals(new BigDecimal("0.00000"), maBuf.get(0, false));
        assertEquals(new BigDecimal("0.00000"), maBuf.get(1, false));
        assertEquals(new BigDecimal("91.19333"), maBuf.get(2, false));
        assertEquals(new BigDecimal("91.18333"), maBuf.get(3, false));
        assertEquals(new BigDecimal("91.13667"), maBuf.get(4, false));
        assertEquals(new BigDecimal("91.07667"), maBuf.get(5, false));
        assertEquals(new BigDecimal("91.03000"), maBuf.get(6, false));
        assertEquals(new BigDecimal("91.01333"), maBuf.get(7, false));


    }

    private Price setP(Price p, BigDecimal val) {
        p.setAsk(val);
        p.setBid(val);
        return p;
    }
}