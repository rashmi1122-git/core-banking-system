package com.cbs.repository;

import com.cbs.entity.Customer;
import com.cbs.entity.Kyc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KycRepository extends JpaRepository<Kyc,Long> {
    boolean existsByCustomer(Customer customer);
    Optional<Kyc> findByCustomerCustomerId(Long customerId);}
