package io.kw.engine.core.indicators;

import lombok.ToString;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Queue;

@ToString
public class SimpleMA extends Indicator {
    public enum Lines { SMA }

    @ToString.Exclude
    private final Queue<BigDecimal> window = new LinkedList<>();

    private final int period;

    public SimpleMA(int period) {
        super(1);
        System.out.println("Simple MA Initialized");
        this.period = period;
        validateInput();

    }

    @Override
    protected void _onInit() {

    }

    @Override
    protected void _onTick() {

    }

    @Override
    protected void _onNewBar() {

    }

    @Override
    protected void validateInput() {
        if (period < 0) {
            throw new IllegalArgumentException("Invalid Arguments " + this);
        }
    }
}
