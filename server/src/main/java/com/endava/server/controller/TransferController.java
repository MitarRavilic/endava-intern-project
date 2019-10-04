package com.endava.server.controller;

import com.endava.server.dto.TransferDTO;
import com.endava.server.service.TransferService;
import com.endava.server.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllTransfers(){
        return ResponseEntity.ok(transferService.getAllTransfers());
    }

    // sending same currency between 2 users
    @PostMapping(path = "/{recipientId}/{currencyCode}/{amount}")
    public ResponseEntity<?> sendMoney(@PathVariable Long recipientId, @PathVariable String currencyCode, @PathVariable BigDecimal amount){

        TransferDTO dto = transferService.transferMoneyBetweenUsers(recipientId, currencyCode, amount);
        return ResponseEntity.ok(dto);
    }


    @PostMapping(path = "/deposits/{currencyCode}/{amount}")
    public ResponseEntity<?> depositMoney( @PathVariable String currencyCode, @PathVariable BigDecimal amount){
        TransferDTO dto = transferService.depositMoney(currencyCode, amount);
        return ResponseEntity.ok(dto);
    }

    //convert money single user
    @PostMapping(path = "/conversions")
    public ResponseEntity<?> convert(@RequestBody TransferDTO transferDTO){
        TransferDTO dto = transferService.convertMoney(transferDTO.getSenderAccountId(), transferDTO.getSenderCurrencyCode(), transferDTO.getRecipientCurrencyCode(), transferDTO.getAmount());
        return ResponseEntity.ok(dto);
    }

    @PostMapping(path = "/conversion-transfers")
    public ResponseEntity<?> sendAndConvertMoney(@RequestBody HashMap<String, String> transferData){
        String recipientUsername = transferData.get("recipientUsername");
        BigDecimal amount = new BigDecimal(transferData.get("amount"));
        String baseCurrency = transferData.get("baseCurrency");
        String targetCurrency = transferData.get("targetCurrency");

        TransferDTO dto = transferService.sendAndConvertMoneyBetweenUsers(recipientUsername, amount, baseCurrency, targetCurrency);

        return ResponseEntity.ok(dto);
    }


}
