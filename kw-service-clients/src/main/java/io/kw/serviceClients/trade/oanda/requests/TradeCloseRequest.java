package io.kw.serviceClients.trade.oanda.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TradeCloseRequest {
    private String units;
}
