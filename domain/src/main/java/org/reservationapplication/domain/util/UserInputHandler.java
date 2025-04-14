package org.reservationapplication.domain.util;


import org.reservationapplication.logger.Loggers;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UserInputHandler {
    static Scanner scanner = new Scanner(System.in);

    public static final InputSupplierCreator<Integer,Integer> intSupplierCreator = new InputSupplierCreator<>(Loggers.USER_LOGGER::info, () -> {
        while (true) {
            try {
                Integer i = scanner.nextInt();
                scanner.nextLine();
                return i;
            } catch (InputMismatchException e) {
                Loggers.TECHNICAL_LOGGER.error("Invalid choice ", e);
                Loggers.USER_LOGGER.warn("Invalid choice, please enter number.");
                scanner.nextLine();
            }
        }
    });

    public static final InputSupplierCreator<Long,Long> longSupplierCreator = new InputSupplierCreator<>(Loggers.USER_LOGGER::info, () -> {
        while (true) {
            try {
                Long l = scanner.nextLong();
                scanner.nextLine();
                return l;
            } catch (InputMismatchException e) {
                Loggers.TECHNICAL_LOGGER.error("Invalid choice ", e);
                Loggers.USER_LOGGER.warn("Invalid choice, please enter number.");
                scanner.nextLine();
            }
        }
    });

    public static final InputSupplierCreator<Double, Double> doubleSupplierCreator = new InputSupplierCreator<>(Loggers.USER_LOGGER::info, () -> {
        while (true) {
            try {
                Double d = scanner.nextDouble();
                scanner.nextLine();
                return d;
            } catch (InputMismatchException e) {
                Loggers.TECHNICAL_LOGGER.error("Invalid choice ", e);
                Loggers.USER_LOGGER.warn("Invalid choice, please enter number.");
                scanner.nextLine();
            }
        }
    });

    public  static final InputSupplierCreator<String, String> stringSupplierCreator = new InputSupplierCreator<>(Loggers.USER_LOGGER::info, () -> {
        String s = scanner.nextLine();
        return s;
    });

}
