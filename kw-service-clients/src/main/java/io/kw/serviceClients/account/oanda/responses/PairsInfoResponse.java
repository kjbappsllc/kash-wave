package io.kw.serviceClients.account.oanda.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PairsInfoResponse {
    List<Instrument> instruments;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Instrument {
        private String displayName;
        private int displayPrecision;
        private String marginRate;
        private String maximumOrderUnits;
        private String maximumPositionSize;
        private String maximumTrailingStopDistance;
        private String minimumTradeSize;
        private String minimumTrailingStopDistance;
        private String name;
        private int pipLocation;
        private int tradeUnitsPrecision;
        private String type;
    }
}
