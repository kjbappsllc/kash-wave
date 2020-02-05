package io.kw.serviceClients.trade.oanda.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TradeModifyRequest {

    PriceDetails takeProfit;
    PriceDetails stopLoss;

    @AllArgsConstructor
    @Data
    public static class PriceDetails {
        String price;
        String timeInForce;
        String gtdTime;
    }
}
