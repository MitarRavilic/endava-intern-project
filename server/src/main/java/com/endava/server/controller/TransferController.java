package com.endava.server.controller;

import com.endava.server.dto.TransferDTO;
import com.endava.server.dto.request.ConvertMoneyRequest;
import com.endava.server.dto.request.DepositRequest;
import com.endava.server.dto.request.SendMoneyRequest;
import com.endava.server.dto.request.WithdrawRequest;
import com.endava.server.service.TransferService;
import com.endava.server.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

import java.util.List;

@RestController
@RequestMapping(path = "/transfers")
public class TransferController {

    @Autowired
    UserAccountService userAccountService;

    @Autowired
    TransferService transferService;

    // == Get Methods ==
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllTransfers(){
        return ResponseEntity.ok(transferService.getAllTransfers());
    }

    @GetMapping("/account/{currencyCode}")
    public ResponseEntity<?> getAllTransfersForUserAccount(@PathVariable String currencyCode) {
    List<TransferDTO> dto = transferService.getAllTransfersForAccount(currencyCode);
    return ResponseEntity.ok(dto);
    }


    // sending same currency between 2 users
    @PostMapping(path = "/{recipientId}/{currencyCode}/{amount}")
    public ResponseEntity<?> sendMoney(@RequestBody SendMoneyRequest request){

        TransferDTO dto = transferService.transferMoneyBetweenUsers(request.getRecipientUsername(), request.getCurrencyCode(), request.getAmount());
        return ResponseEntity.ok(dto);
    }


    @PostMapping(path = "/deposits")
    public ResponseEntity<?> depositMoney(@RequestBody DepositRequest request){
        TransferDTO dto = transferService.depositMoney(request.getCurrencyCode(), request.getAmount());
        return ResponseEntity.ok(dto);
    }

    @PostMapping(path = "/withdrawals")
    public ResponseEntity<?> withdrawMoney(@RequestBody WithdrawRequest request){
        TransferDTO dto = transferService.withdrawMoney(request.getCurrencyCode(), request.getAmount());
        return ResponseEntity.ok(dto);
    }

    //convert money single user
    @PostMapping(path = "/conversions")
    public ResponseEntity<?> convert(@RequestBody @Valid ConvertMoneyRequest request){
        TransferDTO dto = transferService.convertMoney( request.getBaseCurrencyCode(), request.getTargetCurrencyCode(), request.getAmount());
        return ResponseEntity.ok(dto);
    }




}
