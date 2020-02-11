package io.kw.serviceClients.trade.oanda.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateOrderResponse {
    OrderCreateType orderCreateTransaction;
    OrderFillType orderFillTransaction;

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrderCreateType extends TransactionType {
        String reason;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrderFillType extends TransactionType {
        OpenTrade tradeOpened;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class OpenTrade {
        String tradeID;
        String units;
        String price;
        String initialMarginRequired;
    }

    @Data
    private static abstract class TransactionType {
        String id;
        String time;
        String accountID;
        String batchID;
        String requestID;
        String units;
        String type;
        String instrument;
    }
}
