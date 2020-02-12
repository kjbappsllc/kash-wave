package io.kw.engine.core;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HistoricalManager {
    public void process() {
        System.out.println("Historical Manager Processed");
    }
}
