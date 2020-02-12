package io.kw.cdi.producers;

import akka.actor.Actor;
import akka.actor.IndirectActorProducer;
import lombok.AllArgsConstructor;
import lombok.Value;

import javax.enterprise.inject.Instance;

@Value
@AllArgsConstructor
public class ActorRefInjector implements IndirectActorProducer {

    Instance<? extends Actor> actorInstance;
    Class<? extends Actor> actorClass;

    @Override
    public Actor produce() {
        System.out.println(actorInstance);
        return actorInstance.get();
    }

    @Override
    public Class<? extends Actor> actorClass() {
        System.out.println(actorClass);
        return actorClass;
    }
}
