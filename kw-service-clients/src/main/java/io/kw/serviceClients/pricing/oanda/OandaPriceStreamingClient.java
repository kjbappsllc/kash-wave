package io.kw.serviceClients.pricing.oanda;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

@Path("/accounts")
@RegisterRestClient(configKey = "streaming-api")
@Singleton
public interface OandaPriceStreamingClient {
    @GET
    @Path("/{accountID}/pricing/stream")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    InputStream getPrices(@HeaderParam("Authorization") String apiToken,
                          @PathParam("accountID") String accountID,
                          @QueryParam("instruments") String instruments);
}
