package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ConsoleService {
    NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }


    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                BigDecimal value = new BigDecimal(scanner.nextLine());
                if (value.compareTo(BigDecimal.ZERO) > 0) {
                    return value;
                }
                if(value.compareTo(BigDecimal.ZERO) == 0){
                    return null;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printBalance(BigDecimal balance) {
        String currentBalance = currency.format(balance);
        System.out.println("Your current account balance is: " + currentBalance);
    }

    public void printUsersList(List<User> users, AuthenticatedUser currentUser) {
        System.out.println("-------------------------------------------");
        System.out.println("Users");
        System.out.printf("%-10s %-20s%n", "ID", "Name");
        System.out.println("-------------------------------------------");
        if (users.isEmpty() || users.size() == 1) {
            System.out.println("There were no other users found");
        } else {
            for (User user : users) {
                if (!user.equals(currentUser.getUser())) {
                    System.out.printf("%-10d %-20s%n", user.getId(), user.getUsername());
                }
            }
        }
        System.out.println("---------");
    }

    public void printAllTransfers(List<Transfer> transfers, AuthenticatedUser currentUser) {
        System.out.println("-------------------------------------------");
        System.out.println("Transfers");
        System.out.println("ID          From/To                 Amount");
        System.out.println("-------------------------------------------");

        for (Transfer transfer : transfers) {
            int transfer_id = transfer.getTransferId();
            String toFrom = currentUser.getUser().getUsername().equals(transfer.getAccountToUsername()) ?
                    "From: " + transfer.getAccountFromUsername().trim() :
                    "To: " + transfer.getAccountToUsername().trim();
            String amount = currency.format(transfer.getAmount());

            System.out.printf("%-10d %-25s %s%n", transfer_id, toFrom, amount);
        }
        System.out.println("---------");
    }


    public void printPendingTransfers(List<Transfer> transfers) {
        System.out.println("-------------------------------------------");
        System.out.println("Pending Transfers");
        System.out.println("ID          To                     Amount");
        System.out.println("-------------------------------------------");

        for (Transfer transfer : transfers) {
            int transferId = transfer.getTransferId();
            String toUsername = transfer.getAccountToUsername().trim();
            String amount = currency.format(transfer.getAmount());
            System.out.println(transferId + "          " + toUsername + "                     " + amount);
        }
        System.out.println("---------");
    }


    public void printTransfer(Transfer transfer) {
        System.out.println("--------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("--------------------------------------------");
        System.out.println(" ID: " + transfer.getTransferId());
        System.out.println(" From: " + transfer.getAccountFromUsername());
        System.out.println(" To: " + transfer.getAccountToUsername());
        System.out.println(" Type: " + transfer.getTransferType());
        System.out.println(" Status: " + transfer.getTransferStatus());
        System.out.println(" Amount: " + currency.format(transfer.getAmount()));
        System.out.println("--------------------------------------------");
    }

    public void promptForStatusHandling() {
        System.out.println("1: Approve");
        System.out.println("2: Reject");
        System.out.println("0: Don't approve or reject");
        System.out.println("---------");
    }
}