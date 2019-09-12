package com.endava.server.service;

import com.endava.server.dto.TransferDTO;
import com.endava.server.exception.ResourceNotFoundException;
import com.endava.server.model.Transfer;
import com.endava.server.model.TransferType;
import com.endava.server.model.User;
import com.endava.server.model.UserAccount;
import com.endava.server.repository.TransferRepository;
import com.endava.server.repository.UserAccountRepository;
import com.endava.server.repository.UserRepository;
import com.endava.server.util.MoneyUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class TransferService {

    @Autowired
    TransferRepository transferRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserAccountRepository userAccountRepository;

    public Transfer createTransfer(Transfer transfer) {
        return transferRepository.save(transfer);
    }

    @Transactional
    public List<Transfer> getAllTransfers(){
       return transferRepository.findAll();
    }

    @Transactional
    public TransferDTO transferMoneyBetweenUsers(Long senderId, Long recipientId, String currencyCode, BigDecimal amount) {
        if (userRepository.findById(senderId).isPresent() && userRepository.findById(recipientId).isPresent()) {
            User sender = userRepository.findById(senderId).orElseThrow(() -> new  ResourceNotFoundException("User", "senderId", senderId));
            User recipient = userRepository.findById(recipientId).orElseThrow(() -> new  ResourceNotFoundException("User", "recipientId", recipientId));

            Optional<UserAccount> senderAccountOpt = sender.getUserAccountWithCurrency(currencyCode);
            Optional<UserAccount> recipientAccountOpt = recipient.getUserAccountWithCurrency(currencyCode);
            UserAccount senderAccount;
            UserAccount recipientAccount;
            if (senderAccountOpt.isEmpty()) {
                throw new ResourceNotFoundException("UserAccount", "currencyCode", currencyCode);
            } else if (recipientAccountOpt.isEmpty()) {
                senderAccount = senderAccountOpt.get();
                recipientAccount = new UserAccount(recipient, currencyCode);
            } else {
                senderAccount = senderAccountOpt.get();
                recipientAccount = recipientAccountOpt.get();
            }

            MoneyUtility.withdrawMoneyFromAccount(senderAccount, amount);
            MoneyUtility.depositMoneyToAccount(recipientAccount, amount);
            Transfer transfer = transferRepository.save(new Transfer(senderAccount, recipientAccount, amount, TransferType.PEER_TRANSFER));
            return new TransferDTO(transfer);
        } else throw new EntityNotFoundException("Sender or Recipient user doesn't exist");
    }
    @Transactional
    public TransferDTO depositMoney(Long userId, String currencyCode, BigDecimal amount){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","userId", userId));
        Optional<UserAccount> accountWithCurrency = user.getUserAccountWithCurrency(currencyCode);
        UserAccount wantedAccount;
        if(accountWithCurrency.isEmpty()) {
            wantedAccount = new UserAccount(user, currencyCode);
            user.getAccounts().add(wantedAccount);
        } else {
            wantedAccount = accountWithCurrency.get();
        }
        MoneyUtility.depositMoneyToAccount(wantedAccount, amount);
        Transfer transfer = new Transfer(wantedAccount, wantedAccount, amount, TransferType.DEPOSIT);
        transferRepository.save(transfer);
        return new TransferDTO(transfer);
    }
    @Transactional
    public TransferDTO withdrawMoney(Long userId, String currencyCode, BigDecimal amount){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","userId", userId));
        UserAccount wantedAccount = user.getUserAccountWithCurrency(currencyCode).orElseThrow(() -> new ResourceNotFoundException("UserAccount","currencyCode", currencyCode));
        MoneyUtility.withdrawMoneyFromAccount(wantedAccount, amount);
        Transfer transfer = new Transfer(wantedAccount, wantedAccount, amount, TransferType.WITHDRAWAL);
        transferRepository.save(transfer);
        return new TransferDTO(transfer);
    }


    @Transactional
    public TransferDTO convertMoney(Long userId, String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        Optional<UserAccount> accountWithBaseCurrency = user.getUserAccountWithCurrency(baseCurrencyCode);
        Optional<UserAccount> accountWithTargetCurrency = user.getUserAccountWithCurrency(targetCurrencyCode);

        UserAccount baseCurrencyAccount;
        UserAccount targetCurrencyAccount;
        if(accountWithBaseCurrency.isEmpty()) {                 //if base account is empty throw. if target account is empty create new.
            throw new ResourceNotFoundException("UserAccount", "currencyCode", baseCurrencyCode);
        } else {
            baseCurrencyAccount = accountWithBaseCurrency.get();
            if (accountWithTargetCurrency.isEmpty()) {
                targetCurrencyAccount = new UserAccount(user, targetCurrencyCode);

            } else {
                targetCurrencyAccount = accountWithTargetCurrency.get();
            }
        }
        MoneyUtility.sendAndConvertMoney(baseCurrencyAccount, targetCurrencyAccount, amount);
       Transfer transfer = new Transfer(baseCurrencyAccount, targetCurrencyAccount, amount, TransferType.CONVERSION);
       return new TransferDTO(transfer);
    }

    @Transactional
    public TransferDTO sendAndConvertMoneyBetweenUsers(Long senderId, Long recipientId, BigDecimal amount, String baseCurrencyCode, String targetCurrencyCode){
        User sender = userRepository.findById(senderId).orElseThrow(() -> new ResourceNotFoundException("User", "senderId", senderId));
        User recipient = userRepository.findById(recipientId).orElseThrow(() -> new ResourceNotFoundException("User", "recipientId", recipientId));
        Optional<UserAccount> accountWithBaseCurrency = sender.getUserAccountWithCurrency(baseCurrencyCode);
        Optional<UserAccount> accountWithTargetCurrency = recipient.getUserAccountWithCurrency(targetCurrencyCode);
        UserAccount baseCurrencyAccount;
        UserAccount targetCurrencyAccount;
        if(accountWithBaseCurrency.isEmpty() || accountWithTargetCurrency.isEmpty()) {
            throw new ResourceNotFoundException("UserAccount", "currencyCode", baseCurrencyCode + " or " + targetCurrencyCode);
        } else {
            baseCurrencyAccount = accountWithBaseCurrency.get();
            targetCurrencyAccount = accountWithTargetCurrency.get();
        }
        MoneyUtility.sendAndConvertMoney(baseCurrencyAccount, targetCurrencyAccount, amount);
        Transfer transfer = new Transfer(baseCurrencyAccount, targetCurrencyAccount, amount, TransferType.LISTING);
        return new TransferDTO(transfer);
    }



}
