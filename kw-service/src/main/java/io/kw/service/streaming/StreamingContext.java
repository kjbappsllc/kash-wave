package io.kw.service.streaming;

import io.kw.model.CurrencyPair;
import io.kw.model.Tick;
import io.kw.service.BaseContext;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.chain.impl.ContextBase;

import java.util.List;
import java.util.function.Consumer;

@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@Builder
@Data
public class StreamingContext extends ContextBase {
    private BaseContext baseContext;
    private List<CurrencyPair> currencies;
    private Consumer<Tick> tickCallback;
}
