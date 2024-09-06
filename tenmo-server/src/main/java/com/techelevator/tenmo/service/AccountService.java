package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.exception.BalanceNotFoundException;
import com.techelevator.tenmo.exception.InvalidBalanceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;

@Service
public class AccountService {

    @Autowired
    private final AccountDAO accountDAO;

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public BigDecimal getUserBalance(int userId) throws BalanceNotFoundException {
        BigDecimal balance = accountDAO.getUserBalance(userId);
        if (balance == null) {
            throw new BalanceNotFoundException("Balance not found for user ID: " + userId);
        }
        return balance;
    }

    @Transactional
    public boolean increaseBalance(int userId, BigDecimal amount) throws InvalidBalanceException {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidBalanceException("The balance amount to update is invalid");
        }
        int rowsAffected = accountDAO.increaseBalance(userId, amount);
        return rowsAffected > 0;
    }

    @Transactional
    public Boolean decreaseBalance(int userId, BigDecimal amount) throws InvalidBalanceException {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidBalanceException("The balance amount to update is invalid");
        }
        int rowsAffected = accountDAO.decreaseBalance(userId, amount);
        return rowsAffected > 0;
    }
}
