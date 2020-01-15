package io.kw.engine.strategies;

import io.kw.engine.cdi.qualifiers.StrategyImplementation;

public class GoldenCross extends Strategy {
    public GoldenCross() {
        System.out.println("Golden Cross is Initialized");
    }

    @Override
    public void onCalculate() {
        System.out.println(this.getClass().getName() + " onCalculate Called");
    }
}
