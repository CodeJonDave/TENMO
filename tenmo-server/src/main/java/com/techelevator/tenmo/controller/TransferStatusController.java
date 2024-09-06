package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferStatusDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


@RestController
@RequestMapping(path = "transfer/status/")
public class TransferStatusController {

    private final TransferStatusDao transferStatusDao;

    public TransferStatusController(TransferStatusDao transferStatusDao) {
        this.transferStatusDao = transferStatusDao;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "{description}")
    public ResponseEntity<Integer> getTransferStatusId(@PathVariable @NotBlank String description) {
        int id = transferStatusDao.getTransferStatusIdFromDesc(description);
        if (id > 0) {
            return ResponseEntity.ok(id);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(path = "{transferId}/{description}")
    public ResponseEntity<Boolean> updateTransferStatus(@PathVariable @Min(value = 1) int transferId,
                                                        @PathVariable @Min(value = 1) int description) {


        boolean success = transferStatusDao.updateTransferStatus(transferId, description);

        if (success) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }

    }
}