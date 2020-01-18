package io.kw.engine.core.strategies;

import io.kw.model.CurrencyPair;
import io.kw.model.Timeframe;

import java.util.List;

public class MACross extends Strategy {
    public MACross(CurrencyPair pair, Timeframe tf) {
        super(pair, tf, List.of());
        System.out.println("MACross Constructor Called");
    }

    @Override
    protected void _onInit() {
        System.out.println("MACross ON INIT Called");
    }

    @Override
    protected void _onTick() {
        System.out.println("MACross ON TICK Called");
    }

    @Override
    protected void _onNewBar() {
        System.out.println("MACross ON NEW BAR Called");
    }
}
