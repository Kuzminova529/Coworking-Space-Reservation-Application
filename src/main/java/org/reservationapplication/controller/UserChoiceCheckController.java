package org.reservationapplication.controller;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UserChoiceCheckController {
    private static Scanner scanner = new Scanner(System.in);

    public static long getUserChoiceLong() {
        while (true) {
            try {
                System.out.print("Enter: ");
                long choice = scanner.nextLong();
                scanner.nextLine();
                return choice;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
    }


    public static int getUserChoiceInt() {
        while (true) {
            try {
                System.out.print("Enter: ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                return choice;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
    }

    public static double getUserChoiceDouble() {
        while (true) {
            try {
                System.out.print("Enter: ");
                double choice = scanner.nextDouble();
                scanner.nextLine();
                return choice;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a double number.");
                scanner.next();
            }
        }
    }
}