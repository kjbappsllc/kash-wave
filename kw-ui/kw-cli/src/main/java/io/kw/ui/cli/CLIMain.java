package io.kw.ui.cli;

import io.kw.engine.KWEngine;
import io.kw.model.Currency;
import io.kw.model.CurrencyPair;
import io.kw.service.BaseContext;
import io.kw.service.streaming.OandaStreamingCommand;
import io.kw.service.streaming.StreamingContext;
import io.kw.service.streaming.StreamingService;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

import javax.inject.Inject;
import java.util.List;

@QuarkusMain
public class CLIMain implements QuarkusApplication {

    @Inject
    StreamingService engine;

    String apiKey = "Bearer a3f580b7f2357b31d139561a220b4aec-ff520f9ef1b1babf60781cd4ed8c014f";
    String accountID = "101-001-9159383-001";

    public static void main(String[] args) {
        Quarkus.run(CLIMain.class, args);
    }

    @Override
    public int run(String... args) throws Exception {
        System.out.println("App Started");
        BaseContext baseContext = BaseContext.builder()
                .accountID(accountID)
                .apiToken(apiKey)
                .broker(BaseContext.Broker.OANDA)
                .build();
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
        engine.execute(context);
        return 0;
    }
}
