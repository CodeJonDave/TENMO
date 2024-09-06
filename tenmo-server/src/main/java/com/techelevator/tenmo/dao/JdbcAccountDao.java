package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDAO {
    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getUserBalance(int userId) {
        String sql = "SELECT balance " +
                "FROM account " +
                "WHERE user_id = ?";
        try {
            BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
            if (balance == null) {
                throw new DaoException("No balance found for user: " + userId);
            }
            return balance;
        } catch (Exception e) {
            throw new DaoException("Error retrieving balance for user: " + userId, e);
        }
    }

    @Override
    public int increaseBalance(int userId, BigDecimal amount) {
        if (userId <= 0 || amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Invalid account ID or balance.");
        }
        String sql = "UPDATE account " +
                "SET balance = balance + ? " +
                "WHERE user_id = ?";
        try {
            return jdbcTemplate.update(sql, amount, userId);
        } catch (Exception e) {
            throw new DaoException("Error updating balance for account ID: " + userId, e);
        }
    }

    @Override
    public int decreaseBalance(int userId, BigDecimal amount) {
        if (userId <= 0 || amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Invalid account ID or balance.");
        }
        String sql = "UPDATE account " +
                "SET balance = balance - ? " +
                "WHERE user_id = ?";
        try {
            return jdbcTemplate.update(sql, amount, userId);
        } catch (Exception e) {
            throw new DaoException("Error updating balance for account ID: " + userId, e);
        }
    }


}
