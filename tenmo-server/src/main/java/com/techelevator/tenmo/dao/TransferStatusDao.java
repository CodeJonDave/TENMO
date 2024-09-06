package com.techelevator.tenmo.dao;

public interface TransferStatusDao {
    int getTransferStatusIdFromDesc(String description);
    boolean updateTransferStatus(int transferId, int status);
}
