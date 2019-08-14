package com.endava.server.service;

import com.endava.server.model.UserAccount;
import com.endava.server.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAccountService {

    @Autowired
    UserAccountRepository userAccountRepository;


}
