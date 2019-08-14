package com.endava.server.service;

import com.endava.server.model.Transfer;
import com.endava.server.model.UserAccount;
import com.endava.server.repository.UserAccountRepository;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;
import java.util.Currency;

@Service
public class ExchangeService {

    @Autowired
    UserAccountRepository userAccountRepository;



}
