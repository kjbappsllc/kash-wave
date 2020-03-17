package io.kw.engine.system;

import akka.actor.AbstractActor;
import io.kw.engine.core.HistoricalManager;
import lombok.NoArgsConstructor;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
@NoArgsConstructor
public class RootActor extends AbstractActor {

    @Inject
    HistoricalManager historicalManager;

    @Override
    public Receive createReceive() {
        return null;
    }
}
