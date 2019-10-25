package com.endava.server.dto;

import com.endava.server.model.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserAccountDTOUserView {
   private String currencyCode;
   private BigDecimal balance;
   private BigDecimal reserved;
}

