package io.kw.engine.core.strategies;

import io.kw.model.CurrencyPair;
import io.kw.model.Timeframe;

public class MACross extends Strategy {
    public MACross(CurrencyPair pair, Timeframe tf) {
        super(pair, tf);
        System.out.println("MACross is initialized");
    }

    @Override
    public void onInit() {
        System.out.println(this.getClass().getSimpleName() + " onCalculate Called");
    }

    @Override
    public void onTick() {
        System.out.println("MACross onTick Called: " + getGuid());
    }

    @Override
    public void onNewBar() {
        System.out.println("MACross onNewBar Called: " + getGuid());
    }
}
