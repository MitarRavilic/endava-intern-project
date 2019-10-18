package com.endava.server.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class ListingDTOCreate {

    private String baseCurrencyCode;
    private BigDecimal amount;
    private String targetCurrencyCode;
    private BigDecimal rate;
}
