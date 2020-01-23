package io.kw.model;

public abstract class Series<T> extends Buffer<T> {
    protected CurrencyPair pair;
    protected Timeframe timeframe;
    protected int numBuffers;

    protected void checkIfBufferNumInRange() {
        int MAX_BUFFER_NUMBER = 16;
        if (numBuffers > MAX_BUFFER_NUMBER || numBuffers < 0)
            throw new IllegalArgumentException("Amount of buffers is not in the range [0..16]");
    }

    abstract protected void refreshData();
}
