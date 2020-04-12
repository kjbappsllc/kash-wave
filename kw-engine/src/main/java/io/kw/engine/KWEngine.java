package io.kw.engine;

import akka.actor.ActorRef;
import io.kw.cdi.qualifiers.Actor;
import io.kw.engine.system.RootActor;
import io.kw.exceptions.TradeExecutionFailedException;
import io.kw.model.Currency;
import io.kw.model.CurrencyPair;
import io.kw.model.Tick;
import io.kw.service.BaseContext;
import io.kw.service.order.OrderContext;
import io.kw.service.order.OrderService;
import io.kw.service.order.OrderType;
import io.kw.service.order.TradeStatus;
import io.kw.service.streaming.StreamingContext;
import io.vavr.control.Try;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

import static io.kw.service.BaseContext.Broker;

@ApplicationScoped
public class KWEngine {

    public static final String KW_SYSTEM = "kw-engine";

    @Inject
    @Actor(type = RootActor.class, associatedSystem = KWEngine.KW_SYSTEM)
    ActorRef rootActor;

    @Inject
    OrderService orderService;

    public void startUp(BaseContext context) {
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