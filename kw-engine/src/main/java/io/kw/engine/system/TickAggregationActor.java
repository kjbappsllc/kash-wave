package io.kw.engine.system;

import akka.actor.AbstractActor;
import lombok.NoArgsConstructor;

import javax.enterprise.context.Dependent;

@Dependent
@NoArgsConstructor
public class TickAggregationActor extends AbstractActor {
    @Override
    public Receive createReceive() {
        return null;
    }
}
