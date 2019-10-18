package com.endava.server.util;

import lombok.AllArgsConstructor;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.convert.ConversionContext;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.ExchangeRate;
import javax.money.convert.ExchangeRateProvider;

@AllArgsConstructor
public class CustomCurrencyConversion implements CurrencyConversion {

    ExchangeRate exchangeRate;

    @Override
    public ConversionContext getContext() {
        return exchangeRate.getContext();
    }

    @Override
    public ExchangeRate getExchangeRate(MonetaryAmount monetaryAmount) {
        return exchangeRate;
    }

    @Override
    public ExchangeRateProvider getExchangeRateProvider() {
        return null;
    }

    @Override
    public CurrencyUnit getCurrency() {
        return exchangeRate.getCurrency();
    }

    @Override
    public MonetaryAmount apply(MonetaryAmount monetaryAmount) {
        return Money.of(monetaryAmount.multiply(exchangeRate.getFactor()).getNumber(), exchangeRate.getCurrency());
    }
}
