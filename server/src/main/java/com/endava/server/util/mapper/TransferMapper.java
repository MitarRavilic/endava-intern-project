package com.endava.server.util.mapper;

import com.endava.server.dto.TransferDTO;
import com.endava.server.exception.ResourceNotFoundException;
import com.endava.server.model.Transfer;
import com.endava.server.model.UserAccount;
import com.endava.server.repository.UserAccountRepository;

import org.springframework.beans.factory.annotation.Autowired;

public class TransferMapper {
//
//    @Autowired
//    UserAccountRepository userAccountRepository;
//
//   public TransferDTO toDto(Transfer transfer){
//       return new TransferDTO(transfer.getSenderAccount().getId(),
//                                transfer.getSenderAccount().getCurrencyCode(),
//                                transfer.getRecipientAccount().getId(),
//                                transfer.getRecipientCurrencyCode(),
//                                transfer.getAmount());
//   }
//
//   public Transfer toEntity(TransferDTO transferDTO){
//      UserAccount senderAccount = userAccountRepository.findById(transferDTO.getSenderAccountId()).orElseThrow(() ->new ResourceNotFoundException("UserAccount","senderAccountId", transferDTO.getSenderAccountId()));
//      UserAccount recipientAccount = userAccountRepository.findById(transferDTO.getRecipientAccountId()).orElseThrow(() ->new ResourceNotFoundException("UserAccount","recipientAccountId", transferDTO.getRecipientAccountId()));
//      return new Transfer(senderAccount, recipientAccount, transferDTO.getAmount());
//   }
//




}
