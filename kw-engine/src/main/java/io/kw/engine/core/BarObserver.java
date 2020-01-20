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

    @Getter @Setter
    protected boolean initialized = false;

    public final Bar getBar(int index, boolean isReversed) {
        return Try.of(() -> getBars().get(index, isReversed)).getOrNull();
    }
    public final int barCount() { return getBars().size(); }

    public abstract void onInit(final DataBuffer<Bar> bars);
    public abstract void onTick(final DataBuffer<Bar> bars);
    public abstract void onNewBar();
    protected abstract void _onInit();
    protected abstract void _onTick();
    protected abstract void _onNewBar();
}
