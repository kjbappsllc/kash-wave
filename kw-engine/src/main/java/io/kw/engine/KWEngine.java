package io.kw.engine;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import io.kw.engine.system.StreamingActor;
import io.kw.service.OrderService;
import io.kw.service.StreamingService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class KWEngine {

    @Inject
    OrderService orderService;

    @Inject
    StreamingService tickStreamService;

    String apiKey = "Bearer a3f580b7f2357b31d139561a220b4aec-ff520f9ef1b1babf60781cd4ed8c014f";
    String accountID = "101-001-9159383-001";
    ActorSystem kwEngine = null;

    public void startUp() {
        kwEngine = ActorSystem.create("kash-wave");
        ActorRef streamingActor = kwEngine.actorOf(Props.create(StreamingActor.class));
        streamingActor.tell("Hello", ActorRef.noSender());
        kwEngine.terminate();
    }

    public void makeTrade() {
        int tradeID  = orderService.createMarketOrder(apiKey, accountID, "EUR_USD", -15000);
        if (tradeID == -1) {
            System.out.println("make Trade failed ...");
            return;
        }
        System.out.println("Made Trade - ID: " + tradeID);
    }
}
