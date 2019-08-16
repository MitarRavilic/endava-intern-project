package com.endava.server.service;

import com.endava.server.model.Transfer;
import com.endava.server.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

    @Autowired
    TransferRepository transferRepository;

    public Transfer createTransfer(Transfer transfer){
       return transferRepository.save(transfer);
    }
}
