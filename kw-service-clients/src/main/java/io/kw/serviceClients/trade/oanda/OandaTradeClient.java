package io.kw.serviceClients.trade.oanda;


import io.kw.serviceClients.trade.oanda.requests.PositionCloseRequest;
import io.kw.serviceClients.trade.oanda.requests.TradeCloseRequest;
import io.kw.serviceClients.trade.oanda.requests.TradeModifyRequest;
import io.kw.serviceClients.trade.oanda.responses.OpenPositionsResponse;
import io.kw.serviceClients.trade.oanda.responses.PositionCloseResponse;
import io.kw.serviceClients.trade.oanda.responses.TradeCloseResponse;
import io.kw.serviceClients.trade.oanda.responses.TradeModifyResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/accounts")
@RegisterRestClient(configKey = "api-base")
@Singleton
public interface OandaTradeClient {

    @GET
    @Path("/{accountID}/openPositions")
    @Produces(MediaType.APPLICATION_JSON)
    OpenPositionsResponse getOpenPositions(
            @HeaderParam("Authorization") String apiToken,
            @PathParam("accountID") String accountID
    );

    @PUT
    @Path("/{accountID}/trades/{tradeSpecifier}/close")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    TradeCloseResponse closeTrade(
            @HeaderParam("Authorization") String apiToken,
            @PathParam("accountID") String accountID,
            @PathParam("tradeSpecifier") String tradeID,
            TradeCloseRequest tradeCloseRequest
    );

    @PUT
    @Path("/{accountID}/positions/{instrument}/close")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    PositionCloseResponse closePosition(
            @HeaderParam("Authorization") String apiToken,
            @PathParam("accountID") String accountID,
            @PathParam("instrument") String instrument,
            PositionCloseRequest positionCloseRequest
    );

    @PUT
    @Path("/{accountID}/trades/{tradeSpecifier}/orders")
    TradeModifyResponse modifyTPAndSL(
            @HeaderParam("Authorization") String apiToken,
            @PathParam("accountID") String accountID,
            @PathParam("tradeSpecifier") String tradeID,
            TradeModifyRequest tradeModifyRequest
    );
}
