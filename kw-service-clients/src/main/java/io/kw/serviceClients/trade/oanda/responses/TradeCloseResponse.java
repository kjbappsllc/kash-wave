package io.kw.serviceClients.trade.oanda.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TradeCloseResponse {
    //orderCreateTransaction : (MarketOrderTransaction),
    // orderFillTransaction : (OrderFillTransaction),
    // orderCancelTransaction : (OrderCancelTransaction),
    // relatedTransactionIDs : (Array[TransactionID]),
    String lastTransactionID;
    String errorMessage;
    String errorCode;
}
