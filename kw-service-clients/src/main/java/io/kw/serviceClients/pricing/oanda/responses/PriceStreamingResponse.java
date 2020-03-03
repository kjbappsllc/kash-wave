package io.kw.serviceClients.pricing.oanda.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PriceStreamingResponse {
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
    public static class PriceBucket {
        private String price;
        private int liquidity;
    }
}
