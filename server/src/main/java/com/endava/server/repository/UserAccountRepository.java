package com.endava.server.repository;

import com.endava.server.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    UserAccount findByUser_IdAndCurrencyCode(Long userId, String currencyCode);
    List<UserAccount> findAllByUser_Id(Long userId);
}
