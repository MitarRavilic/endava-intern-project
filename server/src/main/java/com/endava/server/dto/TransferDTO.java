package com.endava.server.dto;

import com.endava.server.model.Transfer;
import com.endava.server.model.TransferType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransferDTO {

   private String senderUsername;

   private String senderCurrencyCode;

   private String recipientUsername;

   private String recipientCurrencyCode;

   private BigDecimal amount;

   private Long createdAt;

   private TransferType transferType;

   public TransferDTO(Transfer transfer){
       this.senderUsername = transfer.getSenderAccount().getUser().getUsername();
       this.senderCurrencyCode = transfer.getSenderCurrencyCode();
       this.recipientUsername = transfer.getSenderAccount().getUser().getUsername();
       this.recipientCurrencyCode = transfer.getRecipientCurrencyCode();
       this.amount = transfer.getAmount();
       this.createdAt = transfer.getCreatedAt().toInstant().toEpochMilli();
       this.transferType = transfer.getTransferType();
   }
}
