package com.cbs.dto;

import com.cbs.entity.KycStatus;

public class CustomerResponseDTO {
    private Long customerId;
    private String name;
    private String email;
    private String message;

    private KycStatus kycStatus;

    public Long getCustomerId() {
        return customerId;
    }

    public KycStatus getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(KycStatus kycStatus) {
        this.kycStatus = kycStatus;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
