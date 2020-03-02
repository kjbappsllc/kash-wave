package io.kw.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.kw.model.*;
import io.kw.cdi.qualifiers.DefaultMapper;
import io.kw.cdi.qualifiers.TickReceived;
import io.kw.serviceClients.pricing.oanda.OandaPriceStreamingClient;
import io.vavr.control.Try;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class StreamingService {

    @Inject
    @RestClient
    OandaPriceStreamingClient oandaPriceStreamingClient;

    @Inject
    @DefaultMapper
    ObjectMapper mapper;

    @Inject
    @TickReceived
    Event<Tick> tickReceivedEvent;

    private ExecutorService priceFeedExecutor;
    private HashMap<String, CurrencyPair> currencies;

    public StreamingService() {
        priceFeedExecutor = Executors.newSingleThreadExecutor();
        currencies = new HashMap<>();
    }

    public void startStream(String apiToken, String accountId, CurrencyPair ...pairs) {
        Arrays.stream(pairs).forEach(currencyPair -> currencies.put(currencyPair.name('_'), currencyPair));
        InputStream pricingStream = oandaPriceStreamingClient.getStream(
                apiToken,
                accountId,
                pairs[0].name('_')
        );
        runAsyncPriceFeed(pricingStream);
    }

    public void endStream() {
        Try.run(() -> {
            priceFeedExecutor.shutdown();
            priceFeedExecutor.awaitTermination(1, TimeUnit.SECONDS);
        }).andFinally(() -> {
            if (!priceFeedExecutor.isTerminated()) System.err.println("Canceling non-finished tasks");
            priceFeedExecutor.shutdownNow();
        });
    }

    private void runAsyncPriceFeed(InputStream pricingStream) {
        priceFeedExecutor.submit(() -> {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pricingStream));
            while (!Thread.interrupted()) {
                Try<String> bufferData = Try.of(bufferedReader::readLine);
                if (bufferData.isSuccess()) {
                    String data = bufferData.get();
                    if (data == null) break;
//                    Try<PriceStreamingResponse> mappedResponse = Try.of(() -> mapper.readValue(data, PriceStreamingResponse.class));
//                    mappedResponse.andThenTry(streamingResponse -> tickReceivedEvent.fire(
//                            Tick.builder()
//                                    .ask(new BigDecimal(streamingResponse.getAsks().get(0).getPrice()))
//                                    .bid(new BigDecimal(streamingResponse.getBids().get(0).getPrice()))
//                                    .timestamp(Instant.parse(streamingResponse.getTime()))
//                                    .currencyPair(currencies.get(streamingResponse.getInstrument()))
//                                    .precision(5)
//                                    .build()
//                    ));
                }
            }
            Try.run(bufferedReader::close).andFinally(this::endStream);
        });
    }
}
