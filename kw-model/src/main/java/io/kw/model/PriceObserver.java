package io.kw.model;

public interface PriceObserver {
    void onCalculate(DataBuffer<Bar> bars);
    void onInit(DataBuffer<Bar> bars);
}
