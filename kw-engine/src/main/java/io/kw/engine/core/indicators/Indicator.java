package io.kw.engine.core.indicators;

import io.kw.engine.core.BarObserver;
import io.kw.model.Bar;
import io.kw.model.DataBuffer;
import lombok.NonNull;

import javax.enterprise.context.Dependent;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Indicator extends BarObserver {
    private final @NonNull List<DataBuffer<BigDecimal>> lineBuffers;
    private final @NonNull int bufferNum;

    public Indicator(final int bufferNum) {
        checkIfBufferNumInRange(bufferNum);
        this.bufferNum = bufferNum;
        lineBuffers = Stream
                .generate(DataBuffer<BigDecimal>::new)
                .limit(bufferNum)
                .collect(Collectors.toList());
    }

    public BigDecimal getValue(int bufferNum, int index) {
        checkIfValidBufferIndex(bufferNum);
        return lineBuffers.get(bufferNum).get(index);
    }

    List<DataBuffer<BigDecimal>> getLineBuffers() { return Collections.unmodifiableList(lineBuffers); }

    protected final void addValue(int bufferNum, BigDecimal value) {
        checkIfValidBufferIndex(bufferNum);
        lineBuffers.get(bufferNum).add(value);
    }

    protected final void setValue(int bufferNum, int index, BigDecimal value) {
        checkIfValidBufferIndex(bufferNum);
        lineBuffers.get(bufferNum).update(index, value);
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
    public void onInit(DataBuffer<Bar> bars) {
        setBars(bars);
        _onInit();
        setInitialized(true);
    }

    @Override
    public void onTick(DataBuffer<Bar> bars) {
        if (isInitialized()) {
            setBars(bars);
            _onTick();
            return;
        }
        System.out.println("Indicator is not initialized yet. (OnTick)");
    }

    @Override
    public void onNewBar() {
        _onNewBar();
    }

    protected abstract void validateInput();
}