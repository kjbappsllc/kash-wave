package io.kw.engine.core;

import io.kw.model.*;
import io.kw.service.cdi.qualifiers.TickReceived;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.util.HashMap;

@ApplicationScoped
public class TickAggregator {
    private HashMap<CurrencyPair, DataBuffer<Bar>> bars;
    TickAggregator() {
        bars = new HashMap<>();
    }

    private void tickReceived(@Observes @TickReceived @Priority(0) Price tick) {
        System.out.println("Aggregating tick information");
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
