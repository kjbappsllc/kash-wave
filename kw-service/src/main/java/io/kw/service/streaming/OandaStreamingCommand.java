package io.kw.service.streaming;

import io.kw.service.BaseContext;
import io.kw.serviceClients.pricing.oanda.OandaPriceStreamingClient;
import org.apache.commons.chain.Context;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.io.InputStream;

@Dependent
public class OandaStreamingCommand extends StreamingCommand {
    @Inject
    @RestClient
    OandaPriceStreamingClient oandaPriceStreamingClient;

    @Override
    public boolean execute(Context context) throws Exception {
        StreamingContext streamingContext = getStreamingContext(context);
        if (streamingContext == null || streamingContext.baseContext().broker() != BaseContext.Broker.OANDA)
            return false;
        InputStream pricingStream = oandaPriceStreamingClient.getStream(
                streamingContext.baseContext().apiToken(),
                streamingContext.baseContext().accountID(),
                streamingContext.currencies().get(0).name('_')
        );
        runAsyncFeed(runOandaStreaming(pricingStream));
        return false;
    }

    private Runnable runOandaStreaming(InputStream stream) {
        return () -> {};
    }
}
