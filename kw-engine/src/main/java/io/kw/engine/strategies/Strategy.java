package io.kw.engine.strategies;

import lombok.ToString;

@ToString
public abstract class Strategy {
    public abstract void onCalculate();
}
