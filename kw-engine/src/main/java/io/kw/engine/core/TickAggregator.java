package io.kw.engine.core;

import io.kw.model.*;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;

@ApplicationScoped
public class TickAggregator {
    private HashMap<CurrencyPair, DataBuffer<Bar>> bars;
    TickAggregator() {
        bars = new HashMap<>();
    }

    private boolean isNewBarFormed(Timeframe timeframe, Price newPrice) {
//        if (Timeframe.hasTimeChanged(timeframe, bars.get(0).getTimestamp(), newPrice.getTimestamp())) {
//            bars.add(Bar.builder()
//                    .close(newPrice)
//                    .open(newPrice)
//                    .low(newPrice)
//                    .high(newPrice)
//                    .timeframe(timeframe)
//                    .timestamp(newPrice.getTimestamp().truncatedTo(timeframe.getTruncatedUnit()))
//                    .build()
//            );
//            return true;
//        }
        return false;
    }
}
