package io.kw.engine.core;

import io.kw.engine.core.strategies.Strategy;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class StrategyContainer {

    @Inject
    TickAggregator tickAggregator;

    public void initStrategy(Strategy s, String apiKey) {}

    void onTick() {
    }

    void onNewBar() {
    }

    void onInit() { }
}
