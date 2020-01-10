package io.kw.engine.strategies;

import io.kw.model.Bar;
import io.kw.model.DataBuffer;
import io.kw.service.cdi.qualifiers.DataInitialized;
import io.kw.service.cdi.qualifiers.TickReceived;

import javax.enterprise.event.Observes;

public abstract class Strategy {
    private void _onInit(@Observes @DataInitialized DataBuffer<Bar> bars) {
        System.out.println("Strategy Initialized");
    }
    private void _onCalculate(@Observes @TickReceived DataBuffer<Bar> bars) {
        System.out.println("Strategy Tick Received");
    }
}
