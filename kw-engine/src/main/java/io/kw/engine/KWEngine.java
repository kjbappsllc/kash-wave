package io.kw.engine;

import io.kw.engine.core.TickAggregator;
import io.kw.model.CurrencyPair;
import io.kw.service.TickStreamService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public final class KWEngine {
    @Inject
    TickAggregator tickAggregator;

    @Inject
    TickStreamService tickStreamService;

    public void streamPrices(CurrencyPair... interestedPairs) {
        
    }
}
