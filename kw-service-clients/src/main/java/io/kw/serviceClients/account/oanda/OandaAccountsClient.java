package io.kw.serviceClients.account.oanda;

import io.kw.serviceClients.account.oanda.responses.PairsInfoResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/accounts")
@RegisterRestClient(configKey = "api-base")
@Singleton
public interface OandaAccountsClient {

    @GET
    @Path("/{accountID}/instruments")
    @Produces(MediaType.APPLICATION_JSON)
    PairsInfoResponse getPairInfoForAccount(
            @HeaderParam("Authorization") String apiToken,
            @PathParam("accountID") String accountID,
            @QueryParam("instruments") String instruments
    );
}
