package com.endava.server.dto.response;

import com.endava.server.model.Transfer;
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
    String type;


    public TransferResponse(Transfer transfer) {
        this.senderUsername = transfer.getSenderAccount().getUser().getUsername();
        this.recipientUsername = transfer.getRecipientAccount().getUser().getUsername();
        this.senderCurrencyCode = transfer.getSenderCurrencyCode();
        this.recipientCurrencyCode = transfer.getRecipientCurrencyCode();
        this.amount = transfer.getAmount();
        this.type = transfer.getTransferType().toString();
    }
}
