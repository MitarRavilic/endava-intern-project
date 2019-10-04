package com.endava.server.dto.request;

import java.math.BigDecimal;

public class ConvertMoneyRequest {
    String baseCurrencyCode;
    String targetCurrencyCode;
    BigDecimal amount;
}
