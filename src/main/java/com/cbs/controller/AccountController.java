package com.cbs.controller;

import com.cbs.dto.AccountResponseDTO;
import com.cbs.dto.AccountRequestDTO;
import com.cbs.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bank/account")
public class AccountController {
    @Autowired
    private AccountService accountService;


    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    @PostMapping("/create/account")
    public ResponseEntity<AccountResponseDTO> accountCreate(@RequestBody AccountRequestDTO dto)
    {
        return ResponseEntity.ok(accountService.createAccount(dto));
    }

    @GetMapping("/viewaccount")
    public ResponseEntity<List<AccountResponseDTO>> viewaccounts()
    {
    return ResponseEntity.ok(accountService.fetchAccount());
    }


}
