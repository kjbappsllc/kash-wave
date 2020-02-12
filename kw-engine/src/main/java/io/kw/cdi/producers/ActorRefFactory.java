package io.kw.cdi.producers;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import io.kw.cdi.qualifiers.Actor;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

@ApplicationScoped
public class ActorRefFactory {

    @Inject
    @Any
    Instance<akka.actor.Actor> actors;

    @Produces
    @Dependent
    @Actor(type = akka.actor.Actor.class)
    public ActorRef createActorRef(InjectionPoint injectionPoint, ActorSystemProducer actorSystemProducer) {
        System.out.println("Actor Producer Started...");
        final Actor actor = injectionPoint.getAnnotated().getAnnotation(Actor.class);
        System.out.println("Actor information retrieved from annotation: " + actor.type() + " " + actor.associatedSystem());
        ActorSystem actorSystem = actorSystemProducer.getActorSystem(actor.associatedSystem());
        System.out.println("Actor System Retrieved: " + actorSystem);
        Instance<? extends akka.actor.Actor> actorInstance = actors.select(actor.type());
        System.out.println("Actor Instance gathered: " + actorInstance.isResolvable() + " " + actorInstance.isAmbiguous());
        return actorSystem.actorOf(Props.create(ActorRefInjector.class, actorInstance, actor.type()));
    }
}
