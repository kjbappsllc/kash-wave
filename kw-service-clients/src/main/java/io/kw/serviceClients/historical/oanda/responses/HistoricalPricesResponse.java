package io.kw.serviceClients.historical.oanda.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class HistoricalPricesResponse implements Serializable {
    private List<Candles> candles;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Candles {
        private OHLC ask;
        private OHLC bid;
        private OHLC mid;
        private Boolean complete;
        private String time;
        private Integer volume;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class OHLC {
        private String o;
        private String h;
        private String l;
        private String c;
    }
}
