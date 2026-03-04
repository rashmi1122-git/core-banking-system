package com.cbs.service;

import com.cbs.dto.KycRequestDTO;
import com.cbs.dto.KycResponseDTO;

public interface KycService {

     String submitKyc(KycRequestDTO request);

     KycResponseDTO getKycStatus(Long customerId);
      String approveKyc(Long customerId);
      String rejectKyc(Long customerId, String reason);

}
