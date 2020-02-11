package io.kw.cdi.producers;

import akka.actor.ActorRef;
import io.kw.cdi.qualifiers.Actor;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

@ApplicationScoped
public class ActorRefFactory {
    @Produces
    @Dependent
    @Actor(type = akka.actor.Actor.class)
    public ActorRef createActorRef(InjectionPoint injectionPoint) {
        return null;
    }
}
