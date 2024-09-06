package com.techelevator.tenmo.model;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class TransferDto {

    @NotNull
    @Min(value = 1)
    private int transferType;

    @NotNull
    @Min(value = 1)
    private int transferStatus;

    @NotNull
    @Min(value = 1)
    private int accountFromUsername;

    @NotNull
    @Min(value = 1)
    private int accountToUsername;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    public TransferDto() {
    }

    public TransferDto(int transferType, int transferStatus, int accountFromUsername, int accountToUsername, BigDecimal amount) {
        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.accountFromUsername = accountFromUsername;
        this.accountToUsername = accountToUsername;
        this.amount = amount;
    }

    public int getTransferType() {
        return transferType;
    }

    public void setTransferType(int transferType) {
        this.transferType = transferType;
    }

    public int getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(int transferStatus) {
        this.transferStatus = transferStatus;
    }

    public int getAccountFromUsername() {
        return accountFromUsername;
    }

    public void setAccountFromUsername(int accountFromUsername) {
        this.accountFromUsername = accountFromUsername;
    }

    public int getAccountToUsername() {
        return accountToUsername;
    }

    public void setAccountToUsername(int accountToUsername) {
        this.accountToUsername = accountToUsername;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
