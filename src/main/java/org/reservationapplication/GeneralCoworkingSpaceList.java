package org.reservationapplication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GeneralCoworkingSpaceList {
    private static List<CoworkingSpace> generalCoworkingSpaceList= new ArrayList<CoworkingSpace>();

    public static void addCoworkingSpace(CoworkingSpace coworkingSpace) {
        generalCoworkingSpaceList.add(coworkingSpace);
    }

    public static void removeFromCoworkingSpaceListById(String id) {
        Iterator<CoworkingSpace> iterator = generalCoworkingSpaceList.iterator();
        while (iterator.hasNext()) {
            CoworkingSpace coworkingSpace = iterator.next();
            if (coworkingSpace.getCoworkingSpaceID().equals(id)) {
                iterator.remove();
                System.out.println("Coworking space with ID " + id + " has been removed.");
                return;
            }
        }
        System.out.println("Coworking space with ID " + id + " not found.");
    }

    public static void printGeneralCoworkingSpaceList() {
        for (CoworkingSpace coworkingSpace : generalCoworkingSpaceList) {
            System.out.println(coworkingSpace);
        }
    }

    public static List<CoworkingSpace> getAvailableGeneralCoworkingSpaceList() {
        List<CoworkingSpace> availableCoworkingSpaceList = new ArrayList<>();
        for (CoworkingSpace coworkingSpace : generalCoworkingSpaceList) {

            if (coworkingSpace.getAvailabilityStatus() == AvailabilityStatus.AVAILABLE) {
                availableCoworkingSpaceList.add(coworkingSpace);
            }

        }
        return availableCoworkingSpaceList;
    }

    public static boolean isIDMatch(String id) {
        for (CoworkingSpace coworkingSpace : generalCoworkingSpaceList) {
            if (coworkingSpace.getCoworkingSpaceID().equals(id)) {
                return true;
            }
        }
        return false;
    }



}
