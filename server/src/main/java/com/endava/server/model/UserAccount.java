package com.endava.server.model;

import com.endava.server.dto.UserAccountDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @JsonBackReference()
    @ManyToOne
    private User user;

    @Getter
    private String currencyCode; // has no setter, cannot be changed after init

    @Setter
    @Getter
    private  @PositiveOrZero BigDecimal balance;


    public UserAccount(User user, String currencyCode){
        this.user = user;
        this.currencyCode = currencyCode;
        this.balance =  BigDecimal.ZERO;
    }

    public UserAccount(UserAccountDTO userAccountDTO){
        this.user = new User(userAccountDTO.getUser());
        this.currencyCode = userAccountDTO.getCurrencyCode();
        this.balance = userAccountDTO.getBalance();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {return false;}
        if(!(obj instanceof UserAccount)) {return false;}
        return this.currencyCode == ((UserAccount) obj).currencyCode && this.user == ((UserAccount) obj).user;
    }
}

