package com.endava.server.model;

import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.math.BigDecimal;


@Entity
public class UserAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @ManyToOne
    private User user;

    @Getter
    private String currencyCode; // has no setter, cannot be changed after init

    @Setter
    @Getter
    public @PositiveOrZero BigDecimal balance;






}

