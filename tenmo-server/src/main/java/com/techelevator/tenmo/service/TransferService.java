package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.TransferDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TransferService {

    @Autowired
    private final TransferDao transferDao;

    public TransferService(TransferDao transferDao) {
        this.transferDao = transferDao;
    }

    public boolean createNewTransfer(TransferDto transferDto) {

        int transferId = transferDao.createTransfer(
                transferDto
        );

        return transferId > 0;
    }


}
