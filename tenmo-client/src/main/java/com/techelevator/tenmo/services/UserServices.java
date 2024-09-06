package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import com.techelevator.tenmo.services.util.Header;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UserServices {

    private final String baseUrl;
    private final RestTemplate restTemplate;
    private AuthenticatedUser currentUser;

    public UserServices(String baseUrl) {
        this.baseUrl = baseUrl + "user/";
        this.restTemplate = new RestTemplate();
    }


    public void setCurrentUser(AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
    }

    public List<User> listOfUsers() {

        HttpEntity<String> entity = Header.generateHeaders(currentUser);
        try {
            ResponseEntity<User[]> response = restTemplate.exchange(
                    baseUrl,
                    HttpMethod.GET,
                    entity,
                    User[].class
            );

            List<User> users = Arrays.asList(Objects.requireNonNull(response.getBody()));
            BasicLogger.log("Successfully retrieved list of users.");
            return users;
        } catch (RestClientResponseException ex) {
            BasicLogger.logError("Error retrieving list of users: " + ex.getMessage(), ex);
            return null;
        }
    }

    public User getUserByName(Transfer transfer) {

        HttpEntity<String> entity = Header.generateHeaders(currentUser);
        try {
            ResponseEntity<User> response = restTemplate.exchange(
                    baseUrl + "/by-username?username=" + transfer.getAccountToUsername(),
                    HttpMethod.GET,
                    entity,
                    User.class
            );

            User user = response.getBody();
            BasicLogger.log("Successfully retrieved user: " + transfer.getAccountToUsername());
            return user;
        } catch (RestClientResponseException ex) {
            BasicLogger.logError("Error retrieving user with userId: " + transfer.getAccountToUsername() + ". " + ex.getMessage(), ex);
            return null;
        }
    }
}