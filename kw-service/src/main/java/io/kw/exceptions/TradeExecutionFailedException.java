package io.kw.exceptions;

import io.kw.service.order.TradeStatus;
import lombok.Getter;

@Getter
public class TradeExecutionFailedException extends Exception {
    private TradeStatus failureStatus;
    public TradeExecutionFailedException(TradeStatus failureStatus) {
        super();
        this.failureStatus = failureStatus;
    }
}
