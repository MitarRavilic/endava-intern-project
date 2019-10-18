package com.endava.server.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class ConvertMoneyRequest {
    String baseCurrencyCode;
    String targetCurrencyCode;
    BigDecimal amount;
}
