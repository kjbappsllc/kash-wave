package io.kw.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

import java.util.List;

@Value
@Builder(toBuilder = true)
@Accessors(fluent = true, chain = true)
public class Account {
    @NonNull Currency homeCurrency;
    double equity;
    double balance;
    double leverage;
    String accountID;
    int openPositionCount;
    int pendingOrdersCount;
    int name;
    List<Position> positions;
}
