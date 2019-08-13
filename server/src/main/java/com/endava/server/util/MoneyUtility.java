package com.endava.server.util;

import com.endava.server.model.Transfer;
import com.endava.server.model.UserAccount;
import com.endava.server.repository.UserAccountRepository;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;

import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;


public class MoneyUtility {


    //Convert balance to Money
    public Money getMonetaryAmountFromUserAccount(UserAccount userAccount) {
        return Money.of(userAccount.getBalance(), userAccount.getCurrencyCode());
    }

    // check if two accounts use same currency
    public boolean checkUserAccountCurrencyMatch(UserAccount account1, UserAccount account2){
        return account1.getCurrencyCode()==account2.getCurrencyCode();
    }

    public UserAccount setUserAccountBalanceFromMoney(UserAccount userAccount, Money money) throws Exception {
        if(userAccount.getCurrencyCode() == money.getCurrency().getCurrencyCode()){
            userAccount.setBalance(money.getNumberStripped());
            return userAccount;
        } else {throw new Exception("currency mismatch");} // write CurrencyMismatchException and swap
    }
    // Convert money
    public Money convertMoney(Money money, String targetCurrencyCode){
        CurrencyConversion conversion = MonetaryConversions.getConversion(targetCurrencyCode);
        return money.with(conversion);
    }

    public void transferMoney(UserAccount sender, UserAccount recipient, Money amount) throws Exception {
        try {
            if (checkUserAccountCurrencyMatch(sender, recipient)) {
                UserAccount senderAccount = setUserAccountBalanceFromMoney(sender, getMonetaryAmountFromUserAccount(sender).subtract(amount));
            }
        } catch (Exception e){}
    }



}
