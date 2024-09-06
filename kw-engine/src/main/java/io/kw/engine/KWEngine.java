package io.kw.engine;

import akka.actor.ActorRef;
import static akka.pattern.Patterns.ask;
import io.kw.cdi.qualifiers.Actor;
import io.kw.engine.system.RootActor;
import io.kw.model.Currency;
import io.kw.model.CurrencyPair;
import io.kw.service.BaseContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.Future;

@ApplicationScoped
public class KWEngine {

    public static final String KW_SYSTEM = "kw-engine";

    @Inject
    @Actor(type = RootActor.class, associatedSystem = KWEngine.KW_SYSTEM)
    ActorRef rootActor;

    public void start(BaseContext context) {
        System.out.println("Kash Wave Engine is starting up ...");
        rootActor.tell(new RootActor.InitMessage(
                List.of(CurrencyPair.builder()
                        .base(Currency.EUR)
                        .quote(Currency.USD)
                        .delimiter("_")
                        .build()),
                context
        ), ActorRef.noSender());
    }
}