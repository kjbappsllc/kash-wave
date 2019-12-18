package io.kw.ui.cli;

import io.kw.engine.core.PriceStreamingEngine;
import io.kw.model.bar.Timeframe;
import io.kw.model.currency.Currency;
import io.kw.model.currency.CurrencyPair;
import org.jboss.weld.environment.se.bindings.Parameters;
import org.jboss.weld.environment.se.events.ContainerInitialized;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class App {

    @Inject
    PriceStreamingEngine priceStreamingEngine;

    public void onAppStart(@Observes ContainerInitialized event,
                           @Parameters List<String> args) {
        System.out.println("Working: " + args);
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
