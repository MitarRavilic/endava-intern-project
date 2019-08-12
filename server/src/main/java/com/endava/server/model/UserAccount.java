package com.endava.server.model;

import lombok.Data;
import org.javamoney.moneta.Money;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
public class UserAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private String currencyCode;

    private BigDecimal amount;


}
