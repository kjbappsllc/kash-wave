package io.kw.engine.system;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import io.kw.engine.core.HistoricalManager;
import io.kw.model.CurrencyPair;
import io.kw.service.BaseContext;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;

@Dependent
@NoArgsConstructor
public class RootActor extends AbstractActor {

    @AllArgsConstructor
    @Value
    public static class InitMessage {
        List<CurrencyPair> pairs;
        BaseContext baseContext;
    }

    @Override
    public Receive createReceive() {
        return new ReceiveBuilder()
                .match(InitMessage.class, (initMessage) -> {
                    System.out.println("Root Actor Received Init Message");
                })
                .build();
    }
}
