package io.kw.service;

import io.kw.serviceClients.trade.oanda.OandaTradeClient;
import io.kw.serviceClients.trade.oanda.requests.CreateOrderRequest;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class OrderService {

    @Inject
    @RestClient
    OandaTradeClient tradeClient;

    public void createMarketOrder(String apiKey, String accountID, String currencyPair, double units) {
        CreateOrderRequest createOrderRequest = CreateOrderRequest.builder()
                .type("MARKET")
                .instrument(currencyPair)
                .units(Double.toString(units))
                .timeInForce("FOK")
                .build();
        tradeClient.createOrder(apiKey, accountID, createOrderRequest);
    }

    public int createLimitOrder() {
        return 0;
    }

    public int createTakeProfitOrder() {
        return 0;
    }

    public int createStopLossOrder() {
        return 0;
    }

    public int createTrailingStopLossOrder() {
        return 0;
    }
}
