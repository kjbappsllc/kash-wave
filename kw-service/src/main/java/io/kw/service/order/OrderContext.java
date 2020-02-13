package io.kw.service.order;

import io.kw.service.BaseContext;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.ContextBase;

@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@Builder
@Data
public class OrderContext extends ContextBase {
    private BaseContext baseContext;
    public static String BASE_CONTEXT_KEY = "baseContext";

    private OrderType orderType;
    public static String ORDER_TYPE_KEY = "orderType";

    private String pair;
    public static String PAIR_KEY = "pair";

    private int units;
    public static String UNITS_KEY = "units";
}
