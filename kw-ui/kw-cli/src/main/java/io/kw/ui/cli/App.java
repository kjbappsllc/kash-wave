package io.kw.ui.cli;

import io.kw.engine.KWEngine;
import io.kw.engine.core.strategies.MACross;
import io.kw.model.Currency;
import io.kw.model.CurrencyPair;
import io.kw.model.Timeframe;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class App {

    @Inject
    KWEngine engine;

    public void onAppStart(@Observes StartupEvent e) {
        MACross maCross = new MACross(
                new CurrencyPair(Currency.EUR, Currency.USD),
                Timeframe.M1
        );
        engine.startStrategy(maCross);
    }
}
