package io.kw.engine.core;

import io.kw.engine.cdi.qualifiers.NewBar;
import io.kw.engine.cdi.qualifiers.OnInit;
import io.kw.engine.cdi.qualifiers.OnTick;
import io.kw.engine.core.strategies.Strategy;
import io.kw.model.Bar;
import io.kw.model.CurrencyPair;
import io.kw.model.DataBuffer;
import io.kw.model.Timeframe;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.control.Try;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class StrategyManager {

    @Inject
    TickAggregator tickAggregator;

    private List<Strategy> strategies = new ArrayList<>(3);

    public CompletableFuture<Boolean> initStrategy(Strategy s, String apiKey) {
        if (strategyAlreadyExists(s)) return CompletableFuture.completedFuture(false);
        return CompletableFuture.supplyAsync(() -> {
            strategies.add(s);
            Try<Boolean> tryDidAdd = Try.of(() -> tickAggregator.attemptAddNewPair(apiKey, s.getPair(), s.getTimeframe(), s.getGuid()).get());
            if (tryDidAdd.isFailure() || !tryDidAdd.get()) {
                strategies.remove(s);
                return false;
            }
            return true;
        });
    }

    void onTick(@Observes @OnTick Tuple3<CurrencyPair, Timeframe, DataBuffer<Bar>> newTick) {
        strategies
                .stream()
                .filter(strategy -> strategy.getPair().equals(newTick._1()) &&
                        strategy.getTimeframe().equals(newTick._2()))
                .forEach(strategy -> strategy._onTick(newTick._3()));
    }

    void onNewBar(@Observes @NewBar Tuple2<CurrencyPair, Timeframe> bar) {
        System.out.println("Received New Bar");
        strategies
                .stream()
                .filter(strategy -> strategy.getPair().equals(bar._1()) &&
                        strategy.getTimeframe().equals(bar._2()))
                .forEach(Strategy::onNewBar);
    }

    void onInit(@Observes @OnInit Tuple2<UUID, DataBuffer<Bar>> data) {
        strategies
                .stream()
                .filter(strategy -> strategy.getGuid().equals(data._1()))
                .findFirst()
        .ifPresentOrElse(
                strategy -> strategy._onInit(data._2()),
                () -> System.out.println("Issue with onInit of strategy")
        );
    }

    private boolean strategyAlreadyExists(Strategy s) {
        return strategies
                .stream()
                .anyMatch(s2 -> s.getPair().equals(s2.getPair()) &&
                        s.getTimeframe().equals(s2.getTimeframe()));
    }
}
