package io.kw.service;

import io.kw.model.*;
import io.kw.serviceClients.historical.oanda.OandaHistoricalPricesClient;
import io.kw.serviceClients.historical.oanda.responses.HistoricalPricesResponse;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class HistoricalPricesService {

    @Inject
    @RestClient
    OandaHistoricalPricesClient historicalPricesClient;

    public DataBuffer<Bar> retrieveHistoricalData(String apiToken, Timeframe tf, CurrencyPair pair) {
        HistoricalPricesResponse response = historicalPricesClient.getHistoricalBars(
                apiToken,
                pair.pairName("_"),
                "BA",
                tf.toString(),
                250
        );

        List<Bar> historicalBars = response
                .getCandles()
                .stream()
                .map(candle -> {
                    Instant parsedTime = Instant.parse(candle.getTime());
                    int precision = 5;
                    Price.PriceBuilder priceBuilder = Price.builder()
                            .timestamp(parsedTime)
                            .precision(precision);
                    Bar.BarBuilder barBuilder = Bar.builder()
                            .volume(candle.getVolume())
                            .timeframe(tf)
                            .timestamp(parsedTime);
                    return barBuilder
                            .high(priceBuilder
                                    .ask(new BigDecimal(candle.getAsk().getH()))
                                    .bid(new BigDecimal(candle.getBid().getH()))
                                    .currencyPair(pair)
                                    .build()
                            )
                            .close(priceBuilder
                                    .ask(new BigDecimal(candle.getAsk().getC()))
                                    .bid(new BigDecimal(candle.getBid().getC()))
                                    .currencyPair(pair)
                                    .build()
                            )
                            .low(priceBuilder
                                    .ask(new BigDecimal(candle.getAsk().getL()))
                                    .bid(new BigDecimal(candle.getBid().getL()))
                                    .currencyPair(pair)
                                    .build()
                            )
                            .open(priceBuilder
                                    .ask(new BigDecimal(candle.getAsk().getO()))
                                    .bid(new BigDecimal(candle.getBid().getO()))
                                    .currencyPair(pair)
                                    .build()
                            ).build();
                }).collect(Collectors.toList());

        DataBuffer<Bar> barDataBuffer = new DataBuffer<>();
        barDataBuffer.addAll(historicalBars);
        return barDataBuffer;
    }

}
