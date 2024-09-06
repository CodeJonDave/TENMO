package com.techelevator.tenmo.services.util;

import com.techelevator.tenmo.services.ConsoleService;

import java.math.BigDecimal;

public class BalanceUtil {

    private static final ConsoleService consoleService = new ConsoleService();

    public static BigDecimal handleGetAmount() {
        BigDecimal amount = consoleService.promptForBigDecimal("Enter amount: ");
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("You can not send/request an amount less than $0.01. Transaction canceled");
            return null;
        } else {
            return amount;
        }
    }

    public static boolean checkAvailableFunds(BigDecimal amount, BigDecimal available) {
      return available.compareTo(amount) >= 0;
    }
}
