package io.kw.serviceClients.pricing.oanda.responses;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PriceStreamingResponse implements Serializable {
    private String type;
    private String instrument;
    private String time;
    private Boolean tradeable;
    private String closeoutBid;
    private String closeoutAsk;
    private ArrayList<PriceBucket> bids;
    private ArrayList<PriceBucket> asks;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class PriceBucket implements Serializable {
        private String price;
        private int liquidity;
    }
}
