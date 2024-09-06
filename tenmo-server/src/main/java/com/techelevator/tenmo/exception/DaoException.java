package com.techelevator.tenmo.exception;

public class DaoException extends RuntimeException {
    public DaoException(String message) {
        super(message);
    }
    public DaoException(String message, Exception cause) {
        super(message, cause);
    }
}
