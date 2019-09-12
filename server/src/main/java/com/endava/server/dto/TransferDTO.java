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

   private Long senderAccountId;

   private String senderCurrencyCode;

   private Long recipientAccountId;

   private String recipientCurrencyCode;

   private BigDecimal amount;

   private TransferType transferType;

   public TransferDTO(Transfer transfer){
       this.senderAccountId = transfer.getSenderAccount().getId();
       this.senderCurrencyCode = transfer.getSenderCurrencyCode();
       this.recipientAccountId = transfer.getSenderAccount().getId();
       this.recipientCurrencyCode = transfer.getRecipientCurrencyCode();
       this.amount = transfer.getAmount();
       this.transferType = transfer.getTransferType();
   }
}
