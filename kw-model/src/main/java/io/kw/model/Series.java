package io.kw.model;

public abstract class Series<T> extends Buffer<T> {
    protected CurrencyPair pair;
    protected Timeframe freq;
    protected int numBuffers;
    abstract protected void refreshData();
}
