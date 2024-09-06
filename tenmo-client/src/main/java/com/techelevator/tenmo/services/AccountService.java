package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.util.BasicLogger;
import com.techelevator.tenmo.services.util.Header;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {
    private final String baseUrl;
    private final RestTemplate restTemplate;
    private AuthenticatedUser currentUser;

    public AccountService(String baseUrl) {
        this.baseUrl = baseUrl + "account/";
        this.restTemplate = new RestTemplate();
    }

    public void setCurrentUser(AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
    }

    public BigDecimal getCurrentUserBalance() {
        HttpEntity<String> entity = Header.generateHeaders(currentUser);

        int id = currentUser.getUser().getId();

        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(
                    baseUrl + "user/" + id + "/balance",
                    HttpMethod.GET,
                    entity,
                    BigDecimal.class
            );
            BigDecimal balance = response.getBody();
            BasicLogger.log("Retrieved user balance for user: " + id);
            return balance;
        } catch (RestClientException ex) {
            BasicLogger.logError("Error fetching balance for user: " + id, ex);
            System.err.println("Error fetching balance: " + ex.getMessage());
            return null;
        }
    }


    public void increaseBalance(int id, BigDecimal amount) {
        HttpEntity<BigDecimal> entity = Header.generateHeadersWithBody(currentUser, amount);

        try {
            ResponseEntity<Boolean> response = restTemplate.exchange(
                    baseUrl + id + "/balance/increase",
                    HttpMethod.PUT,
                    entity,
                    Boolean.class
            );
            response.getBody();
            BasicLogger.log("Increased balance for user: " + id);
        } catch (RestClientException ex) {
            BasicLogger.logError("Error increasing balance for user: " + id, ex);
            System.err.println("Error updating balance: " + ex.getMessage());
        }
    }

    public void decreaseBalance(int id, BigDecimal amount) {
        HttpEntity<BigDecimal> entity = Header.generateHeadersWithBody(currentUser, amount);

        try {
            ResponseEntity<Boolean> response = restTemplate.exchange(
                    baseUrl + id + "/balance/decrease",
                    HttpMethod.PUT,
                    entity,
                    Boolean.class
            );
            response.getBody();
            BasicLogger.log("Decreased balance for user: " + id);
        } catch (RestClientException ex) {
            BasicLogger.logError("Error decreasing balance for user: " + id, ex);
            System.err.println("Error updating balance: " + ex.getMessage());
        }
    }

}
