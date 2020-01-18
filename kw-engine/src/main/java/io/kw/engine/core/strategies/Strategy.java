package io.kw.engine.core.strategies;

import io.kw.engine.core.BarObserver;
import io.kw.engine.core.indicators.Indicator;
import io.kw.model.Bar;
import io.kw.model.CurrencyPair;
import io.kw.model.DataBuffer;
import io.kw.model.Timeframe;
import io.vavr.control.Try;
import lombok.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@RequiredArgsConstructor
public abstract class Strategy extends BarObserver {

    @Getter @Setter
    private @NonNull CurrencyPair pair;

    @Getter @Setter
    private @NonNull Timeframe timeframe;

    @Getter
    private @NonNull List<Indicator> indicators;

    @Getter
    private UUID guid = UUID.randomUUID();

    @Getter @Setter
    private boolean didInitialize = false;

    @Override
    public final void onTick(DataBuffer<Bar> bars) {
        if (didInitialize) {
            setBars(bars);
            _onTick();
        }
    }

    @Override
    public final void onInit(DataBuffer<Bar> bars) {
        setBars(bars);
        _onInit();
        setDidInitialize(true);
    }

    @Override
    public final void onNewBar() {
        _onNewBar();
    }

    private void runAsyncIndicatorAction(Consumer<Indicator> task) {
            Try.run(() ->
                    CompletableFuture.allOf(indicators
                    .stream()
                    .map(indicator -> CompletableFuture.runAsync(() -> task.accept(indicator)))
                    .toArray(CompletableFuture[]::new))
                    .get()
            ).onFailure(exception ->
                    System.out.println("Running indicators failed: " + exception.getLocalizedMessage())
            );
    }
}
