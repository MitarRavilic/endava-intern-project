package com.endava.server.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CurrencyPairRequest {
    private String baseCurrencyCode;
    private String targetCurrencyCode;
}
