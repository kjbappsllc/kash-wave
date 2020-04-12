package io.kw.engine.system;

import akka.actor.AbstractActor;
import io.kw.engine.core.HistoricalManager;
import io.kw.model.Currency;
import io.kw.model.CurrencyPair;
import io.kw.service.BaseContext;
import io.kw.service.streaming.StreamingContext;
import io.kw.service.streaming.StreamingService;
import io.vavr.control.Try;
import lombok.NoArgsConstructor;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;

@Dependent
@NoArgsConstructor
public class StreamingActor extends AbstractActor {

    @Inject
    StreamingService streamingService;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, s -> System.out.println("Streaming Actor Received: " + s))
                .build();
    }

    public void startStream(BaseContext baseContext) {
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
                .tickCallback(System.out::println)
                .build();
        Try.of(() -> streamingService.execute(context));
    }
}
