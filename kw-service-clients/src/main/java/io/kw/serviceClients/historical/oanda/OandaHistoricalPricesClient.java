package io.kw.serviceClients.historical.oanda;

import io.kw.serviceClients.historical.oanda.responses.HistoricalPricesResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/instruments")
@RegisterRestClient(configKey = "api-base")
@Singleton
public interface OandaHistoricalPricesClient {
    @GET
    @Path("/{instrument}/candles")
    @Produces(MediaType.APPLICATION_JSON)
    HistoricalPricesResponse getHistoricalBars(
            @HeaderParam("Authorization") String apiToken,
            @PathParam("instrument") String instrument,
            @QueryParam("price") String priceType,
            @QueryParam("granularity") String timeframe,
            @QueryParam("count") Integer numCandles
    );
}
