package io.kw.service.order;

import io.kw.serviceClients.trade.oanda.OandaTradeClient;
import io.kw.serviceClients.trade.oanda.requests.CreateOrderRequest;
import io.kw.serviceClients.trade.oanda.responses.CreateOrderResponse;
import io.vavr.control.Try;
import org.apache.commons.chain.Context;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import static io.kw.service.BaseContext.Broker;

@Dependent
public class OandaOrderCommand extends OrderCommand {

    @Inject
    @RestClient
    OandaTradeClient tradeClient;

    @Override
    public boolean execute(Context context) throws Exception {
        OrderContext orderContext = getOrderContext(context);
        if (orderContext == null || orderContext.baseContext().broker() != Broker.OANDA)
            return false;
        int tradeOrder = executeOrder(orderContext);
        System.out.println("Trade ID: " + tradeOrder);
        return true;
    }

    private int executeOrder(OrderContext context) {
        switch(context.orderType()) {
            case MARKET: return createMarketOrder(
                    context.baseContext().apiToken(),
                    context.baseContext().accountID(),
                    context.pair(),
                    context.units()
            );
            case LIMIT: return 0;
            default: return -1;
        }
    }

    private int createMarketOrder(String apiKey, String accountID, String currencyPair, double units) {
        CreateOrderRequest createOrderRequest = CreateOrderRequest.builder()
                .type("MARKET")
                .instrument(currencyPair)
                .units(Double.toString(units))
                .timeInForce("FOK")
                .build();
        CreateOrderResponse res = Try
                .of(() -> tradeClient.createOrder(apiKey, accountID, createOrderRequest))
                .onFailure(throwable -> System.out.println("Trade is not a go: " + throwable.getLocalizedMessage()))
                .onSuccess(createOrderResponse -> System.out.println("Make trade Success"))
                .getOrNull();
        if (res == null) return -1;
        String fillId = res.getOrderFillTransaction().getId();
        return fillId == null ? -1 : Integer.parseInt(fillId);
    }
}
