package io.kw.engine.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.kw.engine.cdi.qualifiers.DataInitialized;
import io.kw.engine.cdi.qualifiers.DefaultMapper;
import io.kw.engine.cdi.qualifiers.TickReceived;
import io.kw.model.bar.Bar;
import io.kw.model.bar.Price;
import io.kw.model.bar.Timeframe;
import io.kw.model.currency.CurrencyPair;
import io.kw.model.datatype.DataBuffer;
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
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@ApplicationScoped
public class PriceStreamingEngine {

    @Inject
    @RestClient
    OandaPriceStreamingClient oandaPriceStreamingClient;

    @Inject
    @RestClient
    OandaHistoricalPricesClient oandaHistoricalPricesClient;

    @Inject
    @DefaultMapper
    ObjectMapper mapper;

    @Inject
    @TickReceived
    Event<DataBuffer<Bar>> tickReceivedEvent;

    @Inject
    @DataInitialized
    Event<DataBuffer<Bar>> dataInitializedEvent;

    private static final String API_TOKEN_HEADER = "Bearer a3f580b7f2357b31d139561a220b4aec-ff520f9ef1b1babf60781cd4ed8c014f";
    private ExecutorService priceFeedService;
    private DataBuffer<Bar> bars = new DataBuffer<>();

    public PriceStreamingEngine() {
        priceFeedService = Executors.newSingleThreadExecutor();
    }

    public void startPricingStream(CurrencyPair pair, Timeframe timeframe) {
        System.out.println("Started the Pricing Stream");
        if (Objects.isNull(bars) || bars.size() <= 0) {
            bars.addAll(getHistoricalBars(pair, timeframe));
            dataInitializedEvent.fire(bars);
        }
        InputStream pricingStream = oandaPriceStreamingClient.getPrices(
                API_TOKEN_HEADER,
                "101-001-9159383-001",
                pair.getPairName("_")
        );
        runAsyncPriceFeed(pricingStream, timeframe);
    }

    public void stopPricingStream() {
        try {
            System.out.println("Attempt to shutdown price feed");
            priceFeedService.shutdown();
            priceFeedService.awaitTermination(3, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        }
        finally {
            if (!priceFeedService.isTerminated()) {
                System.err.println("Canceling non-finished tasks");
            }
            priceFeedService.shutdownNow();
            System.out.println("Shutdown finished");
        }
    }

    private List<Bar> getHistoricalBars(CurrencyPair pair, Timeframe timeframe) {
        HistoricalPricesResponse historicalPrices = oandaHistoricalPricesClient.getHistoricalBars(
                API_TOKEN_HEADER,
                pair.getPairName("_"),
                "BA",
                timeframe.toString(),
                250
        );
        return historicalPrices
                .getCandles()
                .stream()
                .map(candle -> {
                    Instant parsedTime = Instant.parse(candle.getTime());
                    return Bar.builder()
                            .high(Price.builder()
                                    .ask(new BigDecimal(candle.getAsk().getH()))
                                    .bid(new BigDecimal(candle.getBid().getH()))
                                    .timestamp(parsedTime)
                                    .precision(5)
                                    .build()
                            )
                            .close(Price.builder()
                                    .ask(new BigDecimal(candle.getAsk().getC()))
                                    .bid(new BigDecimal(candle.getBid().getC()))
                                    .timestamp(parsedTime)
                                    .precision(5)
                                    .build()
                            )
                            .low(Price.builder()
                                    .ask(new BigDecimal(candle.getAsk().getL()))
                                    .bid(new BigDecimal(candle.getBid().getL()))
                                    .timestamp(parsedTime)
                                    .precision(5)
                                    .build()
                            )
                            .open(Price.builder()
                                    .ask(new BigDecimal(candle.getAsk().getO()))
                                    .bid(new BigDecimal(candle.getBid().getO()))
                                    .timestamp(parsedTime)
                                    .precision(5)
                                    .build()
                            )
                            .timestamp(parsedTime)
                            .volume(candle.getVolume())
                            .timeframe(timeframe)
                            .build();
                        }
                )
                .collect(Collectors.toList());
    }

    private void runAsyncPriceFeed(InputStream pricingStream, Timeframe timeframe) {
        priceFeedService.submit(() -> {
            String priceStringData;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pricingStream));
            while (!Thread.interrupted()) {
                try {
                    if ((priceStringData = bufferedReader.readLine()) == null) break;
                    PriceStreamingResponse mappedPricingResponse = mapper.readValue(priceStringData, PriceStreamingResponse.class);
                    var newPrice = Price.builder()
                            .ask(new BigDecimal(mappedPricingResponse.getAsks().get(0).getPrice()))
                            .bid(new BigDecimal(mappedPricingResponse.getBids().get(0).getPrice()))
                            .timestamp(Instant.parse(mappedPricingResponse.getTime()))
                            .precision(5)
                            .build();
                    System.out.println("Received Price Event From Broker: " + newPrice);
                    if (!isNewBarFormed(timeframe, newPrice)) {
                        bars.get(0).setClose(newPrice);
                        bars.get(0).setHigh(newPrice);
                        bars.get(0).setLow(newPrice);
                    }
                    System.out.println("Updated Bar: " + bars.get(0));
                    tickReceivedEvent.fire(bars);
                } catch (Exception e) {/* Filter out Heartbeats */}
            }
            try { bufferedReader.close(); } catch (Exception e) {/* catch all */}
        });
    }

    private boolean isNewBarFormed(Timeframe timeframe, Price newPrice) {
        if (Timeframe.hasTimeChanged(timeframe, bars.get(0).getTimestamp(), newPrice.getTimestamp())) {
            bars.add(Bar.builder()
                    .close(newPrice)
                    .open(newPrice)
                    .low(newPrice)
                    .high(newPrice)
                    .timeframe(timeframe)
                    .timestamp(newPrice.getTimestamp().truncatedTo(timeframe.getTruncatedUnit()))
                    .build()
            );
            return true;
        }
        return false;
    }
}
