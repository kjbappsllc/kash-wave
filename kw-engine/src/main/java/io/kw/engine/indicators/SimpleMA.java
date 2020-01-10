package io.kw.engine.indicators;

import io.kw.model.Bar;
import io.kw.model.DataBuffer;

public final class SimpleMA extends IndicatorEngine.Indicator {

    public SimpleMA() {
        super(2);
    }

    @Override
    protected void onCalculate(DataBuffer<Bar> bars) {
        System.out.println("SimpleMA");
    }

    @Override
    protected void onInit(DataBuffer<Bar> bars) {
        System.out.println("SimpleMA Initialized");
    }
}
