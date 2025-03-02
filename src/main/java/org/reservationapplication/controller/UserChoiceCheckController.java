package org.reservationapplication.controller;

import org.reservationapplication.logger.TechnicalLoggable;
import org.reservationapplication.logger.UserLoggable;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UserChoiceCheckController implements TechnicalLoggable, UserLoggable {
    private static Scanner scanner = new Scanner(System.in);

    public long getUserChoiceLong() {
        while (true) {
            try {
                System.out.print("Enter: ");
                long choice = scanner.nextLong();
                scanner.nextLine();
                return choice;
            } catch (InputMismatchException e) {
                getUserLogger().warn("The user entered an incorrect value. A number was expected.");
                getTechnicalLogger().error("Input error: incorrect number format.", e);
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
    }
  
    public int getUserChoiceInt() {
        while (true) {
            try {
                System.out.print("Enter: ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                return choice;
            } catch (InputMismatchException e) {
                getUserLogger().warn("The user entered an incorrect value. A number was expected.");
                getTechnicalLogger().error("Input error: incorrect number format.", e);
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
    }

    public double getUserChoiceDouble() {
        while (true) {
            try {
                System.out.print("Enter: ");
                double choice = scanner.nextDouble();
                scanner.nextLine();
                return choice;
            } catch (InputMismatchException e) {
                getUserLogger().warn("The user entered an incorrect value. Double was expected.");
                getTechnicalLogger().error("Input error: incorrect double number format.", e);
                System.out.println("Invalid input. Please enter a double number.");
                scanner.next();
            }
        }
    }
}