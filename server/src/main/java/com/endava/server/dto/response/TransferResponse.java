package com.endava.server.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
public class TransferResponse {
    String senderUsername;
    String recipientUsername;
    String senderCurrencyCode;
    String recipientCurrencyCode;
    BigDecimal amount;
    Double rate;
    String type;
}
