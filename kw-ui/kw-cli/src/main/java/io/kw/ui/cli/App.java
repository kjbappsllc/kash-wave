package io.kw.ui.cli;

import io.kw.engine.core.PriceStreamingEngine;
import io.kw.model.bar.Timeframe;
import io.kw.model.currency.Currency;
import io.kw.model.currency.CurrencyPair;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.math.BigDecimal;

public class App {

    @Inject
    PriceStreamingEngine priceStreamingEngine;

    public void onAppStart(@Observes StartupEvent event) {
        System.out.println("Working: " + event.toString());
        priceStreamingEngine.startPricingStream(
                new CurrencyPair(
                        Currency.EUR,
                        Currency.USD,
                        new BigDecimal("0.2")
                ),
                Timeframe.M5
        );
    }
}
