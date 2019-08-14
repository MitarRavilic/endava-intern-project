package com.endava.server.util;

import com.endava.server.model.Transfer;
import com.endava.server.model.UserAccount;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Component;

import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;

@Component
public class MoneyUtility {


    //Convert balance to Money
    public Money getMoneyFromUserAccount(UserAccount userAccount) {
        return Money.of(userAccount.getBalance(), userAccount.getCurrencyCode());
    }

    // check if two accounts use same currency
    public boolean checkUserAccountCurrencyMatch(UserAccount account1, UserAccount account2){
        return account1.getCurrencyCode().equals(account2.getCurrencyCode());
    }

    public UserAccount setUserAccountBalanceFromMoney(UserAccount userAccount, Money money) {
        //if(userAccount.getCurrencyCode() == money.getCurrency().getCurrencyCode()){
            userAccount.setBalance(money.getNumberStripped());
            return userAccount;
        //} else {throw new Exception("currency mismatch");} // write CurrencyMismatchException and swap
    }
    // Convert money
    public Money convertMoney(Money money, String targetCurrencyCode){
        CurrencyConversion conversion = MonetaryConversions.getConversion(targetCurrencyCode);
        return money.with(conversion);
    }

    //
    public void transferMoney(UserAccount sender, UserAccount recipient, Money amount){
        UserAccount senderAccount;
        UserAccount recipientAccount;
        if (checkUserAccountCurrencyMatch(sender, recipient)) {
            senderAccount = setUserAccountBalanceFromMoney(sender, getMoneyFromUserAccount(sender).subtract(amount));
            recipientAccount = setUserAccountBalanceFromMoney(recipient, getMoneyFromUserAccount(recipient).add(amount));
        } else {
            sender = setUserAccountBalanceFromMoney(sender, getMoneyFromUserAccount(sender).subtract(amount));
            recipient = setUserAccountBalanceFromMoney(recipient, getMoneyFromUserAccount(recipient).add(convertMoney(amount, recipient.getCurrencyCode())));
        }
    }




}
