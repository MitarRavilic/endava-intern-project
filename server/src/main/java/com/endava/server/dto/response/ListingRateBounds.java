package com.endava.server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class ListingRateBounds {
    private BigDecimal rateMin;
    private BigDecimal rateOfficial;
    private BigDecimal rateMax;
}
