package io.kw.engine;

import io.kw.engine.core.TickAggregator;
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

    public void streamPrices(String apiKey, String accountID, CurrencyPair... interestedPairs) {
        Try.of(() -> tickAggregator.attemptAddNewPair(apiKey, interestedPairs[0], Timeframe.M1).get())
                .onSuccess(didAddPair -> {
                    if (didAddPair) {
                        System.out.println("Currency Pair " + interestedPairs[0] + " added to be watched.");
                        tickStreamService.startStream(apiKey, accountID, interestedPairs[0]);
                    }
                });
    }
}
