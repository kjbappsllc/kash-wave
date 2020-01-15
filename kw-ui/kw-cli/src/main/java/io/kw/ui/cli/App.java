package io.kw.ui.cli;

import io.kw.engine.KWEngine;
import io.kw.model.Currency;
import io.kw.model.CurrencyPair;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class App {

    @Inject
    KWEngine engine;

    String apiToken = "Bearer a3f580b7f2357b31d139561a220b4aec-ff520f9ef1b1babf60781cd4ed8c014f";
    String accountID = "101-001-9159383-001";

    public void onAppStart(@Observes StartupEvent e) {
        engine.streamPrices(
                apiToken,
                accountID,
                new CurrencyPair(Currency.EUR, Currency.USD),
                new CurrencyPair(Currency.GBP, Currency.USD)
        );
    }
}
