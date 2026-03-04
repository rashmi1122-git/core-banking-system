package com.cbs.service;

import com.cbs.dto.*;
import com.cbs.entity.*;
import com.cbs.exception.DublicateCustomerException;
import com.cbs.repository.AccountRepository;
import com.cbs.repository.TransactionDetailsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransactionServiceImple implements TransactionService{

    private AccountRepository accountRepository;
    private TransactionDetailsRepository transactionDetailsRepository;
    private TransactionMapper mapper;

    public TransactionServiceImple(AccountRepository accountRepository, TransactionDetailsRepository transactionDetailsRepository, TransactionMapper mapper) {
        this.accountRepository = accountRepository;
        this.transactionDetailsRepository = transactionDetailsRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    //Deposite and withdraw
    public TransactionResponseDTO processTransaction(TransactionRequestDTO request) {
        Account account = new Account();

        account = accountRepository.findByAccountNumber(request.getAccountNumber()).orElseThrow(()->new RuntimeException("Account Not Found"));

        BigDecimal newBalance;
        if ("CREDIT".equalsIgnoreCase(request.getType()))
       {
           newBalance =account.getBalance().add(request.getAmount());
       }else {
           if (account.getBalance().compareTo(request.getAmount())<0)
           {
               throw  new RuntimeException("Insufficient Balance");
           }
           newBalance=account.getBalance().subtract(request.getAmount());
       }
       account.setBalance(newBalance);
        TransactionDetails details =mapper.mapToEntity(request,account);
        transactionDetailsRepository.save(details);
        return mapper.mapToDTO(details, newBalance);
    }

    @Override
    @Transactional
    public DepositResponseDTO deposit(DepositRequestDTO request) {
        if(request.getAmount().compareTo(BigDecimal.ZERO)<=0)
        {
       throw new DublicateCustomerException("Invalid Deposite Amount ");
        }
        TransactionDetails details = new TransactionDetails();
        DepositResponseDTO responseDTO = new DepositResponseDTO();
        details.setTransactionType(TransactionType.CREDIT);
        details.setTransactionTime(LocalDateTime.now());
        details.setAmount(request.getAmount());
        details.setReferenceNumber(UUID.randomUUID().toString());
        responseDTO.setTransactionDate(LocalDateTime.now());
        try {
             Account account = accountRepository.findByAccountNumber(request.getAccountNumber()).orElseThrow(()->new IllegalArgumentException("Account Not Found"));
             if (!account.getStatus().equals(AccountStatus.ACTIVE))
             {
                 throw new RuntimeException("Account is not active");
             }
             if (account.getStatus() !=AccountStatus.ACTIVE)
             {
             throw  new DublicateCustomerException("Account is not Active");
             }
             account.setBalance(account.getBalance().add(request.getAmount()));
             details.setStatus(TransactionStatus.SUCCESS);
            responseDTO.setMessage("Amount Deposit Successfully");
            responseDTO.setNewBalance(account.getBalance());
            transactionDetailsRepository.save(details);
        }catch (Exception  e)
             {
             responseDTO.setMessage("Amount Deposit Failed Transaction");
             details.setStatus(TransactionStatus.FAILED);
             transactionDetailsRepository.save(details);
                 throw e;
         }
        return responseDTO;
    }

    @Override
    @Transactional
    public DepositResponseDTO withdraw(DepositRequestDTO requestDTO) {
        TransactionDetails transaction= new TransactionDetails();
        transaction.setAccountNumber(requestDTO.getAccountNumber());
        transaction.setAmount(requestDTO.getAmount());
        transaction.setTransactionType(TransactionType.DEBIT);
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setReferenceNumber(UUID.randomUUID().toString());
        try {
            Account account = accountRepository
                    .findByAccountNumberForUpdate(requestDTO.getAccountNumber())
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            account.setBalance(account.getBalance().subtract(requestDTO.getAmount()));
            transaction.setStatus(TransactionStatus.SUCCESS);
            transactionDetailsRepository.save(transaction);
        } catch (Exception ex) {
            transaction.setStatus(TransactionStatus.FAILED);
            transactionDetailsRepository.save(transaction);
            throw ex; // rethrow exception
        }
        return null;
    }

    @Override
    public String transfer(TransferRequestDTO1 dto1) {

        Account sender = accountRepository.findByAccountNumberForUpdate(dto1.getFromAccount()).orElseThrow(()->new IllegalArgumentException("Account No Found Of Sender"));
        Account receiver=accountRepository.findByAccountNumberForUpdate(dto1.getToAccount()).orElseThrow(()->new IllegalArgumentException("Receiver Account Not Found"));
        if (sender.getBalance().compareTo(dto1.getAmount())<0)
        {
            throw new RuntimeException("Insufficient Balance");
        }
        sender.setBalance(sender.getBalance().subtract(dto1.getAmount()));
        receiver.setBalance(receiver.getBalance().add(dto1.getAmount()));
          if (!sender.getStatus().equals("ACTIVE") || receiver.getStatus().equals("ACTIVE"))
          {
              throw new RuntimeException("Account is not active");
          }
        String reference = UUID.randomUUID().toString();
          sender.setBalance(sender.getBalance().subtract(dto1.getAmount()));
          receiver.setBalance(receiver.getBalance().add(dto1.getAmount()));
           accountRepository.save(sender);
           accountRepository.save(receiver);

          TransactionDetails credit = new TransactionDetails();
           credit.setAmount(dto1.getAmount());
           credit.setTransactionType(TransactionType.CREDIT);
           credit.setReferenceNumber(reference);
           credit.setTransactionTime(LocalDateTime.now());

           TransactionDetails debit = new TransactionDetails();
           debit.setTransactionType(TransactionType.DEBIT);
           debit.setAmount(dto1.getAmount());
           debit.setReferenceNumber(reference);
           debit.setTransactionTime(LocalDateTime.now());
          transactionDetailsRepository.save(credit);
          transactionDetailsRepository.save(debit);
          return "Transfer Successful";

    }

}
