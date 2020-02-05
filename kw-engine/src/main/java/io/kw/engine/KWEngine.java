package io.kw.engine;

import io.kw.model.CurrencyPair;
import io.kw.service.TickStreamService;
import io.kw.serviceClients.account.oanda.OandaAccountsClient;
import io.kw.serviceClients.account.oanda.responses.AccountDetailsResponse;
import io.kw.serviceClients.trade.oanda.OandaTradeClient;
import io.kw.serviceClients.trade.oanda.requests.TradeCloseRequest;
import io.kw.serviceClients.trade.oanda.responses.TradeCloseResponse;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;

@ApplicationScoped
public class KWEngine {

    @Inject
    @RestClient
    OandaAccountsClient accountsClient;

    @Inject
    @RestClient
    OandaTradeClient tradeClient;

    @Inject
    TickStreamService tickStreamService;

    String apiKey = "Bearer a3f580b7f2357b31d139561a220b4aec-ff520f9ef1b1babf60781cd4ed8c014f";
    String accountID = "101-001-9159383-001";
}
