package com.cbs.service;

import com.cbs.dto.AccountResponseDTO;
import com.cbs.dto.AccountRequestDTO;

import java.util.List;

public interface AccountService {

     AccountResponseDTO createAccount(AccountRequestDTO request);

     List<AccountResponseDTO> fetchAccount();
}
