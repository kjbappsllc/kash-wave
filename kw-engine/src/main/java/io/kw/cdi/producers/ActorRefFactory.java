package io.kw.cdi.producers;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import io.kw.cdi.qualifiers.Actor;
import io.kw.cdi.qualifiers.RegisteredKWActor;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import java.util.Set;

@ApplicationScoped
public class ActorRefFactory {

    @Inject
    BeanManager beanManager;

    @Produces
    @Dependent
    @Actor(type = akka.actor.Actor.class)
    public ActorRef createActorRef(InjectionPoint injectionPoint, ActorSystemProducer actorSystemProducer) {
        final Actor actor = injectionPoint.getAnnotated().getAnnotation(Actor.class);
        ActorSystem actorSystem = actorSystemProducer.getActorSystem(actor.associatedSystem());
        Set<Bean<?>> actorBeans = beanManager.getBeans(actor.type(), new AnnotationLiteral<RegisteredKWActor>() {});
        Bean<?> bean = beanManager.resolve(actorBeans);
        CreationalContext<?> context = beanManager.createCreationalContext(bean);
        return actorSystem.actorOf(Props.create(
                actor.type().cast(beanManager.getReference(bean, actor.type(), context)).getClass()
        ));
    }
}
