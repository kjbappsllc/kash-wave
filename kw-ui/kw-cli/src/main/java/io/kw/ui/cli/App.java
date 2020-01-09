package io.kw.ui.cli;

import io.kw.engine.core.PriceStreamingEngine;
import io.kw.model.bar.Timeframe;
import io.kw.model.currency.Currency;
import io.kw.model.currency.CurrencyPair;
import io.kw.ui.cli.view.TerminalView;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.math.BigDecimal;

@ApplicationScoped
public class App {

    @Inject
    TerminalView terminalView;

    public void onAppStart(@Observes StartupEvent e) {
        System.out.println("Starting Application: " + e);
        terminalView.initView();
    }
}
