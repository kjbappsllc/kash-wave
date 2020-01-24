package io.kw.engine.core.indicators;

import io.kw.model.CurrencyPair;
import io.kw.model.Timeframe;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;

@ToString
public class SimpleMA extends Indicator {
    public SimpleMA() {}

    @Override
    boolean initialize(CurrencyPair pair, Timeframe timeframe, Object[] params) {
        return false;
    }
}
