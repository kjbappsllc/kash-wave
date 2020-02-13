package io.kw.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Accessors(fluent = true)
@Builder
@Data
public class BaseContext {
    public enum Broker { OANDA }
    private @NonNull Broker broker;
    private @NonNull String apiToken;
    private @NonNull String accountID;
}
