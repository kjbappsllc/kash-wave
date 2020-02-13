package io.kw.service.order;

import org.apache.commons.chain.impl.ChainBase;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

@ApplicationScoped
public class OrderService extends ChainBase {

    @Inject
    Instance<OrderCommand> oandaOrder;

    @PostConstruct
    public void init() {
        for (OrderCommand orderCommand : oandaOrder) {
            System.out.println("Adding Command: " + orderCommand);
            addCommand(orderCommand);
        }
    }
}
