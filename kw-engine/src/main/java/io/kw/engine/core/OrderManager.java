package io.kw.engine.core;

import io.kw.service.order.OrderService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class OrderManager {

    @Inject
    OrderService orderService;

}
