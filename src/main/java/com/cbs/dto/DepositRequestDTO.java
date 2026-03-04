package com.cbs.dto;

import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;

public class DepositRequestDTO {
    private String accountNumber;
    @NotNull
    private BigDecimal amount;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
