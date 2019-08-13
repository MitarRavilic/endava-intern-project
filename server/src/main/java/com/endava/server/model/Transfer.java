package com.endava.server.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Transfer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UserAccount sender;

    private UserAccount recipient;

    @Temporal(TemporalType.TIMESTAMP)
    private Date transferredAt;

}
