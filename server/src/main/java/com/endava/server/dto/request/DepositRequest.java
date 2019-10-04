package com.endava.server.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
public class DepositRequest {
    String currencyCode;
    BigDecimal amount;
}
