package io.kw.engine;

import akka.actor.ActorRef;
import io.kw.cdi.qualifiers.Actor;
import io.kw.engine.system.StreamingActor;
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
import io.kw.service.streaming.StreamingService;
import io.vavr.control.Try;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.util.List;

import static io.kw.service.BaseContext.*;

@ApplicationScoped
public class KWEngine {

    public static final String KW_SYSTEM = "kw-engine";

    @Inject
    @Actor(type = StreamingActor.class, associatedSystem = KWEngine.KW_SYSTEM)
    ActorRef streamingActor;

    @Inject
    OrderService orderService;

    @Inject
    StreamingService streamingService;

    BaseContext baseContext;

    String apiKey = "Bearer a3f580b7f2357b31d139561a220b4aec-ff520f9ef1b1babf60781cd4ed8c014f";
    String accountID = "101-001-9159383-001";

    KWEngine() {
        baseContext = BaseContext.builder()
                .accountID(accountID)
                .apiToken(apiKey)
                .broker(Broker.OANDA)
                .build();
    }

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
                .onFailure(
                        TradeExecutionFailedException.class,
                        throwable -> {
                            TradeStatus status = throwable.getFailureStatus();
                            System.out.println("Failed Trade: " + status.getStatusCode());
                        })
                .onFailure(throwable -> System.out.println("Unknown Error: " + throwable.getLocalizedMessage()));

    }

    public void startStream() {
        CurrencyPair pair = CurrencyPair
                .builder()
                .base(Currency.EUR)
                .quote(Currency.USD)
                .marginRate(0.00)
                .precision(5)
                .pipLocation(-4)
                .build();
        StreamingContext context = StreamingContext.builder()
                .baseContext(baseContext)
                .currencies(List.of(pair))
                .tickCallback(this::tickCB)
                .build();
        Try.of(() -> streamingService.execute(context));
    }

    public void tickCB(Tick tick) {
        System.out.println("Received Tick From Callback: " + tick);
    }
}
