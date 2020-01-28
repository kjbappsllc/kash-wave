package io.kw.engine.core;

import io.kw.model.Bar;
import io.kw.model.Buffer;

public interface IDataObserver {
    void onInit();
    void onTick(Buffer<Bar> bars);
}
