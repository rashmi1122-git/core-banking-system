package com.cbs.service;

import com.cbs.dto.*;

import java.math.BigDecimal;

public interface TransactionService {

   TransactionResponseDTO processTransaction(TransactionRequestDTO request);
   DepositResponseDTO deposit(DepositRequestDTO request);
   DepositResponseDTO withdraw(DepositRequestDTO requestDTO);
    String transfer(TransferRequestDTO1  dto1);
}
