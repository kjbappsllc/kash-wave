package io.kw.ui.cli;

import io.kw.engine.KWEngine;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class App {

    @Inject
    KWEngine engine;

    void onAppStart(@Observes StartupEvent e) {
        System.out.println("App Started");
        engine.makeTrade();
    }
}
