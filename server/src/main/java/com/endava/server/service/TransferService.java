package com.endava.server.service;

import com.endava.server.dto.TransferDTO;
import com.endava.server.dto.response.RateResponse;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransferService {

    @Autowired
    TransferRepository transferRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserAccountRepository userAccountRepository;


    // == Get Methods ==

    public List<Transfer> getAllTransfers(){
       return transferRepository.findAll();
    }

    public List<TransferDTO> getAllTransfersForAccount(String currencyCode) {
       String username = SecurityContextHolder.getContext().getAuthentication().getName();
       User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
       UserAccount account = user.getUserAccountWithCurrency(currencyCode).orElseThrow(() -> new ResourceNotFoundException("UserAccount", "currencyCode", currencyCode));
       List<Transfer> transfers = transferRepository.findAllBySenderAccountOrRecipientAccountOrderByCreatedAtDesc(account, account);
       List<TransferDTO> dto = transfers.stream().map(transfer -> new TransferDTO(transfer)).collect(Collectors.toList());
       return dto;
    }


    @Transactional
    public TransferDTO transferMoneyBetweenUsers(String recipientUsername, String currencyCode, BigDecimal amount) {

        String senderUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> sender = userRepository.findByUsername(senderUsername);
        Optional<User> recipient = userRepository.findByUsername(recipientUsername);

        if (sender.isPresent() && recipient.isPresent()) {

            Optional<UserAccount> senderAccountOpt = sender.get().getUserAccountWithCurrency(currencyCode);
            Optional<UserAccount> recipientAccountOpt = recipient.get().getUserAccountWithCurrency(currencyCode);
            UserAccount senderAccount;
            UserAccount recipientAccount;
            if (senderAccountOpt.isEmpty()) {
                throw new ResourceNotFoundException("UserAccount", "currencyCode", currencyCode);
            } else if (recipientAccountOpt.isEmpty()) {
                senderAccount = senderAccountOpt.get();
                recipientAccount = new UserAccount(recipient.get(), currencyCode);
                userAccountRepository.save(recipientAccount);
                recipient.get().getAccounts().add(recipientAccount);
            } else {
                senderAccount = senderAccountOpt.get();
                recipientAccount = recipientAccountOpt.get();
            }

            MoneyUtility.withdrawMoneyFromAccount(senderAccount, amount);
            MoneyUtility.depositMoneyToAccount(recipientAccount, amount);
            userRepository.saveAll(Arrays.asList(sender.get(), recipient.get()));
            Transfer transfer = transferRepository.save(new Transfer(senderAccount, recipientAccount, amount, TransferType.PEER_TRANSFER));
            return new TransferDTO(transfer);
        } else throw new EntityNotFoundException("Sender or Recipient user doesn't exist");
    }

    @Transactional
    public TransferDTO adminDepositMoney(Long userId, String currencyCode, BigDecimal amount){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","userId", userId));
        Optional<UserAccount> accountWithCurrency = user.getUserAccountWithCurrency(currencyCode);
        UserAccount wantedAccount;
        if(accountWithCurrency.isEmpty()) {
            wantedAccount = new UserAccount(user, currencyCode);
            userAccountRepository.save(wantedAccount);
            user.getAccounts().add(wantedAccount);
        } else {
            wantedAccount = accountWithCurrency.get();
        }
        MoneyUtility.depositMoneyToAccount(wantedAccount, amount);
        Transfer transfer = new Transfer(wantedAccount, wantedAccount, amount, TransferType.DEPOSIT);
        transferRepository.save(transfer);
        userRepository.save(user);
        return new TransferDTO(transfer);
    }

    @Transactional
    public TransferDTO depositMoney(String currencyCode, BigDecimal amount){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User","username", username));
        Optional<UserAccount> accountWithCurrency = user.getUserAccountWithCurrency(currencyCode);
        UserAccount wantedAccount;
        if(accountWithCurrency.isEmpty()) {
            wantedAccount = new UserAccount(user, currencyCode);
            userAccountRepository.save(wantedAccount);
            user.getAccounts().add(wantedAccount);
            userRepository.save(user);
        } else {
            wantedAccount = accountWithCurrency.get();
        }
        MoneyUtility.depositMoneyToAccount(wantedAccount, amount);
        Transfer transfer = new Transfer(wantedAccount, wantedAccount, amount, TransferType.DEPOSIT);
        transferRepository.save(transfer);
        return new TransferDTO(transfer);
    }
    @Transactional
    public TransferDTO withdrawMoney( String currencyCode, BigDecimal amount){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User","username", username));
        UserAccount wantedAccount = user.getUserAccountWithCurrency(currencyCode).orElseThrow(() -> new ResourceNotFoundException("UserAccount","currencyCode", currencyCode));
        MoneyUtility.withdrawMoneyFromAccount(wantedAccount, amount);
        Transfer transfer = new Transfer(wantedAccount, wantedAccount, amount, TransferType.WITHDRAWAL);
        transferRepository.save(transfer);
        return new TransferDTO(transfer);
    }


    @Transactional
    public TransferDTO convertMoney(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
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
                userAccountRepository.save(targetCurrencyAccount);
                user.getAccounts().add(targetCurrencyAccount);
                userRepository.save(user);
            } else {
                targetCurrencyAccount = accountWithTargetCurrency.get();
            }
        }
        MoneyUtility.sendAndConvertMoney(baseCurrencyAccount, targetCurrencyAccount, amount);
        userAccountRepository.saveAll(Arrays.asList(baseCurrencyAccount, targetCurrencyAccount));
      Transfer transfer = transferRepository.save(new Transfer(baseCurrencyAccount, targetCurrencyAccount, amount, TransferType.CONVERSION));
       return new TransferDTO(transfer);
    }

    public RateResponse getRate(String baseCurrency, String targetCurrency) {
        return MoneyUtility.getRateForPair(baseCurrency, targetCurrency);
    }

}
