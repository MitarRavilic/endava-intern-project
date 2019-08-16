package com.endava.server.model;

import com.endava.server.dto.UserAccountDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
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
    private  @PositiveOrZero BigDecimal balance;

    public UserAccount(UserAccountDTO userAccountDto){
        this.user = userAccountDto.getUser();
        this.currencyCode = userAccountDto.getCurrencyCode();
        this.balance = userAccountDto.getBalance();
    }

    public UserAccount(User user, String currencyCode){
        this.user = user;
        this.currencyCode = currencyCode;
        this.balance =  BigDecimal.TEN;
    }
}

