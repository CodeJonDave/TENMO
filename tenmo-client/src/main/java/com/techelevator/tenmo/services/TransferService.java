package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.util.BasicLogger;
import com.techelevator.tenmo.services.util.Header;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TransferService {
    private static final int APPROVED = 2;
    private static final int REJECTED = 3;
    private static final int PENDING = 1;

    private static final int REQUEST = 1;
    private static final int SEND = 2;

    private final String baseUrl;
    private final RestTemplate restTemplate;
    private AuthenticatedUser currentUser;


    public TransferService(String baseUrl) {
        this.baseUrl = baseUrl + "transfer/";
        this.restTemplate = new RestTemplate();
    }

    public List<Transfer> getAllUserTransfers() {
        HttpEntity<String> entity = Header.generateHeaders(currentUser);

        int id = currentUser.getUser().getId();
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(
                    baseUrl + "user/" + id,
                    HttpMethod.GET,
                    entity,
                    Transfer[].class
            );

            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                // Return an empty list if there's no content
                return new ArrayList<>();
            }

            List<Transfer> transfers = Arrays.asList(Objects.requireNonNull(response.getBody()));
            BasicLogger.log("Retrieved all transfers for user: " + id);
            return transfers;
        } catch (RestClientResponseException | ResourceAccessException ex) {
            BasicLogger.logError("Error retrieving all transfers for user: " + id, ex);
            return new ArrayList<>();
        }
    }

    public Boolean sendMoney(BigDecimal amount, int targetUserName) {
        int currentUserId = currentUser.getUser().getId();
        try {
            TransferDto transferDto = new TransferDto();
            transferDto.setTransferStatus(APPROVED);
            transferDto.setTransferType(SEND);
            transferDto.setAccountFromUsername(currentUserId);
            transferDto.setAccountToUsername(targetUserName);
            transferDto.setAmount(amount);

            HttpEntity<TransferDto> entity = Header.generateHeadersWithBody(currentUser, transferDto);
            ResponseEntity<Boolean> response = restTemplate.exchange(
                    baseUrl,
                    HttpMethod.POST,
                    entity,
                    Boolean.class);
            BasicLogger.log("Sent $" + amount + " to user: " + targetUserName);
            return response.getBody();
        } catch (RestClientResponseException ex) {
            BasicLogger.logError("Error sending money to user: " + targetUserName + ". Status code: " + ex.getRawStatusCode(), ex);
            return false;
        } catch (ResourceAccessException ex) {
            BasicLogger.logError("Unexpected error sending money: " + ex.getMessage(), ex);
            return false;
        }
    }

    public Boolean requestMoney(BigDecimal amount, int targetUserId) {
        try {
            TransferDto transferDto = new TransferDto();
            transferDto.setTransferStatus(PENDING);
            transferDto.setTransferType(REQUEST);
            transferDto.setAccountToUsername(currentUser.getUser().getId());
            transferDto.setAccountFromUsername(targetUserId);
            transferDto.setAmount(amount);

            HttpEntity<TransferDto> entity = Header.generateHeadersWithBody(currentUser, transferDto);
            ResponseEntity<Boolean> response = restTemplate.exchange(
                    baseUrl,
                    HttpMethod.POST,
                    entity,
                    Boolean.class
            );
            BasicLogger.log("Requested $" + amount + " from user: " + targetUserId);
            return response.getBody();
        } catch (RestClientResponseException ex) {
            BasicLogger.logError("Error requesting money from user: " + targetUserId + ". Status code: " + ex.getRawStatusCode(), ex);
            return false;
        } catch (ResourceAccessException ex) {
            BasicLogger.logError("Unexpected error requesting money: " + ex.getMessage(), ex);
            return false;
        }
    }

    public List<Transfer> pendingTransfers() {

        HttpEntity<String> entity = Header.generateHeaders(currentUser);

        int id = currentUser.getUser().getId();
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(
                    baseUrl + "user/" + id + "/pending",
                    HttpMethod.GET,
                    entity,
                    Transfer[].class
            );
            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                // Return an empty list if there's no content
                return new ArrayList<>();
            }
            List<Transfer> transfers = Arrays.asList(Objects.requireNonNull(response.getBody()));
            BasicLogger.log("Retrieved pending transfers for user: " + id);
            return transfers;
        } catch (RestClientResponseException ex) {
            BasicLogger.logError("Error retrieving pending transfers for user: " + id + ". Status code: " + ex.getRawStatusCode(), ex);
            return new ArrayList<>();
        } catch (ResourceAccessException ex) {
            BasicLogger.logError("Unexpected error retrieving pending transfers: " + ex.getMessage(), ex);
            return new ArrayList<>();
        }
    }

    public Transfer transferDetails(int transferId) {

        HttpEntity<String> entity = Header.generateHeaders(currentUser);
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(
                    baseUrl + "/" + transferId,
                    HttpMethod.GET,
                    entity,
                    Transfer.class
            );
            Transfer transfer = response.getBody();
            BasicLogger.log("Retrieved the details for transfer: " + transferId);
            return transfer;
        } catch (RestClientResponseException ex) {
            BasicLogger.logError("Error retrieving transfer details for ID: " + transferId + ". Status code: " + ex.getRawStatusCode(), ex);
            return null;
        } catch (ResourceAccessException ex) {
            BasicLogger.logError("Unexpected error retrieving transfer details: " + ex.getMessage(), ex);
            return null;
        }
    }

    private Boolean updateTransferStatus(int transferId, int description) {
        HttpEntity<String> entity = Header.generateHeaders(currentUser);
        try {
            ResponseEntity<Boolean> response = restTemplate.exchange(
                    baseUrl + "status/" + transferId + "/" + description,
                    HttpMethod.PUT,
                    entity,
                    Boolean.class
            );
            BasicLogger.log("Successfully updated transfer status.");
            return response.getBody();
        } catch (RestClientResponseException ex) {
            BasicLogger.logError("Error updating transfer status for ID: " + transferId + ". Status code: " + ex.getRawStatusCode(), ex);
            return false;
        } catch (ResourceAccessException ex) {
            BasicLogger.logError("Unexpected error updating transfer status: " + ex.getMessage(), ex);
            return false;
        }
    }

    public boolean approveTransfer(int transferId) {
        return updateTransferStatus(transferId, APPROVED);
    }


    public boolean rejectTransfer(int transferId) {
        return updateTransferStatus(transferId, REJECTED);
    }

    public void setCurrentUser(AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
    }


}
