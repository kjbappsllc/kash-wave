package io.kw.engine.indicators;

import io.kw.model.CurrencyPair;
import io.kw.model.Timeframe;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IndicatorTest {

    private static class TestIndicator extends Indicator {
        public TestIndicator() {}

        @Override
        boolean initialize(CurrencyPair pair, Timeframe timeframe, Object[] params) {
            return true;
        }
    }

    @Test
    @DisplayName("Test Indicator Integrity")
    public void testIntegrity() {
        TestIndicator indicator = new TestIndicator();
        assertTrue(indicator.createBuffers(3));
        assertEquals(3, indicator.getSize());
    }

}
