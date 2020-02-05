package io.kw.serviceClients.trade.oanda;


import io.kw.serviceClients.trade.oanda.requests.TradeCloseRequest;
import io.kw.serviceClients.trade.oanda.requests.TradeModifyRequest;
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
    @Path("/{accountID}/trades/{tradeSpecifier}/orders")
    TradeModifyResponse modifyTPAndSL(
            @HeaderParam("Authorization") String apiToken,
            @PathParam("accountID") String accountID,
            @PathParam("tradeSpecifier") String tradeID,
            TradeModifyRequest tradeModifyRequest
    );
}
