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
        System.out.println("Getting Actor System...");
        if (actorSystemMap.containsKey(name)) {
            System.out.println("Actor System exists in map");
            return actorSystemMap.get(name);
        }
        System.out.println("Actor System does not exists in map");
        System.out.println(name);
        ActorSystem newSystem = ActorSystem.create(name);
        System.out.println("Created System: " + newSystem.getClass().getSimpleName());
        actorSystemMap.put(name, newSystem);
        return newSystem;
    }

    @PreDestroy
    protected void cleanup() {
        for (ActorSystem actorSystem : actorSystemMap.values()) {
            actorSystem.terminate();
        }
    }
}
