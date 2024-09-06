package com.techelevator.tenmo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TransferExceptionHandler {

    @ExceptionHandler(InvalidDtoForNewTransfer.class)
    public ResponseEntity<String> handleInvalidDtoForNewTransfer(InvalidDtoForNewTransfer ex){
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }


}
