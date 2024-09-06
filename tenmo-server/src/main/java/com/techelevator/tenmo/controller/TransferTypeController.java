package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferTypeDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;


@RestController
@RequestMapping(path = "transfer/type/")
public class TransferTypeController {

    private final TransferTypeDao transferTypeDao;

    public TransferTypeController(TransferTypeDao transferTypeDao) {
        this.transferTypeDao = transferTypeDao;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "{description}")
    public ResponseEntity<Integer> getTransferTypeId(@PathVariable @NotBlank String description){
        int id = transferTypeDao.getTransferTypeIdFromDesc(description);
        if(id > 0){
            return ResponseEntity.ok(id);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }
}
