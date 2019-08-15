package com.endava.server.service;

import com.endava.server.dto.UserAccountDTO;
import com.endava.server.exception.ResourceNotFoundException;
import com.endava.server.model.User;
import com.endava.server.model.UserAccount;
import com.endava.server.repository.UserAccountRepository;
import com.endava.server.repository.UserRepository;
import com.endava.server.util.MoneyUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserAccountService {

    @Autowired
    UserAccountRepository userAccountRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    MoneyUtility moneyUtility;
    // create new account or return if it exists
    public UserAccountDTO createUserAccount(Long userId, String currencyCode){
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "userId", userId));
        UserAccount account = userAccountRepository.findByUser_IdAndCurrencyCode(userId, currencyCode).orElse(new UserAccount(user, currencyCode));
        return new UserAccountDTO(userAccountRepository.save(account));
    }




}
