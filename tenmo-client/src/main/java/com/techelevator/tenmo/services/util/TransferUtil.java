package com.techelevator.tenmo.services.util;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public class TransferUtil {

    public static boolean checkForTransfers(List<Transfer> transfersList) {
        if (transfersList.isEmpty()) {
            System.out.println("There were no Transfers found");
            return true;
        }
        return false;
    }
}
