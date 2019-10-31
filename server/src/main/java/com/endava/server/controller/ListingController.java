package com.endava.server.controller;

import com.endava.server.dto.request.ListingDTOCreate;
import com.endava.server.dto.request.ListingResolveRequest;
import com.endava.server.dto.response.ListingDTOView;
import com.endava.server.dto.response.ListingRateBounds;
import com.endava.server.service.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/listings")
public class ListingController {

    @Autowired
    ListingService listingService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
      List<ListingDTOView> dto = listingService.getAllListings();
      return ResponseEntity.ok(dto);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createListing(@RequestBody ListingDTOCreate listing) {
        if (!listing.getBaseCurrencyCode().equals(listing.getTargetCurrencyCode())) {
            ListingDTOView dto = listingService.createListing(listing);
            return ResponseEntity.ok(dto);
        } else { return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();}
    }

    @PutMapping("/resolve")
    public ResponseEntity<?> resolveListing(@RequestBody ListingResolveRequest request){
        listingService.resolveListing(request.getListingId());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/bounds/{baseCurrency}/{targetCurrency}")
    public ResponseEntity<?> getPairBounds(@PathVariable String baseCurrency, @PathVariable String targetCurrency) {
       ListingRateBounds dto = listingService.getPairBounds(baseCurrency, targetCurrency);
       return ResponseEntity.ok(dto);
    }
}
