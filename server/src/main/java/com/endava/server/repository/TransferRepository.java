package com.endava.server.repository;

import com.endava.server.model.Transfer;
import com.endava.server.model.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    Slice<Transfer> findAllBySenderAccountAndRecipientAccount(UserAccount account);
}
