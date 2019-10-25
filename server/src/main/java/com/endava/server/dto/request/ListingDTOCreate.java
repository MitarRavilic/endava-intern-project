package com.endava.server.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ListingDTOCreate {

    private String baseCurrencyCode;
    private BigDecimal amount;
    private String targetCurrencyCode;
    private BigDecimal rate;
}
