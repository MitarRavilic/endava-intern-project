package com.endava.server.controller;

import com.endava.server.dto.request.ListingDTOCreate;
import com.endava.server.dto.response.ListingDTOView;
import com.endava.server.service.ListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/listings")
public class ListingController {

    @Autowired
    ListingService listingService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
      List<ListingDTOView> dto = listingService.getAll();
      return ResponseEntity.ok(dto);
    }

    @PostMapping("/")
    public ResponseEntity<?> createListing(@RequestBody @Valid ListingDTOCreate listing) {
        if (listing.getBaseCurrencyCode() != listing.getTargetCurrencyCode()) {
            ListingDTOView dto = listingService.createListing(listing);
            return ResponseEntity.ok(dto);
        } else { return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();}
    }

    @PostMapping("/resolve")
    public ResponseEntity<?> resolveListing(@RequestBody Long listingId){
        listingService.resolveListing(listingId);
        return ResponseEntity.ok().build();
    }



}
