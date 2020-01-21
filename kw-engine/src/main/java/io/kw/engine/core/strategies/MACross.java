package io.kw.engine.core.strategies;

import io.kw.engine.core.indicators.Indicator;
import io.kw.engine.core.indicators.SimpleMA;
import static io.kw.engine.core.indicators.SimpleMA.Lines;
import io.kw.model.CurrencyPair;
import io.kw.model.Timeframe;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public class MACross extends Strategy {

    @AllArgsConstructor
    @Getter
    private enum Indicators {
        FAST_MA(0),
        SLOW_MA(1);
        public int index;
        public static int getInd(Indicators indicator) {
            return indicator.getIndex();
        }
    }

    public MACross(CurrencyPair pair, Timeframe tf) {
        super(pair, tf, List.of(
                new SimpleMA(12),
                new SimpleMA(26)
        ));
        System.out.println("MACross Constructor Called");
    }

    @Override
    protected void _onInit() {
        System.out.println("MACross ON INIT Called");
    }

    @Override
    protected void _onTick() {
        System.out.println("MACross ON TICK Called");
        SimpleMA slow = (SimpleMA) getIndicators().get(Indicators.getInd(Indicators.SLOW_MA));
        SimpleMA fast = (SimpleMA) getIndicators().get(Indicators.getInd(Indicators.FAST_MA));
        System.out.println("Slow MA Val: " + getIndicatorVal(slow, Lines.getBufNum(Lines.MA_BUF), 0));
        System.out.println("Fast MA Val: " + getIndicatorVal(fast, Lines.getBufNum(Lines.MA_BUF), 0));
    }

    @Override
    protected void _onNewBar() {
        System.out.println("MACross ON NEW BAR Called");
    }
}
