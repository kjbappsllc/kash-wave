package io.kw.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.kw.model.*;
import io.kw.service.cdi.qualifiers.DefaultMapper;
import io.kw.service.cdi.qualifiers.TickReceived;
import io.kw.serviceClients.pricing.oanda.OandaPriceStreamingClient;
import io.kw.serviceClients.pricing.oanda.responses.PriceStreamingResponse;
import io.vavr.control.Try;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class TickStreamService {

    @Inject
    @RestClient
    OandaPriceStreamingClient oandaPriceStreamingClient;

    @Inject
    @DefaultMapper
    ObjectMapper mapper;

    @Inject
    @TickReceived
    Event<Price> tickReceivedEvent;

    private ExecutorService priceFeedExecutor;
    private HashMap<String, CurrencyPair> currencies;

    public TickStreamService() {
        priceFeedExecutor = Executors.newSingleThreadExecutor();
        currencies = new HashMap<>();
    }

    public void startStream(String apiToken, String accountId, CurrencyPair ...pairs) {
        System.out.println("Starting the Pricing Stream");
        Arrays.stream(pairs).forEach(currencyPair -> currencies.put(currencyPair.getPairName("_"), currencyPair));
        InputStream pricingStream = oandaPriceStreamingClient.getPrices(
                apiToken,
                accountId,
                pairs[0].getPairName("_")
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
                    System.out.println("RECEIVED PRICE FROM BROKER: " + data);
                    if (data == null) break;
                    Try<PriceStreamingResponse> mappedResponse = Try.of(() -> mapper.readValue(data, PriceStreamingResponse.class));
                    mappedResponse.andThenTry(streamingResponse -> tickReceivedEvent.fire(
                            Price.builder()
                                    .ask(new BigDecimal(streamingResponse.getAsks().get(0).getPrice()))
                                    .bid(new BigDecimal(streamingResponse.getBids().get(0).getPrice()))
                                    .timestamp(Instant.parse(streamingResponse.getTime()))
                                    .currencyPair(currencies.get(streamingResponse.getInstrument()))
                                    .precision(5)
                                    .build()
                    ));
                }
            }
            Try.run(bufferedReader::close);
            endStream();
        });
    }
}