package com.endava.server.repository;

import com.endava.server.model.Listing;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {

    List<Listing> findAllByBaseCurrencyCodeAndTargetCurrencyCode(String baseCurrencyCode, String targetCurrencyCode);

    List<Listing> findAllByBaseCurrencyCode(String baseCurrencyCode);

    List<Listing> findAllByTargetCurrencyCode(String targetCurrencyCode);
    List<Listing> findAllByIsActiveEqualsOrderByListingIdAsc(boolean isActive);
}
