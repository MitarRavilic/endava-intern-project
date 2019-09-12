package com.endava.server.util;

import com.endava.server.aspect.ServiceLoggingAspect;
import com.endava.server.model.UserAccount;
import lombok.Getter;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.money.convert.CurrencyConversion;
import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;
import java.math.BigDecimal;
import java.util.Currency;


public class MoneyUtility {
    @Getter
    private static ExchangeRateProvider rateProvider = MonetaryConversions.getExchangeRateProvider("IMF");

    private Logger logger = LoggerFactory.getLogger(MoneyUtility.class);
    //Convert balance to Money
    public static Money getMoneyFromUserAccount(UserAccount userAccount) {
        return Money.of(userAccount.getBalance(), userAccount.getCurrencyCode());
    }

    // check if two accounts use same currency
    public static boolean checkUserAccountCurrencyMatch(UserAccount account1, UserAccount account2){
        return account1.getCurrencyCode().equals(account2.getCurrencyCode());
    }

    public static UserAccount setUserAccountBalanceFromMoney(UserAccount userAccount, Money money) {
        //if(userAccount.getCurrencyCode() == money.getCurrency().getCurrencyCode()){
            userAccount.setBalance(money.getNumberStripped());
            return userAccount;
        //} else {throw new Exception("currency mismatch");} // write CurrencyMismatchException and swap
    }
    // Convert money
    public static Money convertMoney(Money money, String targetCurrencyCode){
        CurrencyConversion conversion = rateProvider.getCurrencyConversion(targetCurrencyCode);
        return money.with(conversion);
    }

        public static void transferMoney(UserAccount sender, UserAccount recipient, BigDecimal amount){

        }

    public static void depositMoneyToAccount(UserAccount userAccount, BigDecimal amount) {
        Money deposit = Money.of(amount, userAccount.getCurrencyCode());
        Money balance = getMoneyFromUserAccount(userAccount).add(deposit);
        setUserAccountBalanceFromMoney(userAccount, balance);
    }

    public static void withdrawMoneyFromAccount(UserAccount userAccount, BigDecimal amount) {
        if(userAccount.getBalance().compareTo(amount) < 0){
            return; // add InsufficientFundsException
        }
        Money balance = getMoneyFromUserAccount(userAccount).subtract(Money.of(amount, userAccount.getCurrencyCode()));
        setUserAccountBalanceFromMoney(userAccount, balance);
    }
    public static boolean isCurrencyCodeValid(String currencyCode) {
        return Currency.getInstance(currencyCode) != null;
    }

    public static void sendMoney(UserAccount senderAccount, UserAccount recipientAccount, BigDecimal amount){
    }


    public static void sendAndConvertMoney(UserAccount senderAccount, UserAccount recipientAccount, BigDecimal amount){
        if(senderAccount.getBalance().compareTo(amount) > 0) {

            Money senderAccountMoney = getMoneyFromUserAccount(senderAccount);
            Money recipientAccountMoney = getMoneyFromUserAccount(recipientAccount);
            Money amountMoney = Money.of(amount, senderAccount.getCurrencyCode());
            Money convertedAmountMoney = convertMoney(amountMoney, recipientAccount.getCurrencyCode());
            setUserAccountBalanceFromMoney(senderAccount, senderAccountMoney.subtract(amountMoney));
            setUserAccountBalanceFromMoney(recipientAccount, recipientAccountMoney.add(convertedAmountMoney));
        }
    }
}
