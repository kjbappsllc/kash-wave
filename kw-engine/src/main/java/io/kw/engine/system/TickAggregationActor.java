package io.kw.engine.system;

import akka.actor.AbstractActor;

import javax.enterprise.context.Dependent;

@Dependent
public class TickAggregationActor extends AbstractActor {
    @Override
    public Receive createReceive() {
        return null;
    }
}
