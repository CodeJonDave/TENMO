package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
    private int transferId;
    private String transferType;
    private String transferStatus;
    private String accountFromUsername;
    private String accountToUsername;
    private BigDecimal amount;

    public Transfer() {
    }

    public Transfer(int transferId, String transferType, String transferStatus, String accountFromUsername, String accountToUsername, BigDecimal amount) {
        this.transferId = transferId;
        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.accountFromUsername = accountFromUsername;
        this.accountToUsername = accountToUsername;
        this.amount = amount;
    }

    // Getters and Setters

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public String getAccountFromUsername() {
        return accountFromUsername;
    }

    public void setAccountFromUsername(String accountFromUsername) {
        this.accountFromUsername = accountFromUsername;
    }

    public String getAccountToUsername() {
        return accountToUsername;
    }

    public void setAccountToUsername(String accountToUsername) {
        this.accountToUsername = accountToUsername;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transferId=" + transferId +
                ", transferType='" + transferType + '\'' +
                ", transferStatus='" + transferStatus + '\'' +
                ", accountFromUsername='" + accountFromUsername + '\'' +
                ", accountToUsername='" + accountToUsername + '\'' +
                ", amount=" + amount +
                '}';
    }
}