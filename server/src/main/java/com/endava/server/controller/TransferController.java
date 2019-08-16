package com.endava.server.controller;

import com.endava.server.model.Transfer;
import com.endava.server.model.UserAccount;
import com.endava.server.service.TransferService;
import com.endava.server.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(path = "/transfer")
public class TransferController {

    @Autowired
    UserAccountService userAccountService;

    @Autowired
    TransferService transferService;

    // sending same currency between 2 users
    @PostMapping(path = "/{senderId}/{recipientId}/{currencyCode}/{amount}")
    public Transfer sendMoney(@PathVariable Long senderId, @PathVariable Long recipientId,@PathVariable String currencyCode, @PathVariable BigDecimal amount){

        Transfer transfer = userAccountService.transferMoneyBetweenAccounts(senderId, recipientId, amount, currencyCode, currencyCode);
        return transferService.createTransfer(transfer);
    }

}
