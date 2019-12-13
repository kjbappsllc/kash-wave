package io.kw.engine.indicators;

import io.kw.engine.core.IndicatorEngine;
import io.kw.model.bar.Bar;
import io.kw.model.datatype.DataBuffer;

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
