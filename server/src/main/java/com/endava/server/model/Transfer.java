package com.endava.server.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Transfer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @Getter
    private UserAccount sender;

    @ManyToOne
    @Getter
    private UserAccount recipient;

    @Getter
    @Temporal(TemporalType.TIMESTAMP)
    private Date transferredAt;

    public Transfer(UserAccount sender, UserAccount recipient){
        this.sender = sender;
        this.recipient = recipient;
        this.transferredAt = new Date();
    }
}
