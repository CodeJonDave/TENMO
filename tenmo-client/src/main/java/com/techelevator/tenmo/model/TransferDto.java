package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferDto {
    private int transferType;
    private int transferStatus;
    private int accountFromUsername;
    private int accountToUsername;
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
