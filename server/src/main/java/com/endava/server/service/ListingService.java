package com.endava.server.service;


import com.endava.server.dto.request.CurrencyPairRequest;
import com.endava.server.dto.request.ListingDTOCreate;
import com.endava.server.dto.response.ListingDTOView;
import com.endava.server.dto.response.ListingRateBounds;
import com.endava.server.exception.ListingException;
import com.endava.server.exception.ResourceNotFoundException;
import com.endava.server.model.*;
import com.endava.server.repository.ListingRepository;
import com.endava.server.repository.TransferRepository;
import com.endava.server.repository.UserAccountRepository;
import com.endava.server.repository.UserRepository;
import com.endava.server.util.ListingResolveHelper;
import com.endava.server.util.MoneyUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListingService {

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAccountRepository accountRepository;

    private Logger logger = LoggerFactory.getLogger(ListingService.class);
    // == Create methods ==
    @Transactional
    public ListingDTOView createListing(ListingDTOCreate listingDTOCreate) {
        if (listingDTOCreate.getTargetCurrencyCode() != listingDTOCreate.getBaseCurrencyCode()) {
            ListingRateBounds bounds = MoneyUtility.getBoundsForPair(listingDTOCreate.getBaseCurrencyCode(), listingDTOCreate.getTargetCurrencyCode());
            if (listingDTOCreate.getRate().compareTo(bounds.getRateMax()) < 0 && listingDTOCreate.getRate().compareTo(bounds.getRateMin()) > 0) {
                User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new ResourceNotFoundException("User", "username", "username") {
                });
                Listing listing = new Listing(user,
                        listingDTOCreate.getBaseCurrencyCode(),
                        listingDTOCreate.getAmount(),
                        listingDTOCreate.getTargetCurrencyCode(),
                        listingDTOCreate.getRate());
                //listingRepository.save(listing);

                ListingDTOView dto = new ListingDTOView(listing);
                return dto;
            } else throw new ListingException("listing rate out of bounds");
            } else throw new ListingException("Base and Target currencies cannot be similar");
        }


    // == Read methods ==
    public List<ListingDTOView> getAllListings(){
       List<Listing> listings = listingRepository.findAllByIsActiveEqualsOrderByListingIdAsc(true);
       List<ListingDTOView> dto = listings.stream().map(listing -> new ListingDTOView(listing)).collect(Collectors.toList());
       return dto;
    }
    @Transactional
    public List<ListingDTOView> getListingsByBaseCurrencyAndTargetCurrency(String baseCurrency, String targetCurrency){
        List<Listing> listings = listingRepository.findAllByBaseCurrencyCodeAndTargetCurrencyCode(baseCurrency, targetCurrency);
        List<ListingDTOView> dto = listings.stream().map(listing -> new ListingDTOView(listing)).collect(Collectors.toList());
        return dto;
    }

    @Transactional
    public List<ListingDTOView> getListingsByBaseCurrency(String baseCurrency){
        List<Listing> listings = listingRepository.findAllByBaseCurrencyCode(baseCurrency);
        List<ListingDTOView> dto = listings.stream().map(listing -> new ListingDTOView(listing)).collect(Collectors.toList());
        return dto;
    }

    @Transactional
    public List<ListingDTOView> getListingsByTargetCurrency(String targetCurrency){
        List<Listing> listings = listingRepository.findAllByTargetCurrencyCode(targetCurrency);
        List<ListingDTOView> dto = listings.stream().map(listing -> new ListingDTOView(listing)).collect(Collectors.toList());
        return dto;
    }

    // == Delete Listing ==

        //only the user that created the listing can delete it
    @Transactional
    public void deleteListing(Long listingId){
        Listing listing = listingRepository.findById(listingId).orElseThrow(() -> new ResourceNotFoundException("Listing", "listingId", listingId));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(listing.getUser().getUsername() == username){
            boolean successfulUnReserve = userRepository.findByUsername(username)
                    .get()
                    .getUserAccountWithCurrency(listing.getBaseCurrencyCode()).get()
                    .unReserve(listing.getAmount());
            if (successfulUnReserve) {
                listingRepository.delete(listing);
            }
       }
    }

    // == Resolve Listing ==
    @Transactional
    public void resolveListing(Long listingId){
        Listing listing = listingRepository.findById(listingId).orElseThrow(() -> new ResourceNotFoundException("Listing", "listingId", listingId));
        if(listing.getIsActive()) {
            User user1 = listing.getUser();
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user2 = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
            ListingResolveHelper result = MoneyUtility.resolveListing(user1, user2, listing.getBaseCurrencyCode(), listing.getTargetCurrencyCode(), listing.getAmount(), listing.getRate());
            accountRepository.saveAll(Arrays.asList(result.getUser1SendingAccount(), result.getUser1ReceivingAccount(), result.getUser2SendingAccount(), result.getUser2ReceivingAccount()));
            listing.setIsActive(false);
            transferRepository.saveAll(Arrays.asList(
                    new Transfer(result.getUser1SendingAccount(), result.getUser2ReceivingAccount(), result.getAmount(), TransferType.LISTING),
                    new Transfer(result.getUser2SendingAccount(), result.getUser1ReceivingAccount(), result.getConvertedAmount(), TransferType.LISTING)
            ));

            listingRepository.save(listing);
        }
    }

    // == Utility methods ==

    public ListingRateBounds getPairBounds(String baseCurrency, String targetCurrency){
       return MoneyUtility.getBoundsForPair(baseCurrency, targetCurrency);
    }
}
