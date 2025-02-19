package org.reservationapplication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CoworkingSpaceService {
    private List<CoworkingSpace> generalCoworkingSpace;

    public CoworkingSpaceService(List<CoworkingSpace> generalCoworkingSpace) {
        this.generalCoworkingSpace = generalCoworkingSpace;
    }

    public void addCoworkingSpace(CoworkingSpace coworkingSpace) {
        generalCoworkingSpace.add(coworkingSpace);
    }

    public void removeFromCoworkingSpaceById(long id) {
        Iterator<CoworkingSpace> iterator = generalCoworkingSpace.iterator();
        while (iterator.hasNext()) {
            CoworkingSpace coworkingSpace = iterator.next();
            if (coworkingSpace.getCoworkingSpaceID() == id) {
                iterator.remove();
                System.out.println("Coworking space with ID " + id + " has been removed.");
                return;
            }
        }
        System.out.println("Coworking space with ID " + id + " not found.");
    }

    public void printGeneralCoworkingSpace() {
        for (CoworkingSpace coworkingSpace : generalCoworkingSpace) {
            System.out.println(coworkingSpace);
        }
    }

    public List<CoworkingSpace> getAvailableGeneralCoworkingSpace() {
        List<CoworkingSpace> availableCoworkingSpaceList = new ArrayList<>();
        for (CoworkingSpace coworkingSpace : generalCoworkingSpace) {

            if (coworkingSpace.getAvailabilityStatus() == AvailabilityStatus.AVAILABLE) {
                availableCoworkingSpaceList.add(coworkingSpace);
            }

        }
        return availableCoworkingSpaceList;
    }

    public boolean isIDMatch(long id) {
        for (CoworkingSpace coworkingSpace : generalCoworkingSpace) {
            if (coworkingSpace.getCoworkingSpaceID() == id) {
                return true;
            }
        }
        return false;
    }


}
