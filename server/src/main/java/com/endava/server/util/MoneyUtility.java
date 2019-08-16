package com.endava.server.util;

import com.endava.server.model.UserAccount;
import org.javamoney.moneta.Money;
import org.springframework.data.util.Pair;

import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;
import java.math.BigDecimal;


public class MoneyUtility {


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
        CurrencyConversion conversion = MonetaryConversions.getConversion(targetCurrencyCode);
        return money.with(conversion);
    }

    // return sender-recipient account pair
    public static Pair<UserAccount, UserAccount> transferMoney(UserAccount sender, UserAccount recipient, String currencyCode, BigDecimal amount){
        UserAccount senderAccount;
        UserAccount recipientAccount;
        Money money = Money.of(amount, currencyCode);
        if (checkUserAccountCurrencyMatch(sender, recipient)) {
            senderAccount = setUserAccountBalanceFromMoney(sender, getMoneyFromUserAccount(sender).subtract(money));
            recipientAccount = setUserAccountBalanceFromMoney(recipient, getMoneyFromUserAccount(recipient).add(money));
        } else {
            senderAccount = setUserAccountBalanceFromMoney(sender, getMoneyFromUserAccount(sender).subtract(money));
            recipientAccount = setUserAccountBalanceFromMoney(recipient, getMoneyFromUserAccount(recipient).add(convertMoney(money, recipient.getCurrencyCode())));
        }
        return Pair.of(senderAccount, recipientAccount);
    }





}
