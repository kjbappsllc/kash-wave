package io.kw.engine;

import akka.actor.ActorRef;
import io.kw.cdi.qualifiers.Actor;
import io.kw.engine.system.StreamingActor;
import io.kw.service.BaseContext;
import io.kw.service.order.OrderContext;
import io.kw.service.order.OrderService;
import io.kw.service.order.OrderType;
import io.vavr.control.Try;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static io.kw.service.BaseContext.*;

@ApplicationScoped
public class KWEngine {

    public static final String KW_SYSTEM = "kw-engine";

    @Inject
    @Actor(type = StreamingActor.class, associatedSystem = KWEngine.KW_SYSTEM)
    ActorRef streamingActor;

    @Inject
    OrderService orderService;

    String apiKey = "Bearer a3f580b7f2357b31d139561a220b4aec-ff520f9ef1b1babf60781cd4ed8c014f";
    String accountID = "101-001-9159383-001";

    public void startUp() {
        streamingActor.tell("CDI", ActorRef.noSender());
    }
    public void makeTrade() {
        System.out.println("make trade called");
        BaseContext baseContext = BaseContext.builder()
                .accountID(accountID)
                .apiToken(apiKey)
                .broker(Broker.OANDA)
                .build();
        OrderContext context = OrderContext.builder()
                .baseContext(baseContext)
                .orderType(OrderType.MARKET)
                .pair("EUR_USD")
                .units(-10000)
                .build();
        System.out.println(context);
        Try.of(() -> orderService.execute(context))
                .onSuccess(wasTrueSuccess -> System.out.println("Was True Success: " + wasTrueSuccess))
                .onFailure(throwable -> System.out.println("Failed Trade: " + throwable.getLocalizedMessage()));

    }
}
