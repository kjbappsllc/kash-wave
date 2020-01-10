package io.kw.model;

import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Indicator implements PriceObserver {
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
}