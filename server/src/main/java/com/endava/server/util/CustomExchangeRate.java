package com.endava.server.util;


import lombok.AllArgsConstructor;
import org.javamoney.moneta.spi.DefaultNumberValue;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.NumberValue;
import javax.money.convert.ConversionContext;
import javax.money.convert.ExchangeRate;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
public class CustomExchangeRate implements ExchangeRate {

   private String baseCurrency;
   private String targetCurrency;
   private BigDecimal factor;

    @Override
    public ConversionContext getContext() {
        return ConversionContext.REALTIME_CONVERSION;
    }

    @Override
    public CurrencyUnit getBaseCurrency() {
        return Monetary.getCurrency(this.baseCurrency);
    }

    @Override
    public CurrencyUnit getCurrency() {
        return Monetary.getCurrency(this.targetCurrency);
    }

    @Override
    public NumberValue getFactor() {
        return DefaultNumberValue.of(this.factor);
    }

    @Override
    public List<ExchangeRate> getExchangeRateChain() {
        return List.of(this);
    }
}
