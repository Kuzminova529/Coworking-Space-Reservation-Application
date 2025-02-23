package org.reservationapplication.service;

import org.reservationapplication.model.AvailabilityStatus;
import org.reservationapplication.model.CoworkingSpace;

import java.util.ArrayList;
import java.util.List;


public class CoworkingSpaceServiceImpl implements CoworkingSpaceService {
    private List<CoworkingSpace> generalCoworkingSpace;

    public CoworkingSpaceServiceImpl() {
        this.generalCoworkingSpace = new ArrayList<>();
    }

    public List<CoworkingSpace> getGeneralCoworkingSpace() {
        return generalCoworkingSpace;
    }

    public void addCoworkingSpace(CoworkingSpace coworkingSpace) {
        generalCoworkingSpace.add(coworkingSpace);
    }

    public void removeCoworkingSpace(long id) {
        generalCoworkingSpace.removeIf(space -> space.getCoworkingSpaceID() == id);
    }

    public void printGeneralCoworkingSpace() {
        for (CoworkingSpace coworkingSpace : generalCoworkingSpace) {
            System.out.println(coworkingSpace);
        }
    }

    public List<CoworkingSpace> loadAvailableCoworkingSpace() {
        List<CoworkingSpace> availableCoworkingSpaceList = new ArrayList<>();
        for (CoworkingSpace coworkingSpace : generalCoworkingSpace) {

            if (coworkingSpace.getAvailabilityStatus() == AvailabilityStatus.AVAILABLE) {
                availableCoworkingSpaceList.add(coworkingSpace);
            }

        }
        return availableCoworkingSpaceList;
    }
}
