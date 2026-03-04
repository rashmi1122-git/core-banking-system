package com.cbs.service;

import com.cbs.dto.KycRequestDTO;
import com.cbs.dto.KycResponseDTO;
import com.cbs.entity.Customer;
import com.cbs.entity.Kyc;
import com.cbs.entity.KycStatus;
import com.cbs.repository.CustomerRepository;
import com.cbs.repository.KycRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class KycServiceImplement implements KycService{
    private KycRepository kycRepository;
    private CustomerRepository customerRepository;
    public KycServiceImplement(KycRepository kycRepository, CustomerRepository customerRepository) {
        this.kycRepository = kycRepository;
        this.customerRepository = customerRepository;
    }
    @Override
    public String submitKyc(KycRequestDTO request) {

        Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(()->new IllegalArgumentException("customer not found"));

        if (kycRepository.existsByCustomer(customer))
        {
            throw new IllegalArgumentException("Kyc Already submitted");
        }
        if(!request.getPanNumber().matches("^[A-Z]{5}[0-9]{4}[A-Z]{1}$"))
        {
            throw new IllegalArgumentException("Pan card not valid");
        }
        if (!request.getAadhaarNumber().matches("^[0-9]{12}$"))
        {
            throw new IllegalArgumentException("Adhar Number not valid");
        }
        Kyc kyc = new Kyc();
        kyc.setPanNumber(request.getPanNumber());
        kyc.setAadhaarNumber(request.getAadhaarNumber());
        kyc.setAddress(request.getAddress());
        kyc.setStatus(KycStatus.PENDING);
        kyc.setCustomer(customer);
        kyc.setSubmittedAt(LocalDateTime.now());
        kycRepository.save(kyc);
        return "KYC submitted successfully. Status: PENDING";
    }

    //fetch kyc status
    @Override
    public KycResponseDTO getKycStatus(Long customerId) {
        Optional<Kyc> optionalKyc = kycRepository.findByCustomerCustomerId(customerId);
        // If KYC not submitted
        if (optionalKyc.isEmpty()) {
            return new KycResponseDTO(
                    customerId,
                    "NOT_SUBMITTED",
                    null
            );
        }
        Kyc kyc = optionalKyc.get();
        return new KycResponseDTO(
                customerId,
                kyc.getStatus().name(),
                kyc.getRejectReason()
        );

    }

    @Override
    @Transactional
    public String approveKyc(Long customerId) {
        Kyc kyc =kycRepository.findByCustomerCustomerId(customerId).orElseThrow(()->new IllegalArgumentException("KYC not submitted"));
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (kyc.getStatus() !=KycStatus.PENDING)
        {
            throw new IllegalArgumentException("Only PENDING KYC can be approved");
        }
        customer.setKycStatus(KycStatus.VERIFIED);
        kyc.setStatus(KycStatus.VERIFIED);
       // kyc.setRejectReason(null);
        kycRepository.save(kyc);
        return "KYC Approved Successfully";
    }

    @Override
    public String rejectKyc(Long customerId, String reason) {
        Kyc kyc = kycRepository.findById(customerId).orElseThrow(()->new IllegalArgumentException("kyc Not submitted"));
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        if (kyc.getStatus() !=KycStatus.PENDING)
        {
            throw new IllegalArgumentException("Only Pending status can be approved or reject");
        }
        customer.setKycStatus(KycStatus.REJECTED);
        kyc.setStatus(KycStatus.REJECTED);
        kyc.setRejectReason(reason);
        kycRepository.save(kyc);
        return "KYC Rejected Successfully";
    }
}
