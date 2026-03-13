package com.cbs.controller;

import com.cbs.dto.KycRejectRequestDTO;
import com.cbs.dto.KycRequestDTO;
import com.cbs.dto.KycResponseDTO;
import com.cbs.service.KycService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bank/kyc")
public class KycController {

    private KycService kycService;
    public KycController(KycService kycService) {
        this.kycService = kycService;
    }

    @PostMapping("/submit")
    public ResponseEntity<String> kycSubmit(@RequestBody KycRequestDTO requestDTO)
    {
       String  kyc=kycService.submitKyc(requestDTO);
       return ResponseEntity.status(HttpStatus.CREATED).body(kyc);
    }

    @PostMapping("/approveKyc/{customerId}")
    public ResponseEntity<String> approveKyc(@PathVariable("customerId") Long customerId)
    {
        String s =kycService.approveKyc(customerId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(s);
    }

    @PutMapping("/reject")
    public ResponseEntity<String> rejectKyc(@RequestBody KycRejectRequestDTO request) {
        String kyc=kycService.rejectKyc(request.getCustomerId(),request.getReason());
      return ResponseEntity.ok(kyc);
    }

    @GetMapping("/fetchKyc/{customerId}")
    public ResponseEntity<KycResponseDTO> getKycStatus(@PathVariable("customerId") Long customerId)
    {
     KycResponseDTO s= ((kycService.getKycStatus(customerId)));
        return ResponseEntity.ok(s);
    }

}
