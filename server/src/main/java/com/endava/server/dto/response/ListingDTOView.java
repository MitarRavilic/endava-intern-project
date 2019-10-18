package com.endava.server.dto.response;

import com.endava.server.model.Listing;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@NoArgsConstructor
@Getter
public class ListingDTOView {

    private Long listingId;
    private String username;
    private String baseCurrencyCode;
    private BigDecimal amount;
    private String targetCurrencyCode;
    private BigDecimal rate;

public ListingDTOView(Listing listing){
    this.listingId = listing.getListingId();
    this.username = listing.getUser().getUsername();
    this.baseCurrencyCode = listing.getBaseCurrencyCode();
    this.amount = listing.getAmount();
    this.targetCurrencyCode = listing.getTargetCurrencyCode();
    this.rate = listing.getRate();
}
}
