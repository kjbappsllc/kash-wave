package io.kw.serviceClients.account.oanda.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountDetailsResponse {
    String lastTransactionID;
    OandaAccount account;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class OandaAccount {
        String id;
        String alias;
        String currency;
        Integer createdByUserID;
        String guaranteedStopLossOrderMode;
        Double marginRate;
        Integer openTradeCount;
        Integer openPositionCount;
        Integer pendingOrderCount;
        Boolean hedgingEnabled;
        String unrealizedPL;
        String NAV;
        String marginUsed;
        String marginAvailable;
        String positionValue;
        String marginCloseoutUnrealizedPL;
        String marginCloseoutNAV;
        String marginCloseoutMarginUsed;
        String marginCloseoutPercent;
        String marginCloseoutPositionValue;
        String withdrawalLimit;
        String marginCallMarginUsed;
        String marginCallPercent;
        String balance;
        String pl;
        String resettablePL;
        String financing;
        String commission;
        String dividend;
        String guaranteedExecutionFees;
        String marginCallEnterTime;
        Integer marginCallExtensionCount;
        String lastMarginCallExtensionTime;
        String lastTransactionID;
        List<TradeSummary>trades;
        List<Position>positions;
        List<Order> orders;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class TradeSummary {
        String id;
        String instrument;
        String price;
        String openTime;
        String state;
        String initialUnits;
        String initialMarginRequired;
        String currentUnits;
        String unrealizedPL;
        String marginUsed;
        String averageClosePrice;
        List<String> closingTransactionIDs;
        String financing;
        String dividend;
        String closeTime;
        ClientExtensions clientExtensions;
        String takeProfitOrderID;
        String stopLossOrderID;
        String trailingStopLossOrderID;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Order {
        String id;
        String createTime;
        String state;
        ClientExtensions clientExtensions;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Position {
        String instrument;
        String pl;
        String unrealizedPL;
        String marginUsed;
        String resettablePL;
        String financing;
        String commission;
        String dividend;
        String guaranteedExecutionFees;
        @JsonProperty("long")
        PositionSide longSide;
        @JsonProperty("short")
        PositionSide shortSide;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class ClientExtensions {
        String id;
        String tag;
        String comment;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class PositionSide {
        String units;
        String averagePrice;
        List<String> tradeIDs;
        String pl;
        String unrealizedPL;
        String resettablePL;
        String financing;
        String dividend;
        String guaranteedExecutionFees;
    }

}
