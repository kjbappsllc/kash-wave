package io.kw.service;

import io.kw.model.Bar;
import io.kw.model.CurrencyPair;
import io.kw.model.DataBuffer;
import io.kw.model.Timeframe;
import io.kw.serviceClients.historical.oanda.OandaHistoricalPricesClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;

public class HistoricalPricesService {

    @Inject
    @RestClient
    OandaHistoricalPricesClient historicalPricesClient;

    public DataBuffer<Bar> retrieveHistoricalData(String accountId, String apiToken, Timeframe tf, CurrencyPair pair) {
        return new DataBuffer<>();
    }

}
