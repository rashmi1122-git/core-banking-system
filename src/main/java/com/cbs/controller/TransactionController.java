package com.cbs.controller;

import com.cbs.dto.*;
import com.cbs.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bank/transaction")
public class TransactionController {

    private TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping("/transactions")
    public ResponseEntity<TransactionResponseDTO> transaction(@RequestBody TransactionRequestDTO dto)
    {
        return ResponseEntity.ok(service.processTransaction(dto));
    }
     @PostMapping("/deposite")
    public ResponseEntity<DepositResponseDTO> deposite(@RequestBody DepositRequestDTO requestDTO)
    {
        DepositResponseDTO responseDTO= service.deposit(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
    @PostMapping("/withdraw")
    public ResponseEntity<DepositResponseDTO> withdraw(@RequestBody DepositRequestDTO requestDTO)
    {
        DepositResponseDTO responseDTO= service.withdraw(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
    @PostMapping("/transfer")
    public ResponseEntity<String> transfermanyaccount(@RequestBody TransferRequestDTO1 dto1) {
        String s=service.transfer(dto1);
        return ResponseEntity.ok(s);
    }
}
