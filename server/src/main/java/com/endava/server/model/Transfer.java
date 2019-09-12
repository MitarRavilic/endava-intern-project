package com.endava.server.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@NoArgsConstructor
public class Transfer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Getter
    private UserAccount senderAccount;

    @Getter
    private String senderCurrencyCode;

    @ManyToOne
    @Getter
    private UserAccount recipientAccount;

    @Getter
    private String recipientCurrencyCode;

    @Getter
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Getter
    private BigDecimal amount;

    @Getter
    @Enumerated(EnumType.STRING)
    private TransferType transferType;


    public Transfer(UserAccount sender, UserAccount recipient, BigDecimal amount, TransferType transferType){
        this.senderAccount = sender;
        this.senderCurrencyCode = sender.getCurrencyCode();
        this.recipientAccount = recipient;
        this.recipientCurrencyCode = recipient.getCurrencyCode();
        this.createdAt = new Date();
        this.amount = amount;
        this.transferType = transferType;
    }

//    public Transfer(TransferDTO transferDTO){
//        this.sender = new UserAccount(transferDTO.getSender());
//        this.senderCurrencyCode = transferDTO.getSenderCurrencyCode();
//        this.recipient = new UserAccount(transferDTO.getRecipient());
//        this.recipientCurrencyCode = transferDTO.getRecipientCurrencyCode();
//        this.amount = transferDTO.getAmount();
//    }
}
