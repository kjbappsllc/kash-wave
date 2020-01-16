package io.kw.engine;

import io.kw.engine.cdi.qualifiers.StrategyImplementation;
import io.kw.engine.core.TickAggregator;
import io.kw.engine.strategies.Strategy;
import io.kw.model.CurrencyPair;
import io.kw.model.Timeframe;
import io.kw.service.TickStreamService;
import io.vavr.control.Try;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@ApplicationScoped
public class KWEngine {

    @Inject
    TickStreamService tickStreamService;

    @Inject
    TickAggregator tickAggregator;

    @Inject
    Instance<Strategy> strategy;

    String apiKey = "Bearer a3f580b7f2357b31d139561a220b4aec-ff520f9ef1b1babf60781cd4ed8c014f";
    String accountID = "101-001-9159383-001";

    public void startStrategy(Strategy strategy, CurrencyPair pair) {
        System.out.println("Start Strategy Called");
    }

    private void streamPrices(CurrencyPair interestedPair) {
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
