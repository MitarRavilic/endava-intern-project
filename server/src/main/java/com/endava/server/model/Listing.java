package com.endava.server.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;



@Entity
@Data
@NoArgsConstructor
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long listingId;

    @NotNull
    @ManyToOne
    private User user;

    @NotEmpty
    private String baseCurrencyCode;

    @NotNull
    private BigDecimal amount;

    @NotEmpty
    private String targetCurrencyCode;

    @NotNull
    private BigDecimal rate;

    @NotNull
    private LocalDateTime issuedAt;

    @NotNull
    private LocalDateTime expiresAt;

    @NotNull
    private Boolean isActive;

    public Listing(User user, String baseCurrencyCode, BigDecimal amount, String targetCurrencyCode, BigDecimal rate) {
        this.user = user;
        this.user.getUserAccountWithCurrency(baseCurrencyCode).ifPresent(userAccount -> userAccount.reserve(amount));
        this.baseCurrencyCode = baseCurrencyCode;
        this.amount = amount;
        this.targetCurrencyCode = targetCurrencyCode;
        this.rate = rate;
        this.issuedAt = LocalDateTime.now();
        this.expiresAt = LocalDateTime.now().plusMinutes(30);
        this.isActive = true;
    }

    public void delayExpiration(long delayInMinutes){
        this.expiresAt.plus(delayInMinutes, ChronoUnit.MINUTES);
    }

    public void deactivate() {
        this.isActive = false;
    }

}


