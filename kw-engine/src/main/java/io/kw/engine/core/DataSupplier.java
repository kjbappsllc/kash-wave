package io.kw.engine.core;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DataSupplier {

    public boolean supplyTo(IDataObserver observer) {
        return false;
    }
}
