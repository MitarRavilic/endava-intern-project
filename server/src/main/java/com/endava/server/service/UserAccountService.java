package com.endava.server.service;

import com.endava.server.dto.UserAccountDTO;
import com.endava.server.exception.CurrencyMismatchException;
import com.endava.server.exception.ResourceNotFoundException;
import com.endava.server.model.Transfer;
import com.endava.server.model.User;
import com.endava.server.model.UserAccount;
import com.endava.server.repository.UserAccountRepository;
import com.endava.server.repository.UserRepository;
import com.endava.server.util.MoneyUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class UserAccountService {

    @Autowired
    UserAccountRepository userAccountRepository;
    @Autowired
    UserRepository userRepository;

    @Transactional
    public void createUserAccount(String currencyCode) {
        if (MoneyUtility.isCurrencyCodeValid(currencyCode)) {
           String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
            user.addAccount(currencyCode);
        } else throw new CurrencyMismatchException();
    }






}
