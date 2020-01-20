package io.kw.engine.core.strategies;

import io.kw.engine.core.BarObserver;
import io.kw.engine.core.indicators.Indicator;
import io.kw.model.Bar;
import io.kw.model.CurrencyPair;
import io.kw.model.DataBuffer;
import io.kw.model.Timeframe;
import io.vavr.control.Try;
import lombok.*;

import javax.enterprise.context.Dependent;
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

    @Override
    public final void onTick(final DataBuffer<Bar> bars) {
        if (isInitialized()) {
            setBars(bars);
            mapIndicators(indicator -> indicator.onTick(bars));
            _onTick();
            return;
        }
        System.out.println("Strategy is not initialized yet. (OnTick)");
    }

    @Override
    public final void onInit(final DataBuffer<Bar> bars) {
        setBars(bars);
        mapIndicators(indicator -> indicator.onInit(bars));
        _onInit();
        setInitialized(true);
    }

    @Override
    public final void onNewBar() {
        mapIndicators(Indicator::onNewBar);
        _onNewBar();
    }

    private void mapIndicators(Consumer<Indicator> task) {
            Try.run(() ->
                    CompletableFuture.allOf(indicators
                    .stream()
                    .map(indicator -> CompletableFuture.runAsync(() -> task.accept(indicator)))
                    .toArray(CompletableFuture[]::new))
                    .get()
            ).onFailure(exception ->
                    System.out.println("Running indicators failed: " + exception.getLocalizedMessage())
            );
        System.out.println("Populated Indicators.");
    }

    protected abstract void _onInit();
    protected abstract void _onTick();
    protected abstract void _onNewBar();
}
