package com.techelevator.tenmo.services.util;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.ConsoleService;


import java.util.List;

public class UserUtil {
    private static final ConsoleService consoleService = new ConsoleService();

    public static int handleGetUser(List<User> userList, AuthenticatedUser currentUser) {
        // Initialize targetUserId to -1 (indicating no valid selection yet)
        int targetUserId = -1;

        // Print the list of users
        consoleService.printUsersList(userList, currentUser);

        // Prompt for the user ID to send to
        targetUserId = consoleService.promptForMenuSelection("Enter ID of user you are sending to (0 to cancel):");

        // Handle cancellation
        if (targetUserId == 0) {
            System.out.println("Transaction canceled.");
            return -1;
        }

        // Ensure the user is not trying to send funds to themselves
        if (currentUser.getUser().getId() == targetUserId) {
            System.out.println("You cannot send/request funds to yourself. Transaction canceled.");
            return -1;
        }

        // Validate the user ID
        if (checkForUser(targetUserId, userList)) {
            return targetUserId;
        } else {
            System.out.println("User Not Found. Transaction canceled.");
            return -1;
        }
    }

    private static boolean checkForUser(int targetUserId, List<User> userList) {
        boolean isFound = false;
        for (User user : userList) {
            if (user.getId() == targetUserId) {
                isFound = true;
                break;
            }
        }
        return isFound;
    }
}
