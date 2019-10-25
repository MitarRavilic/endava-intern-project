package com.endava.server.util;

import com.endava.server.exception.InvalidCurrencyCodeException;
import com.endava.server.exception.ResourceNotFoundException;
import com.endava.server.model.Transfer;
import com.endava.server.model.TransferType;
import com.endava.server.model.User;
import com.endava.server.model.UserAccount;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.money.convert.*;
import java.math.BigDecimal;
import java.util.*;

public class MoneyUtility {
    @Getter
    private static ExchangeRateProvider rateProvider = MonetaryConversions.getExchangeRateProvider("IMF");

    //Convert balance to Money
    public static Money getMoneyFromUserAccount(UserAccount userAccount) {
        return Money.of(userAccount.getBalance(), userAccount.getCurrencyCode());
    }

    // check if two accounts use same currency
    public static boolean checkUserAccountCurrencyMatch(UserAccount account1, UserAccount account2) {
        return account1.getCurrencyCode().equals(account2.getCurrencyCode());
    }


    public static UserAccount setUserAccountBalanceFromMoney(UserAccount userAccount, Money money) {
        //if(userAccount.getCurrencyCode() == money.getCurrency().getCurrencyCode()){
        userAccount.setBalance(money.getNumberStripped());
        return userAccount;
        //} else {throw new Exception("currency mismatch");} // write CurrencyMismatchException and swap
    }

    // Convert money
    public static Money convertMoney(Money money, String targetCurrencyCode) {
        CurrencyConversion conversion = rateProvider.getCurrencyConversion(targetCurrencyCode);
        return money.with(conversion);

    }


    public static Money convertMoneyCustomRate(Money money, String targetCurrencyCode, BigDecimal rate) {
        ExchangeRate exchangeRate = new CustomExchangeRate(money.getCurrency().getCurrencyCode(), targetCurrencyCode, rate);
        CurrencyConversion conversion = new CustomCurrencyConversion(exchangeRate);
        return money.with(conversion);
    }


    public static void transferMoney(UserAccount sender, UserAccount recipient, BigDecimal amount) {

    }

    public static void depositMoneyToAccount(UserAccount userAccount, BigDecimal amount) {
        Money deposit = Money.of(amount, userAccount.getCurrencyCode());
        Money balance = getMoneyFromUserAccount(userAccount).add(deposit);
        setUserAccountBalanceFromMoney(userAccount, balance);
    }

    public static void withdrawMoneyFromAccount(UserAccount userAccount, BigDecimal amount) {
        if (userAccount.getBalance().compareTo(amount) < 0) {
            return; // add InsufficientFundsException
        }
        Money balance = getMoneyFromUserAccount(userAccount).subtract(Money.of(amount, userAccount.getCurrencyCode()));
        setUserAccountBalanceFromMoney(userAccount, balance);
    }

    public static void reserveMoney(UserAccount userAccount, BigDecimal amount) {

    }

    public static boolean isCurrencyCodeValid(String currencyCode) {
        return Currency.getInstance(currencyCode) != null;
    }

    public static void sendMoney(UserAccount senderAccount, UserAccount recipientAccount, BigDecimal amount) {
    }


    public static void sendAndConvertMoney(UserAccount senderAccount, UserAccount recipientAccount, BigDecimal amount) {
        if (senderAccount.getBalance().compareTo(amount) > 0) {

            Money senderAccountMoney = getMoneyFromUserAccount(senderAccount);
            Money recipientAccountMoney = getMoneyFromUserAccount(recipientAccount);
            Money amountMoney = Money.of(amount, senderAccount.getCurrencyCode());
            Money convertedAmountMoney = convertMoney(amountMoney, recipientAccount.getCurrencyCode());
            setUserAccountBalanceFromMoney(senderAccount, senderAccountMoney.subtract(amountMoney));
            setUserAccountBalanceFromMoney(recipientAccount, recipientAccountMoney.add(convertedAmountMoney));
        }
    }


//    public static void convertMoneyListing(UserAccount senderAccount, UserAccount recipientAccount, BigDecimal amount, BigDecimal rate) {
//        if (senderAccount.getBalance().compareTo(amount) > 0) {
//
//            Money senderAccountMoney = getMoneyFromUserAccount(senderAccount);
//            Money recipientAccountMoney = getMoneyFromUserAccount(recipientAccount);
//            Money amountMoney = Money.of(amount, senderAccount.getCurrencyCode());
//
//
//            Money convertedAmountMoney = convertMoneyCustomRate(amountMoney, recipientAccount.getCurrencyCode(), rate);
//            setUserAccountBalanceFromMoney(senderAccount, senderAccountMoney.subtract(amountMoney));
//            setUserAccountBalanceFromMoney(recipientAccount, recipientAccountMoney.add(convertedAmountMoney));
//        }
//    }

    public static ListingResolveHelper resolveListing(User user1, User user2, String currencyCode1, String currencyCode2, BigDecimal amount, BigDecimal rate) {

        if (isCurrencyCodeValid(currencyCode1) && isCurrencyCodeValid(currencyCode2)) {
            UserAccount user1SendingAccount = user1.getUserAccountWithCurrency(currencyCode1).orElseThrow(() -> new ResourceNotFoundException("UserAccount", "currencyCode", currencyCode1));
            UserAccount user1ReceivingAccount = user1.getUserAccountWithCurrency(currencyCode2).orElse(new UserAccount(user1, currencyCode2));
            UserAccount user2SendingAccount = user2.getUserAccountWithCurrency(currencyCode2).orElseThrow(() -> new ResourceNotFoundException("UserAccount", "currencyCode", currencyCode2));
            UserAccount user2ReceivingAccount = user2.getUserAccountWithCurrency(currencyCode1).orElse(new UserAccount(user2, currencyCode1));

            // Monetary amounts
            Money amountMoney = Money.of(amount, currencyCode1);
            Money convertedAmountMoney = convertMoneyCustomRate(amountMoney, currencyCode2, rate);

            // subtracting from listing creator
            user1SendingAccount.resolveReserved(amount);

            // subtracting from listing resolver
            setUserAccountBalanceFromMoney(user2SendingAccount,
                    getMoneyFromUserAccount(user2SendingAccount).subtract(convertedAmountMoney));

            // adding to listing creator
            setUserAccountBalanceFromMoney(user1ReceivingAccount,
                    getMoneyFromUserAccount(user1ReceivingAccount).add(convertedAmountMoney));
            // adding to listing resolver
            setUserAccountBalanceFromMoney(user2ReceivingAccount,
                    getMoneyFromUserAccount(user2ReceivingAccount).add(amountMoney));

            ArrayList<Transfer> transfers = new ArrayList<>();
            transfers.add(new Transfer(user1SendingAccount, user2ReceivingAccount, amount, TransferType.LISTING));
            transfers.add(new Transfer(user2SendingAccount, user1ReceivingAccount, convertedAmountMoney.getNumberStripped(), TransferType.LISTING));

            return new ListingResolveHelper(user1SendingAccount, user1ReceivingAccount, user2SendingAccount, user2ReceivingAccount, amount, convertedAmountMoney.getNumberStripped());
        } else throw new InvalidCurrencyCodeException();
    }

//    public static HashMap<String, BigDecimal> testMoneyToMap(Money money){
//       HashMap<String, BigDecimal> map = new HashMap<>();
//       map.put(money.getCurrency().getCurrencyCode(), money.getNumberStripped());
//        return map;
//    }
//    public static Money moneyWrapper(String currencyCode, BigDecimal amount){
//        return Money.of(amount, currencyCode);
//    }
//

}
