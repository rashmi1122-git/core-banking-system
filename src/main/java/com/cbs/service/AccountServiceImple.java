package com.cbs.service;

import com.cbs.dto.AccountRequestDTO;
import com.cbs.dto.AccountResponseDTO;
import com.cbs.dto.TransactionMapper;
import com.cbs.entity.Account;
import com.cbs.entity.AccountStatus;
import com.cbs.entity.AccountType;
import com.cbs.entity.Customer;
import com.cbs.exception.DublicateCustomerException;
import com.cbs.repository.AccountRepository;
import com.cbs.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountServiceImple implements AccountService {
    private TransactionMapper mapper;
    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;

    public AccountServiceImple(TransactionMapper mapper, AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.mapper = mapper;
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public AccountResponseDTO createAccount(AccountRequestDTO request) {
        Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(() ->
                new DublicateCustomerException("Customer Not Found"));
        if (!customer.getKycStatus().equals("VERIFIED")) {
            throw new RuntimeException("KYC not verified");
       }
        if (request.getOpeningBalance().compareTo(BigDecimal.valueOf(1000)) < 0) {
            throw new IllegalArgumentException("Minimum balance is 1000");
        }
        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setAccountType(AccountType.SAVINGS);
        account.setBalance(request.getOpeningBalance());
        account.setStatus(AccountStatus.ACTIVE);
        account.setCustomer(customer);
        accountRepository.save(account);
        return mapper.mapToAccountDTO(account);
    }

    @Override
    public List<AccountResponseDTO> fetchAccount() {
        List<Account> account = accountRepository.findAll();
        List<AccountResponseDTO> list = new ArrayList<>();
        for (Account account1 : account) {
            AccountResponseDTO responseDTO = new AccountResponseDTO();
            responseDTO.setAccountNumber(account1.getAccountNumber());
            responseDTO.setAccountType(account1.getAccountType());
            responseDTO.setStatus(AccountStatus.ACTIVE);
            responseDTO.setBalance(account1.getBalance());
            responseDTO.setCustomerId(account1.getCustomer().getCustomerId());
            responseDTO.setMessage("View");
            list.add(responseDTO);
        }
        return list;
    }

    private String generateAccountNumber() {
        return "AC-" + System.currentTimeMillis();
    }
}
