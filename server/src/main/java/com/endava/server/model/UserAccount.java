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

    @Getter
    @Setter
    private  @PositiveOrZero BigDecimal balance;

    @Getter
    @Setter
    private @PositiveOrZero BigDecimal reserved;

//    @Setter
//    @Getter
//    private BigDecimal reserved;

    public UserAccount(User user, String currencyCode){
        this.user = user;
        this.currencyCode = currencyCode;
        this.balance =  BigDecimal.ZERO;
        this.reserved = BigDecimal.ZERO;
    }


    @Override
    public boolean equals(Object obj) {
        if(obj == null) {return false;}
        if(!(obj instanceof UserAccount)) {return false;}
        return this.currencyCode == ((UserAccount) obj).currencyCode && this.user == ((UserAccount) obj).user;
    }

    public boolean reserve(BigDecimal amount){
        if (this.balance.compareTo(amount) > 0){
            this.balance.subtract(amount);
            this.reserved.add(amount);
            return true;
        } return false;
    }

    public boolean unReserve(BigDecimal amount){
        if (this.reserved.compareTo(amount) > 0) {
            this.reserved.subtract(amount);
            this.balance.add(amount);
        } return false;
    }
}

