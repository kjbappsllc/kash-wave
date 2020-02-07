package io.kw.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

import java.util.List;

@Value
@Builder(toBuilder = true)
@Accessors(fluent = true, chain = true)
public class Position {
    double lots;
    double averagePrice;
    List<String> tradeIDs;
    double marginUsed;
    CurrencyPair instrument;
    double profitLoss;
}
