package io.kw.engine.core;

import io.kw.model.Bar;
import io.kw.model.DataBuffer;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public abstract class BarObserver {

    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED)
    private DataBuffer<Bar> bars;

    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED)
    protected boolean initialized = false;

    protected final Bar getBar(int index, boolean isReversed) {
        return Try.of(() -> getBars().at(index, isReversed)).getOrNull();
    }
    protected final int barCount() { return getBars().size(); }

    public abstract void onInit(final DataBuffer<Bar> bars);
    public abstract void onTick(final DataBuffer<Bar> bars);
    public abstract void onNewBar();
    protected abstract void _onInit();
    protected abstract void _onTick();
    protected abstract void _onNewBar();
}
