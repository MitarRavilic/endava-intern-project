package com.endava.server.service;

import com.endava.server.dto.TransferDTO;
import com.endava.server.dto.request.ListingDTOCreate;
import com.endava.server.dto.response.ListingDTOView;
import com.endava.server.exception.ListingException;
import com.endava.server.exception.ResourceNotFoundException;
import com.endava.server.model.Listing;
import com.endava.server.model.Transfer;
import com.endava.server.model.User;
import com.endava.server.model.UserAccount;
import com.endava.server.repository.ListingRepository;
import com.endava.server.repository.TransferRepository;
import com.endava.server.repository.UserRepository;
import com.endava.server.util.MoneyUtility;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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



    // == Create methods ==
    public ListingDTOView createListing(ListingDTOCreate listingDTOCreate) {
        if(listingDTOCreate.getTargetCurrencyCode() != listingDTOCreate.getBaseCurrencyCode()) {
            User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
            Listing listing = new Listing(user,
                    listingDTOCreate.getBaseCurrencyCode(),
                    listingDTOCreate.getAmount(),
                    listingDTOCreate.getTargetCurrencyCode(),
                    listingDTOCreate.getRate());
            listingRepository.save(listing);

            ListingDTOView dto = new ListingDTOView(listing);
            return dto;
        } else throw new ListingException("Base and Target currencies cannot be similar");
        }

    // == Read methods ==
    public List<ListingDTOView> getAll(){
       List<Listing> listings = listingRepository.findAllByIsActiveEquals(true);
       List<ListingDTOView> dto = listings.stream().map(listing -> new ListingDTOView(listing)).collect(Collectors.toList());
       return dto;
    }

    public List<ListingDTOView> getListingsByBaseCurrencyAndTargetCurrency(String baseCurrency, String targetCurrency){
        List<Listing> listings = listingRepository.findAllByBaseCurrencyCodeAndTargetCurrencyCode(baseCurrency, targetCurrency);
        List<ListingDTOView> dto = listings.stream().map(listing -> new ListingDTOView(listing)).collect(Collectors.toList());
        return dto;
    }

    public List<ListingDTOView> getListingsByBaseCurrency(String baseCurrency){
        List<Listing> listings = listingRepository.findAllByBaseCurrencyCode(baseCurrency);
        List<ListingDTOView> dto = listings.stream().map(listing -> new ListingDTOView(listing)).collect(Collectors.toList());
        return dto;
    }

    public List<ListingDTOView> getListingsByTargetCurrency(String targetCurrency){
        List<Listing> listings = listingRepository.findAllByTargetCurrencyCode(targetCurrency);
        List<ListingDTOView> dto = listings.stream().map(listing -> new ListingDTOView(listing)).collect(Collectors.toList());
        return dto;
    }

    // == Delete Listing ==

        //only the user that created the listing can delete it
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

    // == Perform Listing ==

    public void resolveListing(Long listingId){
        Listing listing = listingRepository.findById(listingId).orElseThrow(() -> new ResourceNotFoundException("Listing", "listingId", listingId));
        if(listing.getIsActive()) {
            User user1 = listing.getUser();
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user2 = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
            List<Transfer> transfers = MoneyUtility.resolveListing(user1, user2, listing.getBaseCurrencyCode(), listing.getTargetCurrencyCode(), listing.getAmount(), listing.getRate());
            listing.setIsActive(false);
            transferRepository.saveAll(transfers);
        }
    }

}
