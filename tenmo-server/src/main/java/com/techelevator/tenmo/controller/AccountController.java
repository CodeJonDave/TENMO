package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.exception.InvalidBalanceException;
import com.techelevator.tenmo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@RestController
@RequestMapping(path = "account/")
@Validated
public class AccountController {

    @Autowired
    private AccountService accountService;


    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "user/{userId}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable int userId) {
        return ResponseEntity.ok(accountService.getUserBalance(userId));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(path = "{userId}/balance/increase")
    public ResponseEntity<Boolean> increase(
            @PathVariable @Min(value = 1) int userId,
            @Valid @RequestBody BigDecimal newBalance) throws InvalidBalanceException {
        return ResponseEntity.ok(accountService.increaseBalance(userId, newBalance));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(path = "{userId}/balance/decrease")
    public ResponseEntity<Boolean> decrease(
            @PathVariable @Min(value = 1) int userId,
            @Valid @RequestBody BigDecimal newBalance) throws InvalidBalanceException {
        return ResponseEntity.ok(accountService.decreaseBalance(userId, newBalance));
    }
}
