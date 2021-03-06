package com.endava.server.repository;

import com.endava.server.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByUser_IdAndCurrencyCode(Long userId, String currencyCode);
    ArrayList<UserAccount> findAllByUser_Id(Long userId);
    ArrayList<UserAccount> findAllByCurrencyCode(String currencyCode);
}
