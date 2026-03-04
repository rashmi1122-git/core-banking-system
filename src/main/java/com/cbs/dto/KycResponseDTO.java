package com.cbs.dto;

public class KycResponseDTO {
    private Long customerId;
    private String status;
    private String rejectionReason;

    // constructors
    public KycResponseDTO(Long customerId, String status, String rejectionReason) {
        this.customerId = customerId;
        this.status = status;
        this.rejectionReason = rejectionReason;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}
