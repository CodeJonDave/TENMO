package com.techelevator.util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BasicLogger {

    private static PrintWriter pw = null;

    private static final String DIRECTORY_NAME = "tenmo-client";
    private static
    final String ERROR_LOG_SUFFIX = "_errors.log";

    public static void log(String message) {
        try {
            if (pw == null) {
                String userDir = System.getProperty("user.dir");

                if (!userDir.endsWith(DIRECTORY_NAME)) {
                    userDir += File.separator + DIRECTORY_NAME;
                }

                String logFilename = userDir + File.separator + "logs/" + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE) + ".log";
                pw = new PrintWriter(new FileOutputStream(logFilename, true));

            }
            pw.println(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + " " + message);
            pw.flush();
        } catch (FileNotFoundException e) {
            throw new BasicLoggerException(e.getMessage());

        }
    }

    public static void
    logError(String message, Throwable throwable) {
        try {
            if (pw == null) {
                String userDir = System.getProperty("user.dir");

                if (!userDir.endsWith(DIRECTORY_NAME)) {
                    userDir += File.separator + DIRECTORY_NAME;
                }

                String
                        errorFilename = userDir + File.separator + "logs/" + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE) + ERROR_LOG_SUFFIX;
                pw = new PrintWriter(new FileOutputStream(errorFilename, true));
            }

            StringBuilder errorMessage = new StringBuilder(message);
            if (throwable != null) {
                errorMessage.append(System.lineSeparator());
                errorMessage.append("Caused by: ");
                errorMessage.append(throwable.getClass().getName());
                errorMessage.append(": ");
                errorMessage.append(throwable.getMessage());
                errorMessage.append(System.lineSeparator());
                for (StackTraceElement element : throwable.getStackTrace()) {
                    errorMessage.append("\tat ").append(element).append(System.lineSeparator());
                }
            }
            pw.println(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + " " + errorMessage);
            pw.flush();
        } catch (FileNotFoundException e) {
            throw new BasicLoggerException(e.getMessage());  // Consider a more specific exception type
        }
    }
}