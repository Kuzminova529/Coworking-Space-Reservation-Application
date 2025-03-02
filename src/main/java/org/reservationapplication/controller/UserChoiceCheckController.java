package org.reservationapplication.controller;

import org.reservationapplication.logger.Loggers;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UserChoiceCheckController {
    private static Scanner scanner = new Scanner(System.in);

    public long getUserChoiceLong() {
        while (true) {
            try {
                Loggers.USER_LOGGER.info("Enter: ");
                long choice = scanner.nextLong();
                scanner.nextLine();
                return choice;
            } catch (InputMismatchException e) {
                Loggers.USER_LOGGER.warn("Invalid input. Please enter a number.");
                Loggers.TECHNICAL_LOGGER.error("Input error: incorrect number format.", e);
                scanner.next();
            }
        }
    }
  
    public int getUserChoiceInt() {
        while (true) {
            try {
                Loggers.USER_LOGGER.info("Enter: ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                return choice;
            } catch (InputMismatchException e) {
                Loggers.USER_LOGGER.warn("Invalid input. Please enter a number.");
                Loggers.TECHNICAL_LOGGER.error("Input error: incorrect number format.", e);
                scanner.next();
            }
        }
    }

    public double getUserChoiceDouble() {
        while (true) {
            try {
                Loggers.USER_LOGGER.info("Enter: ");
                double choice = scanner.nextDouble();
                scanner.nextLine();
                return choice;
            } catch (InputMismatchException e) {
                Loggers.USER_LOGGER.warn("Invalid input. Please enter a double number.");
                Loggers.TECHNICAL_LOGGER.error("Input error: incorrect number format.", e);
                scanner.next();
            }
        }
    }
}