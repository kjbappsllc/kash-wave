package io.kw.service.order;

import io.kw.service.BaseContext;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.chain.impl.ContextBase;

@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@Builder
@Data
public class OrderContext extends ContextBase {
    private BaseContext baseContext;
    private OrderType orderType;
    private String pair;
    private int units;
}
