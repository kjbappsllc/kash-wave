package io.kw.model;

import lombok.NoArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IndicatorTest {

    private static class TestIndicator extends Indicator {
        public TestIndicator() {super(2);}
        @Override
        public void onCalculate(DataBuffer<Bar> bars) { }
        @Override
        public void onInit(DataBuffer<Bar> bars) { }
    }

    @Test
    @DisplayName("Test Indicator Integrity")
    public void testIntegrity() {
        Indicator testIndicator = new TestIndicator();
        List<DataBuffer<BigDecimal>> lineBuffers = testIndicator.getLineBuffers();
        assertThrows(UnsupportedOperationException.class, () -> lineBuffers.add(new DataBuffer<>()));
        lineBuffers.get(0).add(new BigDecimal(1));
        assertEquals(0, lineBuffers.get(0).size());
    }

}