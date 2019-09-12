package com.endava.server.controller;

import com.endava.server.dto.TransferDTO;
import com.endava.server.model.Transfer;
import com.endava.server.service.TransferService;
import com.endava.server.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;

@RestController
@RequestMapping(path = "/transfers")
public class TransferController {

    @Autowired
    UserAccountService userAccountService;

    @Autowired
    TransferService transferService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllTransfers(){
        return ResponseEntity.ok(transferService.getAllTransfers());
    }

    // sending same currency between 2 users
    @PostMapping(path = "{senderId}/{recipientId}/{currencyCode}/{amount}")
    public ResponseEntity<?> sendMoney(@PathVariable Long senderId, @PathVariable Long recipientId,@PathVariable String currencyCode, @PathVariable BigDecimal amount){

        TransferDTO dto = transferService.transferMoneyBetweenUsers(senderId, recipientId, currencyCode, amount);
        return ResponseEntity.ok(dto);
    }


    @PostMapping(path = "/deposits/{userId}/{currencyCode}/{amount}")
    public ResponseEntity<?> depositMoney(@PathVariable Long userId, @PathVariable String currencyCode, @PathVariable BigDecimal amount){
        TransferDTO dto = transferService.depositMoney(userId, currencyCode, amount);
        return ResponseEntity.ok(dto);
    }

    //convert money single user
    @PostMapping(path = "/conversions")
    public ResponseEntity<?> convert(@RequestBody TransferDTO transferDTO){
        TransferDTO dto = transferService.convertMoney(transferDTO.getSenderAccountId(), transferDTO.getSenderCurrencyCode(), transferDTO.getRecipientCurrencyCode(), transferDTO.getAmount());
        return ResponseEntity.ok(dto);
    }



    @PostMapping(path = "/conversion-transfers")
    public ResponseEntity sendAndConvertMoney(@RequestBody HashMap<String, String> transferData){
        Long senderId = Long.valueOf(transferData.get("senderId"));
        Long recipientId = Long.valueOf(transferData.get("recipientId"));
        BigDecimal amount = new BigDecimal(transferData.get("amount"));
        String baseCurrency = transferData.get("baseCurrency");
        String targetCurrency = transferData.get("targetCurrency");

        TransferDTO dto = transferService.sendAndConvertMoneyBetweenUsers(senderId, recipientId, amount, baseCurrency, targetCurrency);

        return ResponseEntity.ok(dto);
    }


}
