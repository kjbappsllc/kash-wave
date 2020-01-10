package io.kw.engine.core;

import io.kw.model.Bar;
import io.kw.model.CurrencyPair;
import io.kw.model.DataBuffer;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;

@ApplicationScoped
public class TickAggregator {
    private HashMap<CurrencyPair, DataBuffer<Bar>> bars;
}
