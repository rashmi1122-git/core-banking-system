package com.cbs.repository;

import com.cbs.entity.Account;
import com.cbs.entity.TransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionDetailsRepository extends JpaRepository<TransactionDetails,Long> {

    List<TransactionDetails> findByAccountAccountNumber(String accountNumber);
    Optional<Account> findByAccountNumber(String accountNumber);

}
