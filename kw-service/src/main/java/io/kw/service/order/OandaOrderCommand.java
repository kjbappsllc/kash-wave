package io.kw.service.order;

import io.kw.exceptions.TradeExecutionFailedException;
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
    public boolean execute(Context context) throws TradeExecutionFailedException {
        OrderContext orderContext = getOrderContext(context);
        if (orderContext == null || orderContext.baseContext().broker() != Broker.OANDA)
            return false;
        TradeStatus status = executeOrder(orderContext);
        if (status != TradeStatus.TRADE_SUCCESS)
            throw new TradeExecutionFailedException(status);
        return true;
    }

    private TradeStatus executeOrder(OrderContext context) {
        switch(context.orderType()) {
            case MARKET: return createMarketOrder(
                    context.baseContext().apiToken(),
                    context.baseContext().accountID(),
                    context.pair(),
                    context.units()
            );
            case LIMIT: return TradeStatus.TRADE_SUCCESS;
            default: return TradeStatus.TRADE_FAILURE;
        }
    }

    private TradeStatus createMarketOrder(String apiKey, String accountID, String currencyPair, double units) {
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
        if (res == null) return TradeStatus.TRADE_FAILURE;
        String fillId = res.getOrderFillTransaction().getId();
        System.out.println("Trade ID: " + fillId);
        return fillId == null ? TradeStatus.TRADE_UNFILLED : TradeStatus.TRADE_SUCCESS;
    }
}
