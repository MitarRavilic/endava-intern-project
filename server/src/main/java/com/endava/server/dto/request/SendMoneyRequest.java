package com.endava.server.dto.request;

import java.math.BigDecimal;

public class SendMoneyRequest {
    String recipientUsername;
    String currencyCode;
    BigDecimal amount;
}
