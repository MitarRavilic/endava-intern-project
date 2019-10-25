package com.endava.server.util;

import com.endava.server.model.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class ListingResolveHelper {
   private UserAccount user1SendingAccount;
   private UserAccount user1ReceivingAccount;
   private UserAccount user2SendingAccount;
   private UserAccount user2ReceivingAccount;
   private BigDecimal amount;
   private BigDecimal convertedAmount;
}
