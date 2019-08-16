package com.endava.server.service;

import com.endava.server.dto.UserAccountDTO;
import com.endava.server.exception.ResourceNotFoundException;
import com.endava.server.model.Transfer;
import com.endava.server.model.User;
import com.endava.server.model.UserAccount;
import com.endava.server.repository.UserAccountRepository;
import com.endava.server.repository.UserRepository;
import com.endava.server.util.MoneyUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;


@Service
public class UserAccountService {

    @Autowired
    UserAccountRepository userAccountRepository;
    @Autowired
    UserRepository userRepository;

    // create new account or return if it exists
    public UserAccountDTO createUserAccount(Long userId, String currencyCode) throws ResourceNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "userId", userId));
        UserAccount account = userAccountRepository.findByUser_IdAndCurrencyCode(userId, currencyCode).or(() -> {
            return  Optional.of(userAccountRepository.save(new UserAccount(user, currencyCode)));
        }).get();
        return new UserAccountDTO(account);
    }

    public Transfer transferMoneyBetweenAccounts(Long senderUserId, Long recipientUserId, BigDecimal amount, String currencyCodeSenderAccount, String currencyCodeRecipientAccount) throws ResourceNotFoundException {
        UserAccount senderAccount = userAccountRepository.findByUser_IdAndCurrencyCode(senderUserId, currencyCodeSenderAccount).orElseThrow(()-> new ResourceNotFoundException("User", "senderUserId", senderUserId));
        UserAccount recipientAccount = userAccountRepository.findByUser_IdAndCurrencyCode(recipientUserId, currencyCodeSenderAccount).orElseThrow(() -> new ResourceNotFoundException("User", "recipientUserId", recipientUserId));
        Pair<UserAccount, UserAccount> accountPair = MoneyUtility.transferMoney(senderAccount, recipientAccount, currencyCodeSenderAccount, amount);
        userAccountRepository.saveAll(Arrays.asList(accountPair.getFirst(), accountPair.getSecond()));


        return new Transfer(accountPair.getFirst(), accountPair.getSecond());
    }




}
