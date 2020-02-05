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

    private void streamPrices(CurrencyPair interestedPair) {
        System.out.println("Starting Stream ...");
//        tickStreamService.startStream(
//                apiKey,
//                accountID,
//                interestedPair
//        );
    }

    // TODO: REMOVE
    public void getAccount() {
        AccountDetailsResponse res = accountsClient.getAccountDetails(apiKey, accountID);
        System.out.println(res.getAccount().getTrades());
    }

    // TODO: REMOVE
    public void closeTrade() {
        System.out.println("In the Close Trade Function");
        try {
            TradeCloseResponse res = tradeClient.closeTrade(apiKey, accountID, "42", new TradeCloseRequest("10"));
            System.out.println(res);
        } catch (WebApplicationException e) {
            if (e.getResponse().getStatus()  == 404) {
                System.out.println("The Account or Trade specified does not exist.");
            } else if (e.getResponse().getStatus() == 400) {
                System.out.println("The Trade cannot be closed as requested.");
            } else {
                System.out.println("Bad Request, unknown error");
            }
        } catch (Exception e) {
            System.out.println("Unknown Exception " + e);
        }
    }
}
