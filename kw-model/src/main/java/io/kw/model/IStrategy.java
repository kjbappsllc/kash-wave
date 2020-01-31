package io.kw.model;

public interface IStrategy {
    void onStart();
    void onTick(CurrencyPair pair, Tick tick);
    void onBar(CurrencyPair pair, Timeframe timeframe, Bar bar);
    void onStop();
}