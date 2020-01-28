package io.kw.engine;

import io.kw.model.CurrencyPair;
import io.kw.service.TickStreamService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class KWEngine {

    @Inject
    TickStreamService tickStreamService;

    String apiKey = "Bearer a3f580b7f2357b31d139561a220b4aec-ff520f9ef1b1babf60781cd4ed8c014f";
    String accountID = "101-001-9159383-001";

    private void streamPrices(CurrencyPair interestedPair) {
        System.out.println("Starting Stream ...");
        tickStreamService.startStream(
                apiKey,
                accountID,
                interestedPair
        );
    }
}
