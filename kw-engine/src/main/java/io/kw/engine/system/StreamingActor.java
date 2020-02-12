package io.kw.engine.system;

import akka.actor.AbstractActor;
import io.kw.cdi.qualifiers.RegisteredKWActor;

@RegisteredKWActor
public class StreamingActor extends AbstractActor {

    StreamingActor() {}

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> System.out.println("Streaming Actor Received: " + s))
                .build();
    }
}
