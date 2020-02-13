package io.kw.service.order;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

public abstract class OrderCommand implements Command {
    protected OrderContext getOrderContext(Context context) {
        if (!(context instanceof OrderContext)) return null;
        return (OrderContext) context;
    }
}
