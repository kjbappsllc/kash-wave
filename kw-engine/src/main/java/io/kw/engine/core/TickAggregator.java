package io.kw.engine.core;

import io.kw.model.*;
import io.kw.service.HistoricalPricesService;
import io.kw.service.cdi.qualifiers.TickReceived;
import io.vavr.Tuple;
import io.vavr.Tuple2;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@ApplicationScoped
public final class TickAggregator {

    @Inject
    HistoricalPricesService historicalPricesService;

    private ConcurrentHashMap<Tuple2<CurrencyPair, Timeframe>, DataBuffer<Bar>> barMap;

    TickAggregator() {
        barMap = new ConcurrentHashMap<>();
    }

    public CompletableFuture<Boolean> attemptAddNewPair(String apiKey, CurrencyPair pair, Timeframe tf) {
        Tuple2<CurrencyPair, Timeframe> potentialNewPair = Tuple.of(pair, tf);
        if (barMap.containsKey(potentialNewPair))
            return CompletableFuture.completedFuture(Boolean.TRUE);
        return CompletableFuture.supplyAsync(() -> {
            DataBuffer<Bar> historicalBars = historicalPricesService.retrieveHistoricalData(apiKey, tf, pair);
            barMap.put(Tuple.of(pair, tf), historicalBars);
            return Boolean.TRUE;
        }).exceptionally(exception -> {
            System.out.println("Problem adding Pair to List");
            return Boolean.FALSE;
        });
    }

    private void tickReceived(@Observes @TickReceived @Priority(0) Price tick) {
        System.out.println("Aggregating tick information: " + tick);
        System.out.println("Instance: " + this.hashCode());
        barMap.entrySet()
                .parallelStream()
                .forEach(pairTime -> {
                    if (pairTime.getKey()._1() == tick.getCurrencyPair()) {
                        var isNewBar = isNewBarFormed (
                                pairTime.getValue().get(0),
                                pairTime.getKey()._2(),
                                tick
                        );
                        if (isNewBar) {
                            System.out.println("New Bar For Pair: " + pairTime.getKey()._1());
                        }
                    }
                });
    }

    private boolean isNewBarFormed(Bar currentBar, Timeframe timeframe, Price newPrice) {
        return Timeframe.hasTimeChanged (
                timeframe,
                currentBar.getTimestamp(),
                newPrice.getTimestamp()
        );
    }
}
