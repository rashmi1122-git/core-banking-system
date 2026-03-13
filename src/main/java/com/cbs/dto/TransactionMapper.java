package com.cbs.dto;

import com.cbs.entity.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class TransactionMapper {

    public TransactionDetails mapToEntity(TransactionRequestDTO dto, Account account) {

        TransactionDetails tx = new TransactionDetails();
        if ("DEBIT".equalsIgnoreCase(dto.getType()))
        {
            tx.setTransactionType(TransactionType.DEBIT);
        }else
        {
            tx.setTransactionType(TransactionType.CREDIT);
        }
        tx.setAmount(dto.getAmount());
        tx.setAccountNumber(dto.getAccountNumber());
        tx.setAccount(account);
        return tx;
    }


    public TransactionResponseDTO mapToDTO(TransactionDetails tx, BigDecimal balance) {

        TransactionResponseDTO response = new TransactionResponseDTO();

        response.setTransactionId("TXN-" + tx.getTransactionId());
        response.setStatus("SUCCESS");
        response.setMessage("Transaction completed successfully");
        response.setAccountNumber(tx.getAccount().getAccountNumber());
        response.setAvailableBalance(balance);
        response.setTimestamp(LocalDateTime.now());

        return response;
    }

    public Account mapToAccountEntity(AccountRequestDTO dto, Customer customer) {

        Account tx = new Account();
        tx.setBalance(dto.getOpeningBalance());
        tx.setAccountNumber("SB-"+System.currentTimeMillis());
        tx.setCustomer(customer);
        return tx;
    }

    public AccountResponseDTO mapToAccountDTO(Account account) {
        AccountResponseDTO response = new AccountResponseDTO();
        response.setAccountNumber(account.getAccountNumber());
        response.setBalance(account.getBalance());
        response.setCustomerId(account.getCustomer().getCustomerId());
        response.setStatus(AccountStatus.ACTIVE);
        response.setAccountType(AccountType.SAVINGS);
        response.setMessage("Account Created Successfully!............");
        return response;
    }

}
