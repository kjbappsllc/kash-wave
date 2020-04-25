package io.kw.ui.cli;

import io.kw.engine.KWEngine;
import io.kw.service.BaseContext;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.annotations.QuarkusMain;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

@QuarkusMain
public class CLIMain implements QuarkusApplication {

    @Inject
    KWEngine engine;

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
        engine.start(baseContext);
        return 0;
    }
}
