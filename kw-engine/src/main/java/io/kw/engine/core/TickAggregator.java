package io.kw.engine.core;

import io.kw.engine.cdi.qualifiers.NewBar;
import io.kw.engine.cdi.qualifiers.OnInit;
import io.kw.engine.cdi.qualifiers.OnTick;
import io.kw.model.*;
import io.kw.service.HistoricalPricesService;
import io.kw.service.cdi.qualifiers.TickReceived;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.Tuple3;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class TickAggregator {

    @Inject
    HistoricalPricesService historicalPricesService;

    @Inject
    @NewBar
    Event<Tuple2<CurrencyPair, Timeframe>> newBarEvent;

    @Inject
    @OnTick
    Event<Tuple3<CurrencyPair, Timeframe, Buffer<Bar>>> onTickEvent;

    @Inject
    @OnInit
    Event<Tuple2<UUID, Buffer<Bar>>> onInitEvent;

    private ConcurrentHashMap<Tuple2<CurrencyPair, Timeframe>, Buffer<Bar>> barMap;

    TickAggregator() {
        barMap = new ConcurrentHashMap<>();
    }

    public CompletableFuture<Boolean> attemptAddNewPair(String apiKey, CurrencyPair pair, Timeframe tf, UUID guid) {
        Tuple2<CurrencyPair, Timeframe> potentialNewPair = Tuple.of(pair, tf);
        if (barMap.containsKey(potentialNewPair))
            return CompletableFuture.completedFuture(true);
        return CompletableFuture.supplyAsync(() -> {
            Buffer<Bar> historicalBars = historicalPricesService.retrieveHistoricalData(apiKey, tf, pair);
            barMap.put(Tuple.of(pair, tf), historicalBars);
            onInitEvent.fire(Tuple.of(guid, historicalBars));
            return true;
        }).exceptionally(exception -> {
            System.out.println("Problem adding Pair to List, Exception: " + exception.getLocalizedMessage());
            exception.printStackTrace();
            return false;
        });
    }

    void tickReceived(@Observes @TickReceived @Priority(0) Price tick) {
        barMap.entrySet()
                .parallelStream()
                .filter(pairTime -> isObservedCurrency(tick, pairTime))
                .forEach(pairTime -> {
//                    boolean newBarIsFormed = isNewBarFormed(
//                            pairTime.getValue().at(0, true),
//                            pairTime.getKey()._2(),
//                            tick
//                    );
//                    if (newBarIsFormed) {
//                        createNewBar(tick, pairTime);
//                    } else {
//                        updateCurrentBar(tick, pairTime);
//                    }
//                    fireEvents(pairTime, newBarIsFormed);
                });
    }

    private void fireEvents(Map.Entry<Tuple2<CurrencyPair, Timeframe>, Buffer<Bar>> pairTime, boolean newBarIsFormed) {
        onTickEvent.fire(
                Tuple.of(pairTime.getKey()._1(), pairTime.getKey()._2(), pairTime.getValue())
        );
        if (newBarIsFormed) newBarEvent.fire(Tuple.of(pairTime.getKey()._1(), pairTime.getKey()._2()));
    }

    private void updateCurrentBar(Price tick, Map.Entry<Tuple2<CurrencyPair, Timeframe>, Buffer<Bar>> pairTime) {
//        Bar currentBar = pairTime.getValue().at(0, true);
//        long currentTickVolume = currentBar.volume();
//        currentBar.volume(currentTickVolume + 1);
//        currentBar.high(tick);
//        currentBar.low(tick);
//        currentBar.close(tick);
    }

    private boolean isObservedCurrency(Price tick, Map.Entry<Tuple2<CurrencyPair, Timeframe>, Buffer<Bar>> pairTime) {
        return pairTime.getKey()._1() == tick.getCurrencyPair();
    }

    private void createNewBar(Price tick, Map.Entry<Tuple2<CurrencyPair, Timeframe>, Buffer<Bar>> pairTime) {
        System.out.println("New Bar For Pair: " + pairTime.getKey()._1());
        Timeframe timeframe = pairTime.getKey()._2();
        Bar newBar = Bar.builder()
                .close(tick)
                .low(tick)
                .high(tick)
                .open(tick)
                .volume(1)
                .timeframe(timeframe)
                .timestamp(tick.getTimestamp().truncatedTo(timeframe.getTruncatedUnit()))
                .build();
        pairTime.getValue().add(newBar);
    }

    private boolean isNewBarFormed(Bar currentBar, Timeframe timeframe, Price newPrice) {
        return Timeframe.hasTimeChanged (
                timeframe,
                currentBar.timestamp(),
                newPrice.getTimestamp()
        );
    }
}
