package io.kw.engine.system;

import akka.actor.AbstractActor;
import io.kw.engine.core.HistoricalManager;
import lombok.NoArgsConstructor;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
@NoArgsConstructor
public class StreamingActor extends AbstractActor {

    @Inject
    HistoricalManager historicalManager;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> {
                    System.out.println("Streaming Actor Received: " + s);
                    historicalManager.process();
                })
                .build();
    }
}
