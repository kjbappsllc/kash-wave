package io.kw.engine.strategies;

import io.kw.engine.core.TickAggregator;
import lombok.ToString;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@ToString
@Dependent
public abstract class Strategy {

    @Inject
    TickAggregator tickAggregator;

    public abstract void onCalculate();
}
