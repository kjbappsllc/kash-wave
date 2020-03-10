package io.kw.service.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TradeStatus {
    TRADE_UNFILLED(-1), TRADE_FAILURE(-2), TRADE_SUCCESS(0);
    private int statusCode;
}
