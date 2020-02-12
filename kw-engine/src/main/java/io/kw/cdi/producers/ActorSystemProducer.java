package io.kw.cdi.producers;

import akka.actor.ActorSystem;
import io.kw.cdi.qualifiers.ActorSystemName;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class ActorSystemProducer {

    private Map<String, ActorSystem> actorSystemMap = new ConcurrentHashMap<>();

    @Produces
    @Dependent
    @ActorSystemName(name = ActorSystemName.DEFAULT_NAME)
    protected ActorSystem createActorSystem(InjectionPoint injectionPoint) {
        return getActorSystem(injectionPoint.getAnnotated().getAnnotation(ActorSystemName.class).name());
    }

    protected ActorSystem getActorSystem(String name) {
        if (actorSystemMap.containsKey(name))
            return actorSystemMap.get(name);
        return actorSystemMap.put(name, akka.actor.ActorSystem.create(name));
    }

    @PreDestroy
    protected void cleanup() {
        for (ActorSystem actorSystem : actorSystemMap.values()) {
            actorSystem.terminate();
        }
    }
}
