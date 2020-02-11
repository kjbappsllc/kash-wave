package io.kw.serviceClients.trade.oanda.requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName(value = "order")
public class CreateOrderRequest {
    private String instrument;
    private String units;
    private String priceBound;
    private String price;
    private String positionFill;
    private String timeInForce;
    private String gtdTime;
    private String triggerCondition;
    private String tradeID;
    private String distance;
    private @NonNull String type;
}