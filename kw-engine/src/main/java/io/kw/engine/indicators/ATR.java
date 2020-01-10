package io.kw.engine.indicators;

import io.kw.engine.core.IndicatorEngine;
import io.kw.model.Bar;
import io.kw.model.DataBuffer;

public final class ATR extends IndicatorEngine.Indicator {

    public ATR() {
        super(2);
    }

    @Override
    protected void onCalculate(DataBuffer<Bar> bars) {
        System.out.println("ATR");
    }

    @Override
    protected void onInit(DataBuffer<Bar> bars) {
        System.out.println("ATR Initialized");
    }
}
