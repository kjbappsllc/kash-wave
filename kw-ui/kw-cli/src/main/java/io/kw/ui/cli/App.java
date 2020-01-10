package io.kw.ui.cli;

import io.kw.ui.cli.view.TerminalView;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class App {

    @Inject
    TerminalView terminalView;

    public void onAppStart(@Observes StartupEvent e) {
        System.out.println("Starting Application: " + e);
        terminalView.initView();
    }
}
