package io.kw.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.kw.model.*;
import io.kw.service.cdi.qualifiers.DataInitialized;
import io.kw.service.cdi.qualifiers.DefaultMapper;
import io.kw.service.cdi.qualifiers.TickReceived;
import io.kw.serviceClients.historical.oanda.OandaHistoricalPricesClient;
import io.kw.serviceClients.historical.oanda.responses.HistoricalPricesResponse;
import io.kw.serviceClients.pricing.oanda.OandaPriceStreamingClient;
import io.kw.serviceClients.pricing.oanda.responses.PriceStreamingResponse;
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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@ApplicationScoped
public class TickStreamer {
    @Inject
    @RestClient
    OandaPriceStreamingClient oandaPriceStreamingClient;

    @Inject
    @DefaultMapper
    ObjectMapper mapper;

    @Inject
    @TickReceived
    Event<Price> tickReceivedEvent;

    private static final String API_TOKEN_HEADER = "Bearer a3f580b7f2357b31d139561a220b4aec-ff520f9ef1b1babf60781cd4ed8c014f";
    private ExecutorService priceFeedExecutor;
    private HashMap<String, CurrencyPair> currencies;

    public TickStreamer() {
        priceFeedExecutor = Executors.newSingleThreadExecutor();
        currencies = new HashMap<>();
    }

    public void startStream(CurrencyPair ...pairs) {
        System.out.println("Started the Pricing Stream");
        Arrays.stream(pairs).forEach(currencyPair -> {
            currencies.put(currencyPair.getPairName("_"), currencyPair);
        });
        InputStream pricingStream = oandaPriceStreamingClient.getPrices(
                API_TOKEN_HEADER,
                "101-001-9159383-001",
                pairs[0].getPairName("_")
        );
        runAsyncPriceFeed(pricingStream);
    }

    public void endStream() {
        try {
            System.out.println("Attempt to shutdown price feed");
            priceFeedExecutor.shutdown();
            priceFeedExecutor.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        } finally {
            if (!priceFeedExecutor.isTerminated()) {
                System.err.println("Canceling non-finished tasks");
            }
            priceFeedExecutor.shutdownNow();
            System.out.println("Shutdown finished");
        }
    }

    private void runAsyncPriceFeed(InputStream pricingStream) {
        priceFeedExecutor.submit(() -> {
            String priceStringData;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pricingStream));
            while (!Thread.interrupted()) {
                try {
                    if ((priceStringData = bufferedReader.readLine()) == null) break;
                    PriceStreamingResponse mappedPricingResponse = mapper.readValue(priceStringData, PriceStreamingResponse.class);
                    var newTick = Price.builder()
                            .ask(new BigDecimal(mappedPricingResponse.getAsks().get(0).getPrice()))
                            .bid(new BigDecimal(mappedPricingResponse.getBids().get(0).getPrice()))
                            .timestamp(Instant.parse(mappedPricingResponse.getTime()))
                            .currencyPair(currencies.get(mappedPricingResponse.getInstrument()))
                            .precision(5)
                            .build();
                    System.out.println("Received Price Event From Broker: " + newTick);
                    tickReceivedEvent.fire(newTick);
                } catch (Exception e) {/* Filter out Heartbeats */}
            }
            try { bufferedReader.close(); } catch (Exception e) {/* catch all */}
        });
    }
}
