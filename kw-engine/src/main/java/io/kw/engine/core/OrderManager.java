package io.kw.engine.core;

import io.kw.exceptions.TradeExecutionFailedException;
import io.kw.model.CurrencyPair;
import io.kw.service.BaseContext;
import io.kw.service.order.OrderContext;
import io.kw.service.order.OrderService;
import io.kw.service.order.OrderType;
import io.kw.service.order.TradeStatus;
import io.vavr.control.Try;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class OrderManager {

    @Inject
    OrderService orderService;

    public void createMarketOrder(BaseContext baseContext, CurrencyPair pair, int units) {
        System.out.println("make trade called");
        OrderContext context = OrderContext.builder()
                .baseContext(baseContext)
                .orderType(OrderType.MARKET)
                .pair(pair.name())
                .units(units)
                .build();
        System.out.println(context);
        Try.of(() -> orderService.execute(context))
                .onSuccess(wasTrueSuccess -> System.out.println("Was True Success: " + wasTrueSuccess))
                .onFailure(
                        TradeExecutionFailedException.class,
                        throwable -> {
                            TradeStatus status = throwable.getFailureStatus();
                            System.out.println("Failed Trade: " + status.getStatusCode());
                        })
                .onFailure(throwable -> System.out.println("Unknown Error: " + throwable.getLocalizedMessage()));
    }
}
