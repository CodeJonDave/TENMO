package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.*;
import com.techelevator.tenmo.services.util.BalanceUtil;
import com.techelevator.tenmo.services.util.TransferUtil;
import com.techelevator.tenmo.services.util.UserUtil;
import com.techelevator.util.BasicLogger;
import com.techelevator.util.BasicLoggerException;

import java.math.BigDecimal;
import java.util.List;

public class App {
    //Constants for messages and API base URL
    private static final String CANCELED = "Transaction was canceled";
    private static final String API_BASE_URL = "http://localhost:8080/";
    //Service instances for interacting with different parts of the application
    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService(API_BASE_URL);
    private final UserServices userServices = new UserServices(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);
    //Holds the current authenticated user
    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run(); // Start the application
    }

    private void run() {
        consoleService.printGreeting(); //Greet the user
        loginMenu();// Show the login menu
        if (currentUser != null) { // if login was successful
            // Set the authenticated user for all service instances
            userServices.setCurrentUser(currentUser);
            accountService.setCurrentUser(currentUser);
            transferService.setCurrentUser(currentUser);
            mainMenu(); // Show the main menu
        }

    }

    private void loginMenu() {  //Displays the login menu and handles the user input
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister(); // Register a new user
            } else if (menuSelection == 2) {
                handleLogin(); // Login an existing user
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause(); // pause for the user to see the message
            }
        }
    }

    private void handleRegister() { // Handles user registration
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        try {
            if (authenticationService.register(credentials)) {
                BasicLogger.log("New user was registered: " + credentials.getUsername());
                System.out.println("Registration successful. You can now login.");
            } else {
                consoleService.printErrorMessage();
            }
        } catch (BasicLoggerException ex) {
            throw new BasicLoggerException("An Error occurred when logging user registration");
        }
    }

    private void handleLogin() {  // Handles user login
        UserCredentials credentials = consoleService.promptForCredentials();

        try {
            currentUser = authenticationService.login(credentials);
            if (currentUser == null) {
                consoleService.printErrorMessage();
            } else {
                BasicLogger.log("User: " + credentials.getUsername() + " logged in successfully.");
                System.out.println("Welcome back, " + credentials.getUsername() + "!");
            }
        } catch (BasicLoggerException ex) {
            throw new BasicLoggerException("An Error occurred when logging user login");
        }
    }

    private void mainMenu() { // Displays the main menu and handles user input
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance(); // View the current users account balance
            } else if (menuSelection == 2) {
                viewTransferHistory(); // View the current users transfer history
            } else if (menuSelection == 3) {
                viewPendingRequests(); // View the current users pending transfer requests
            } else if (menuSelection == 4) {
                sendBucks(); // Send TE bucks to another user
            } else if (menuSelection == 5) {
                requestBucks(); // Request TE bucks from another user
            } else if (menuSelection == 0) {
                continue; // Exit the menu
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause(); // Pause for the user to see the message
        }
    }

    private void viewCurrentBalance() { // Retrieves and displays the current user's balance
        try {
            BigDecimal balance = accountService.getCurrentUserBalance();
            BasicLogger.log("Retrieved current balance for user: " + currentUser.getUser().getUsername());
            consoleService.printBalance(balance);
        } catch (BasicLoggerException ex) {
            throw new BasicLoggerException("An Error occurred when logging user actions");
        }
    }

    private void viewTransferHistory() { // Retrieves and displays the current user's transfer history
        try {
            BasicLogger.log("Current user " + currentUser.getUser().getUsername() + " viewing Transfer History:");
            List<Transfer> transfersList = transferService.getAllUserTransfers();
            Transfer transfer = null;

            if (TransferUtil.checkForTransfers(transfersList)) {
                BasicLogger.log("No transfers found for user: " + currentUser.getUser().getUsername());
                System.out.println("No transfers found for user: " + currentUser.getUser().getUsername());
                return;
            }
            consoleService.printAllTransfers(transfersList, currentUser);

            int transferId = -1;
            transferId = consoleService.promptForMenuSelection("Please enter transfer ID to view details (0 to cancel): ");
            if (transferId > 0) {
                for (Transfer t : transfersList) {
                    if (t.getTransferId() == transferId) {
                        transfer = transferService.transferDetails(transferId);
                    }
                }
            } else {
                BasicLogger.log("User " + currentUser.getUser().getUsername() + " canceled viewing history.");
                return;
            }

            if (transfer != null) {
                BasicLogger.log("Viewed transfer details for ID: " + transferId);
                consoleService.printTransfer(transfer);
            } else {
                System.out.println("There was an error in retrieving the transfer details. Returning to main menu");
            }
        } catch (BasicLoggerException ex) {
            throw new BasicLoggerException("Error logging user interactions. Please try again later");
        }
    }


    private void viewPendingRequests() {  // Displays and handles pending transfer requests
        try {
            Transfer transfer =null;
            BasicLogger.log("Current user: " + currentUser.getUser().getUsername() + " is viewing pending requests.");
            List<Transfer> transfersList = transferService.pendingTransfers();  // Get all pending transfers
            if (TransferUtil.checkForTransfers(transfersList)) {
                BasicLogger.log("No pending transfers found for user: " + currentUser.getUser().getUsername());
                System.out.println("No pending transfers found for user: " + currentUser.getUser().getUsername());
                return;
            }
            consoleService.printPendingTransfers(transfersList);  // Print all pending transfers

            int transferId = consoleService.promptForMenuSelection("Please enter transfer ID to view details (0 to cancel): ");
            if (transferId > 0) {
                for (Transfer t : transfersList) {
                    if (t.getTransferId() == transferId) {
                        transfer = transferService.transferDetails(transferId);
                    }
                }
                if (transfer != null) {
                    BasicLogger.log("User: " + currentUser.getUser().getUsername() + " selected transfer ID: " + transferId);
                    handlePendingTransfer(transferId, transfer);  // Handle the pending transfer (approve or reject)
                } else {
                    System.out.println("There was an error retrieving the transfer details.");
                }
            } else {
                BasicLogger.log("User " + currentUser.getUser().getUsername() + " canceled viewing pending requests.");
            }
        } catch (BasicLoggerException ex) {
            throw new BasicLoggerException("Error logging user interactions. Please try again later");
        }
    }

    private void sendBucks() { // Handles the sending of TE bucks to another user
        BasicLogger.log("User initiated selecting a recipient to send funds to.");
        List<User> userList = userServices.listOfUsers(); // Get list of users
        boolean success = false; // Flag to track success
        BigDecimal amount = null;

        int targetUserId = UserUtil.handleGetUser(userList, currentUser); // Get the recipient's user_id
        if (targetUserId == -1) {
            BasicLogger.log("User canceled selecting a recipient. " + CANCELED);
            return; // User canceled the selection
        }

        amount = BalanceUtil.handleGetAmount(); // Get the amount to send
        if (amount == null) {
            BasicLogger.log("User canceled entering an amount. " + CANCELED);
            return;  // User canceled entering the amount
        }

        BigDecimal available = accountService.getCurrentUserBalance();  // Get the current user's available balance
        success = BalanceUtil.checkAvailableFunds(amount, available);  // Check if the user has enough funds
        if (!success) {
            BasicLogger.log("User canceled transaction because of insufficient funds.");
            return;  // User has insufficient funds
        }

        success = transferService.sendMoney(amount, targetUserId);
        if (success) {  // Handle the actual adjustment of the accounts
            accountService.decreaseBalance(currentUser.getUser().getId(), amount);
            accountService.increaseBalance(targetUserId, amount);
            BasicLogger.log("User: " + currentUser.getUser().getUsername() + " successfully sent: " + amount);
            System.out.println("Funds have been successfully sent.");
        } else {
            System.out.println("There was an error transferring funds. Transaction canceled.");
        }
    }

    private void requestBucks() { // Handles the requesting of TE bucks from another user
        BasicLogger.log("User initiated selecting a user to request funds from.");
        List<User> userList = userServices.listOfUsers(); // Get the list
        boolean success = false;
        BigDecimal amount = null;

        int targetUserId = UserUtil.handleGetUser(userList, currentUser); // Get the recipient of the requests user_id
        if (targetUserId == -1) {
            BasicLogger.log("User canceled selecting a recipient. Transaction was canceled");
            return; // User selection was canceled
        }

        amount = BalanceUtil.handleGetAmount(); // Get the amount to send
        if (amount == null) {
            BasicLogger.log("User canceled entering an amount. " + CANCELED);
            return;  // User canceled entering the amount
        }

        success = transferService.requestMoney(amount, targetUserId);  //start transfer if amount is not null
        if (success) {
            BasicLogger.log("User: " + currentUser.getUser().getUsername() + " successfully request for: " + amount);
            System.out.println("Request for fund has been successfully sent");
        }
    }

    private void handlePendingTransfer(int transferId, Transfer transfer) {  // Handles pending transfer approval or rejection
        consoleService.promptForStatusHandling(); // Prompt user for status handling options (approve or reject)
        int menuSelection = consoleService.promptForMenuSelection("Please choose an option:");
        switch (menuSelection) {
            case 1: // If user selects 1, handle transfer approval
                handleApproveTransfer(transferId, transfer);
                BasicLogger.log("Transfer: " + transferId + " has been approved");
                break;
            case 2: // If user selects 2, handle transfer rejection
                handleRejectTransfer(transferId);
                BasicLogger.log("Transfer: " + transferId + " has been rejected");
                break;
            default: // If the user selects any other option, return to the main menu
                System.out.println("Returning to main menu.");
                BasicLogger.log("User: " + currentUser.getUser().getUsername() + " took no action with Transfer: " + transferId);
                break;
        }
    }

    private void handleApproveTransfer(int transferId, Transfer transfer) {  // Handles the approval of a transfer
        boolean success = false;
        BigDecimal available = accountService.getCurrentUserBalance(); // Get the  current user's available balance
        User userTo = userServices.getUserByName(transfer); // Get the user receiving the transfer of funds\

        if (!BalanceUtil.checkAvailableFunds(transfer.getAmount(), available)) {  // Check to see if the current user has enough funds to approve the transfer
            System.out.println("You cannot approve this transfer due to insufficient funds.");
            return;
        }

        if (transferId == transfer.getTransferId()) { // Check if the transfer ID matches and approve the transfer
            success = transferService.approveTransfer(transferId);
        }

        if (success) { // If the transfer is approved successfully, update the account balances
            accountService.increaseBalance(userTo.getId(), transfer.getAmount());  // Increase the balance of the receiving user
            accountService.decreaseBalance(currentUser.getUser().getId(), transfer.getAmount()); // Decrease the balance of the current user
            System.out.println("You have successfully approved the transaction");  // Notify the user of successful approval
        }
    }

    private void handleRejectTransfer(int transferId) { // Handles the rejection of a transfer
        boolean success = transferService.rejectTransfer(transferId); // Attempt to reject the transfer
        if (success) { // If the transfer is successfully rejected, notify the user
            System.out.println("The transfer was rejected");
        }
    }
}