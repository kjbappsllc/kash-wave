package io.kw.engine.strategies;

import io.kw.engine.cdi.qualifiers.StrategyImplementation;

public class MACross extends Strategy {
    public MACross() {
        System.out.println("MACross is initialized");
    }

    @Override
    public void onCalculate() {
        System.out.println(this.getClass().getName() + " onCalculate Called");
    }
}
