package io.kw.engine.core.indicators;

import io.kw.engine.core.BarObserver;
import io.kw.model.Bar;
import io.kw.model.CurrencyPair;
import io.kw.model.DataBuffer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.enterprise.context.Dependent;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Indicator extends BarObserver {
    private final @NonNull List<DataBuffer<BigDecimal>> lineBuffers;
    private final int bufferNum;

    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED)
    private int prevCalculated = 0;

    public Indicator(final int bufferNum) {
        checkIfBufferNumInRange(bufferNum);
        this.bufferNum = bufferNum;
        lineBuffers = Stream
                .generate(DataBuffer<BigDecimal>::new)
                .limit(bufferNum)
                .collect(Collectors.toList());
    }

    public BigDecimal getLineValue(int bufferNum, int index, boolean isReversed) {
        checkIfValidBufferIndex(bufferNum);
        return lineBuffers.get(bufferNum).get(index, isReversed);
    }

    List<DataBuffer<BigDecimal>> getLineBuffers() { return Collections.unmodifiableList(lineBuffers); }

    protected final void addValueToLine(int bufferNum, BigDecimal value) {
        checkIfValidBufferIndex(bufferNum);
        lineBuffers.get(bufferNum).add(value);
    }

    protected final void setValueForLine(int bufferNum, int index, BigDecimal value) {
        checkIfValidBufferIndex(bufferNum);
        lineBuffers.get(bufferNum).updateByIndex(index, value, false);
    }

    private void checkIfValidBufferIndex(int bufferNum) {
        if (bufferNum < 0 || bufferNum >= this.bufferNum) {
            throw new IndexOutOfBoundsException("There is not a buffer " + bufferNum);
        }
    }

    private void checkIfBufferNumInRange(int bufferNum) {
        if (bufferNum > 16) {
            throw new IllegalArgumentException("Amount of buffers is not in the range [0..16]");
        }
    }

    @Override
    public void onInit(final DataBuffer<Bar> bars) {
        setBars(bars);
        _onInit();
        setInitialized(true);
    }

    @Override
    public void onTick(final DataBuffer<Bar> bars) {
        if (isInitialized()) {
            setBars(bars);
            System.out.println("TICK FOR IND: " + bars.get(0, true).close().getMid());
            _onTick();
            return;
        }
        System.out.println("Indicator " + this.toString() + " is not initialized yet. (OnTick)");
    }

    @Override
    public void onNewBar() {
        _onNewBar();
    }

    protected abstract void validateInput();
}