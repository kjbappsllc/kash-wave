package io.kw.service.streaming;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.kw.cdi.qualifiers.DefaultMapper;
import io.kw.serviceClients.pricing.oanda.OandaPriceStreamingClient;
import io.kw.serviceClients.pricing.oanda.responses.PriceStreamingResponse;
import io.vavr.control.Try;
import org.apache.commons.chain.Context;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.kw.service.BaseContext.Broker;

@Dependent
public class OandaStreamingCommand extends StreamingCommand {

    @Inject
    @DefaultMapper
    ObjectMapper mapper;

    @Inject
    @RestClient
    OandaPriceStreamingClient oandaPriceStreamingClient;

    @Override
    public boolean execute(Context context) throws Exception {
        StreamingContext streamingContext = getStreamingContext(context);
        System.out.println("Here in oanda execute");
        System.out.println(streamingContext);
        if (streamingContext == null || streamingContext.baseContext().broker() != Broker.OANDA) {
            System.out.println("Not Valid Command For: " + this.getClass().getSimpleName());
            return false;
        }
        InputStream pricingStream = oandaPriceStreamingClient.getStream(
                streamingContext.baseContext().apiToken(),
                streamingContext.baseContext().accountID(),
                streamingContext.currencies().get(0).name('_')
        );
        System.out.println("Here in oanda execute");
        runAsyncFeed(runOandaStreaming(pricingStream));
        return true;
    }

    private Runnable runOandaStreaming(InputStream stream) {
        System.out.println("Running Oanda Stream");
        return () -> {
            AtomicBoolean serverIsHealthy = new AtomicBoolean(true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            while (serverIsHealthy.get() && !Thread.interrupted()) {
                Try<String> bufferedData = Try.of(bufferedReader::readLine);
                bufferedData.onSuccess(data -> {
                    if (data == null) {
                        serverIsHealthy.set(false);
                        return;
                    }
                    Try<PriceStreamingResponse> mappedResponse = Try.of(() -> mapper.readValue(data, PriceStreamingResponse.class));
                    System.out.println("Response Received: " + mappedResponse.getOrNull());
                }).onFailure(throwable -> {
                    System.out.println("Error has been thrown! " + throwable.getLocalizedMessage());
                });
            }
            Try.run(bufferedReader::close).andFinally(this::endExecutor);
        };
    }
}
