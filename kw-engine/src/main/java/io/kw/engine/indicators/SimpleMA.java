package io.kw.engine.indicators;

import io.kw.model.Bar;
import io.kw.model.Buffer;
import io.kw.model.CurrencyPair;
import io.kw.model.Timeframe;
import lombok.*;

@ToString
public class SimpleMA extends Indicator {
    public SimpleMA() {}

    @Override
    public boolean create(CurrencyPair pair, Timeframe timeframe, Object[] params) {
        return false;
    }
}
