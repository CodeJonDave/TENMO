package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(path = "transfer/")
@Validated
public class TransferController {

    @Autowired
    private TransferService transferService;

    private final TransferDao transferDao;

    public TransferController(TransferDao transferDao) {
        this.transferDao = transferDao;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "user/{userId}")
    public ResponseEntity<List<Transfer>> getAllUserTransfers(@PathVariable @Min(value = 1) int userId) {

        List<Transfer> transfers = transferDao.getTransfersByUserID(userId);

        if (transfers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(transfers);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "user/{userId}/pending")
    public ResponseEntity<List<Transfer>> getAllUserPendingTransfers(@PathVariable @Min(value = 1) int userId) {

        List<Transfer> transfers = transferDao.getPendingTransfersByUserID(userId);

        if (transfers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(transfers);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "{transferId}")
    public ResponseEntity<Transfer> getTransferById(@PathVariable @Min(value = 1) int transferId) {

        Transfer transfer = transferDao.getTransferById(transferId);

        if (transfer != null) {
            return ResponseEntity.ok(transfer);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }



    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "")
    public ResponseEntity<Boolean> createNewTransfer(@RequestBody @Valid TransferDto transferDto) {
        return ResponseEntity.ok(transferService.createNewTransfer(transferDto));
    }



}
