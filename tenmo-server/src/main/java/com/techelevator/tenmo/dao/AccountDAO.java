package com.techelevator.tenmo.dao;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface AccountDAO {
    BigDecimal getUserBalance(int userId);

    int increaseBalance(int userId, BigDecimal amount);

    int decreaseBalance(int userId, BigDecimal amount);



}
