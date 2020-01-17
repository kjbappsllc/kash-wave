package io.kw.engine.core.strategies;

import io.kw.model.Bar;
import io.kw.model.CurrencyPair;
import io.kw.model.DataBuffer;
import io.kw.model.Timeframe;
import io.vavr.control.Try;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public abstract class Strategy {
    private @NonNull CurrencyPair pair;
    private @NonNull Timeframe timeframe;
    private UUID guid = UUID.randomUUID();
    private DataBuffer<Bar> bars;
    private boolean didInitialize = false;

    public final void _onTick(DataBuffer<Bar> bars) {
        if (didInitialize) {
            setBars(bars);
            this.onTick();
        }
    }
    public final void _onInit(DataBuffer<Bar> bars) {
        setBars(bars);
        onInit();
        setDidInitialize(true);
    }

    public final Bar getBar(int index) {
        return Try.of(() -> bars.get(index)).getOrNull();
    }

    public abstract void onInit();
    public abstract void onTick();
    public abstract void onNewBar();
}
