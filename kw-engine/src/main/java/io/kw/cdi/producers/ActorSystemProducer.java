package io.kw.cdi.producers;

import io.kw.cdi.qualifiers.ActorSystem;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class ActorSystemProducer {

    private Map<String, akka.actor.ActorSystem> actorSystemMap = new ConcurrentHashMap<String, akka.actor.ActorSystem>();

    @Produces
    @Dependent
    @ActorSystem(name = ActorSystem.DEFAULT_NAME)
    public akka.actor.ActorSystem createActorSystem(InjectionPoint injectionPoint) {
        return getActorSystem(injectionPoint.getAnnotated().getAnnotation(ActorSystem.class).name());
    }

    private akka.actor.ActorSystem getActorSystem(String name) {
        akka.actor.ActorSystem actorSystem = actorSystemMap.get(name);
        return actorSystem;
    }
}
