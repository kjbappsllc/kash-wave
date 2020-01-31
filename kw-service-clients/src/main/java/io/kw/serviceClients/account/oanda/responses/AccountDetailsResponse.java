package io.kw.serviceClients.account.oanda.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AccountDetailsResponse {
    String lastTransactionID;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class OandaAccount {
        String id;
        String alias;
        String currency;
        Integer createdByUserID;
        String guaranteedStopLossOrderMode;
        Double marginRate;
        Integer openTradeCount;
        Integer openPositionCount;
        Integer pendingOrderCount;
        Boolean hedgingEnabled;

//                #
//                # The total unrealized profit/loss for all Trades currently open in the
//    # Account.
//    #
//        unrealizedPL : (AccountUnits),
//
//                #
//                # The net asset value of the Account. Equal to Account balance +
//                # unrealizedPL.
//    #
//        NAV : (AccountUnits),
//
//                #
//                # Margin currently used for the Account.
//                #
//        marginUsed : (AccountUnits),
//
//                #
//                # Margin available for Account currency.
//                #
//        marginAvailable : (AccountUnits),
//
//                #
//                # The value of the Account’s open positions represented in the Account’s
//    # home currency.
//                #
//        positionValue : (AccountUnits),
//
//                #
//                # The Account’s margin closeout unrealized PL.
//    #
//        marginCloseoutUnrealizedPL : (AccountUnits),
//
//                #
//                # The Account’s margin closeout NAV.
//                #
//        marginCloseoutNAV : (AccountUnits),
//
//                #
//                # The Account’s margin closeout margin used.
//    #
//        marginCloseoutMarginUsed : (AccountUnits),
//
//                #
//                # The Account’s margin closeout percentage. When this value is 1.0 or above
//    # the Account is in a margin closeout situation.
//                #
//        marginCloseoutPercent : (DecimalNumber),
//
//                #
//                # The value of the Account’s open positions as used for margin closeout
//    # calculations represented in the Account’s home currency.
//    #
//        marginCloseoutPositionValue : (DecimalNumber),
//
//                #
//                # The current WithdrawalLimit for the account which will be zero or a
//    # positive value indicating how much can be withdrawn from the account.
//    #
//        withdrawalLimit : (AccountUnits),
//
//                #
//                # The Account’s margin call margin used.
//    #
//        marginCallMarginUsed : (AccountUnits),
//
//                #
//                # The Account’s margin call percentage. When this value is 1.0 or above the
//    # Account is in a margin call situation.
//    #
//        marginCallPercent : (DecimalNumber),
//
//                #
//                # The current balance of the account.
//                #
//        balance : (AccountUnits),
//
//                #
//                # The total profit/loss realized over the lifetime of the Account.
//                #
//        pl : (AccountUnits),
//
//                #
//                # The total realized profit/loss for the account since it was last reset by
//    # the client.
//                #
//        resettablePL : (AccountUnits),
//
//                #
//                # The total amount of financing paid/collected over the lifetime of the
//    # account.
//    #
//        financing : (AccountUnits),
//
//                #
//                # The total amount of commission paid over the lifetime of the Account.
//                #
//        commission : (AccountUnits),
//
//                #
//                # The total amount of dividend paid over the lifetime of the Account in the
//    # Account’s home currency.
//    #
//        dividend : (AccountUnits),
//
//                #
//                # The total amount of fees charged over the lifetime of the Account for the
//    # execution of guaranteed Stop Loss Orders.
//                #
//        guaranteedExecutionFees : (AccountUnits),
//
//                #
//                # The date/time when the Account entered a margin call state. Only provided
//    # if the Account is in a margin call.
//    #
//        marginCallEnterTime : (DateTime),
//
//                #
//                # The number of times that the Account’s current margin call was extended.
//                #
//        marginCallExtensionCount : (integer),
//
//                #
//                # The date/time of the Account’s last margin call extension.
//    #
//        lastMarginCallExtensionTime : (DateTime),
//
//                #
//                # The ID of the last Transaction created for the Account.
//                #
//        lastTransactionID : (TransactionID),
//
//                #
//                # The details of the Trades currently open in the Account.
//                #
//        trades : (Array[TradeSummary]),
//
//                #
//                # The details all Account Positions.
//    #
//        positions : (Array[Position]),
//
//                #
//                # The details of the Orders currently pending in the Account.
//                #
//        orders : (Array[Order])
    }
}
