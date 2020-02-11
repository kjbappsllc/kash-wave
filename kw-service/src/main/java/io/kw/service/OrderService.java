package io.kw.service;

import io.kw.serviceClients.trade.oanda.OandaTradeClient;
import io.kw.serviceClients.trade.oanda.requests.CreateOrderRequest;
import io.kw.serviceClients.trade.oanda.responses.CreateOrderResponse;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class OrderService {

    @Inject
    @RestClient
    OandaTradeClient tradeClient;

    public int createMarketOrder(String apiKey, String accountID, String currencyPair, double units) {
        CreateOrderRequest createOrderRequest = CreateOrderRequest.builder()
                .type("MARKET")
                .instrument(currencyPair)
                .units(Double.toString(units))
                .timeInForce("FOK")
                .build();
        CreateOrderResponse res = Try
                .of(() -> tradeClient.createOrder(apiKey, accountID, createOrderRequest))
                .onFailure(throwable -> System.out.println(throwable.getLocalizedMessage()))
                .onSuccess(createOrderResponse -> System.out.println("Make trade Success"))
                .getOrNull();
        if (res == null) return -1;
        String fillId = res.getOrderFillTransaction().getId();
        return fillId == null ? -1 : Integer.parseInt(fillId);
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
