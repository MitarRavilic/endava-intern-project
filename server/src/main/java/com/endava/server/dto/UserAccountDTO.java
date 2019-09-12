package com.endava.server.dto;


import com.endava.server.model.User;
import com.endava.server.model.UserAccount;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
public class UserAccountDTO {

    private UserDTO user;

    private String currencyCode;

    private BigDecimal balance;

    public UserAccountDTO(UserAccount userAccount) {
        this.user = new UserDTO(userAccount.getUser());
        this.currencyCode = userAccount.getCurrencyCode();
        this.balance = userAccount.getBalance();
    }
}
