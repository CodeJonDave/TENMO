package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;

import java.util.List;

public interface TransferDao {
    List<Transfer> getTransfersByUserID(int userId);
    int createTransfer(TransferDto transferDto);
    Transfer getTransferById(int transferId);
    List<Transfer> getPendingTransfersByUserID(int userID);

}
