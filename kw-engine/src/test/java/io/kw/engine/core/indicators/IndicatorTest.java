package io.kw.engine.core.indicators;

import io.kw.model.Buffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IndicatorTest {

    private static class TestIndicator extends Indicator {
        public TestIndicator() {
            super(1);
        }
    }

    @Test
    @DisplayName("Test Indicator Integrity")
    public void testIntegrity() {
    }

}
