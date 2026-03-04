package com.cbs.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DepositResponseDTO {
    private String message;
    private BigDecimal newBalance;
    private LocalDateTime transactionDate;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BigDecimal getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(BigDecimal newBalance) {
        this.newBalance = newBalance;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}
