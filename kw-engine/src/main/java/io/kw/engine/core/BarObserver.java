package io.kw.engine.core;

import io.kw.model.Bar;
import io.kw.model.DataBuffer;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public abstract class BarObserver {

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private DataBuffer<Bar> bars;

    public final Bar get(int index) {
        return Try.of(() -> getBars().get(index)).getOrNull();
    }
    public abstract void onInit(DataBuffer<Bar> bars);
    public abstract void onTick(DataBuffer<Bar> bars);
    public abstract void onNewBar();
    protected abstract void _onInit();
    protected abstract void _onTick();
    protected abstract void _onNewBar();
}