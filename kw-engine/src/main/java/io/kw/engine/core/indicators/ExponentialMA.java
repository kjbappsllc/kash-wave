package io.kw.engine.core.indicators;

import io.kw.model.Bar;
import io.kw.model.DataBuffer;

public final class ExponentialMA extends Indicator {

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
