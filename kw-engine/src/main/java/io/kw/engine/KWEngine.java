package io.kw.engine;

import io.kw.engine.cdi.qualifiers.StrategyImplementation;
import io.kw.engine.core.TickAggregator;
import io.kw.engine.strategies.Strategy;
import io.kw.model.CurrencyPair;
import io.kw.model.Timeframe;
import io.kw.service.TickStreamService;
import io.vavr.control.Try;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class KWEngine {

    @Inject
    TickStreamService tickStreamService;

    @Inject
    TickAggregator tickAggregator;

    public void startStrategy(Strategy strategy, String apiKey, String accountID, CurrencyPair pair) {
        System.out.println("Start Strategy Called");
    }

    private void streamPrices(String apiKey, String accountID, CurrencyPair interestedPair) {
        Try.of(() -> tickAggregator.attemptAddNewPair(apiKey, interestedPair, Timeframe.M1).get())
                .onSuccess(didAddPair -> {
                    if (didAddPair) {
                        System.out.println("Currency Pair " + interestedPair + " added to be watched.");
                        tickStreamService.startStream(apiKey, accountID, interestedPair);
                    }
                })
                .onFailure(exception -> System.out.println("Could not add Currency Pair to be watched" + exception.getLocalizedMessage()));
    }
}
