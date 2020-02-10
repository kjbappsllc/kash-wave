package io.kw.serviceClients.trade.oanda.requests;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Builder(builderMethodName = "hidden")
@Data
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName(value = "order")
public class CreateOrderRequest {
    private @NonNull String instrument;
    private @NonNull String units;
    @Builder.Default private String timeInForce = "FOK";
    private @NonNull String type;

    public static CreateOrderRequestBuilder builder(String instrument, String units, String type) {
        return hidden().instrument(instrument).units(units).type(type);
    }
}