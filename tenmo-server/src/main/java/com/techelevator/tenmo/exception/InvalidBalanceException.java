package com.techelevator.tenmo.exception;

public class InvalidBalanceException extends Throwable {
    public InvalidBalanceException(String message) {
        super(message);
    }
}