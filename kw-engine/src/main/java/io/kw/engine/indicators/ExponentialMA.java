package io.kw.engine.indicators;

import io.kw.engine.core.IndicatorEngine;
import io.kw.model.bar.Bar;
import io.kw.model.datatype.DataBuffer;

public final class ExponentialMA extends IndicatorEngine.Indicator {

    public ExponentialMA() {
        super(2);
    }

    @Override
    protected void onCalculate(DataBuffer<Bar> bars) {
        System.out.println("ExpMA");
    }

    @Override
    protected void onInit(DataBuffer<Bar> bars) {
        System.out.println("EXPMA Initialized");
    }
}
