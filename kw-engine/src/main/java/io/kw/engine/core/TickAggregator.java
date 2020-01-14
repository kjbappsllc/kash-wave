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
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public final class TickAggregator {

    @Inject
    HistoricalPricesService historicalPricesService;

    private HashMap<Tuple2<CurrencyPair, Timeframe>, DataBuffer<Bar>> barMap;

    TickAggregator() {
        barMap = new HashMap<>();
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
        System.out.println(this.hashCode());
    }

    private boolean isNewBarFormed(Timeframe timeframe, Price newPrice) {
//        if (Timeframe.hasTimeChanged(timeframe, bars.get(0).getTimestamp(), newPrice.getTimestamp())) {
//            bars.add(Bar.builder()
//                    .close(newPrice)
//                    .open(newPrice)
//                    .low(newPrice)
//                    .high(newPrice)
//                    .timeframe(timeframe)
//                    .timestamp(newPrice.getTimestamp().truncatedTo(timeframe.getTruncatedUnit()))
//                    .build()
//            );
//            return true;
//        }
        return false;
    }
}
