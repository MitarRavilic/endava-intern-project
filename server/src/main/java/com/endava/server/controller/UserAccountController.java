package com.endava.server.controller;

import com.endava.server.dto.UserAccountDTO;
import com.endava.server.model.Transfer;
import com.endava.server.service.UserAccountService;
import com.endava.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/account")
public class UserAccountController {

    @Autowired
    UserAccountService userAccountService;

    @Autowired
    UserService userService;

    @PostMapping(path = "/{userId}/{currencyCode}")
    public UserAccountDTO createUserAccount(@PathVariable @Valid Long userId, @PathVariable @Valid String currencyCode){

        return userAccountService.createUserAccount(userId, currencyCode);
    }




}
