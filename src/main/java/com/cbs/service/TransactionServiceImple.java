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
        details.setAmount(request.getAmount());
        details.setAccountNumber(request.getAccountNumber());
        details.setReferenceNumber(UUID.randomUUID().toString());
        responseDTO.setTransactionDate(LocalDateTime.now());
        try {
             Account account = accountRepository.findByAccountNumber(request.getAccountNumber()).orElseThrow(()->new DublicateCustomerException("Account Not Found"));
             if (!account.getStatus().equals(AccountStatus.ACTIVE))
             {
                 throw new RuntimeException("Account is not active");
             }
//             if (account.getStatus() !=AccountStatus.ACTIVE)
//             {
//             throw  new DublicateCustomerException("Account is not Active");
//             }
             account.setBalance(account.getBalance().add(request.getAmount()));
             details.setAccount(account);

             details.setStatus(TransactionStatus.SUCCESS);
            responseDTO.setMessage("Amount Deposit Successfully");
            responseDTO.setNewBalance(account.getBalance());
            transactionDetailsRepository.save(details);
        }catch (Exception  e)
             {
                 details.setStatus(TransactionStatus.FAILED);
                 responseDTO.setMessage("Amount Deposit Failed Transaction");

                 if (details.getAccount()!=null)
                 {
                     transactionDetailsRepository.save(details);
                 }
                  throw e;
             }
        return responseDTO;
    }

    @Override
    @Transactional
    public DepositResponseDTO withdraw(DepositRequestDTO requestDTO) {

        DepositResponseDTO dto = new DepositResponseDTO();

        Account account = accountRepository
                .findByAccountNumber(requestDTO.getAccountNumber())
                .orElseThrow(() -> new DublicateCustomerException("Account not found"));

        if (account.getBalance().compareTo(requestDTO.getAmount()) < 0) {
            throw new DublicateCustomerException("Insufficient Balance");
        }

        // update balance
        account.setBalance(account.getBalance().subtract(requestDTO.getAmount()));

        TransactionDetails transaction = new TransactionDetails();
        transaction.setAccount(account);
        transaction.setAccountNumber(requestDTO.getAccountNumber());
        transaction.setAmount(requestDTO.getAmount());
        transaction.setTransactionType(TransactionType.DEBIT);
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setReferenceNumber(UUID.randomUUID().toString());
        transaction.setStatus(TransactionStatus.SUCCESS);

        transactionDetailsRepository.save(transaction);

        dto.setMessage("Withdraw Success");
        dto.setNewBalance(account.getBalance());

        return dto;
    }
    @Override
    @Transactional
    public String transfer(TransferRequestDTO1 dto1) {

        Account sender = accountRepository.findByAccountNumber(dto1.getFromAccount()).orElseThrow(()->new DublicateCustomerException("Account No Found Of Sender"));
        Account receiver=accountRepository.findByAccountNumber(dto1.getToAccount()).orElseThrow(()->new DublicateCustomerException("Receiver Account Not Found"));

        if (sender.getBalance().compareTo(dto1.getAmount())<0)
        {
            throw new RuntimeException("Insufficient Balance");
        }
        if (sender.getStatus() != AccountStatus.ACTIVE|| receiver.getStatus() !=AccountStatus.ACTIVE)
        {
            throw new DublicateCustomerException("Account is not active");
        }
         String reference = UUID.randomUUID().toString();

          sender.setBalance(sender.getBalance().subtract(dto1.getAmount()));
          receiver.setBalance(receiver.getBalance().add(dto1.getAmount()));

          TransactionDetails credit = new TransactionDetails();
           credit.setAmount(dto1.getAmount());
           credit.setTransactionType(TransactionType.CREDIT);
           credit.setReferenceNumber(reference);
           credit.setTransactionTime(LocalDateTime.now());
            credit.setAccount(receiver);

           TransactionDetails debit = new TransactionDetails();
           debit.setTransactionType(TransactionType.DEBIT);
           debit.setAmount(dto1.getAmount());
           debit.setReferenceNumber(reference);
           debit.setTransactionTime(LocalDateTime.now());
           debit.setAccount(sender);
          transactionDetailsRepository.save(credit);
          transactionDetailsRepository.save(debit);
          return "Transfer Successful";
    }

}
