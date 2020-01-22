package io.kw.model;

public abstract class Series<T> extends DataBuffer<T> {
    protected CurrencyPair pair;
    protected Timeframe freq;
    protected int bufferNum;
}
