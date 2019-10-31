package com.endava.server.repository;

import com.endava.server.model.Transfer;
import com.endava.server.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findAllBySenderAccountOrRecipientAccountOrderByCreatedAtDesc(UserAccount senderAccount, UserAccount recipientAccount);
}
