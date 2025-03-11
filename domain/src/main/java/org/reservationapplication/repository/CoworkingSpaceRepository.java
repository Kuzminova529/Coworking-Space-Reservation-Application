package org.reservationapplication.repository;

import org.reservationapplication.exeption.CoworkingSpaceNotFoundException;
import org.reservationapplication.model.CoworkingSpace;


import java.util.List;
import java.util.Optional;

public class CoworkingSpaceRepository implements EntityRepository<CoworkingSpace, Long> {
    private static long nextId = 0L;
    private final WorkspaceDataStorage storage = new WorkspaceDataStorage();

    public static long getNextID() {
        return nextId;
    }

    @Override
    public void save(List<CoworkingSpace> coworkingSpaces) {
        storage.save(coworkingSpaces);
        updateID(coworkingSpaces);
    }

    @Override
    public List<CoworkingSpace> read() {
        List<CoworkingSpace> spaces = storage.load();
        updateID(spaces);
        return spaces;
    }

    @Override
    public void add(CoworkingSpace coworkingSpace) {
        List<CoworkingSpace> spaces = read();
        spaces.add(coworkingSpace);
        save(spaces);
    }

    @Override
    public void deleteByID(Long id) {
        List<CoworkingSpace> spaces = read();
        boolean removed = spaces.removeIf(space -> Long.valueOf(space.getID()).equals(id));

        if (!removed) {
            throw new CoworkingSpaceNotFoundException(id);
        }
        save(spaces);
    }

    public Optional<CoworkingSpace> getById(Long id) {
        return read().stream()
                .filter(space -> Long.valueOf(space.getID()).equals(id))
                .findFirst();
    }

    @Override
    public void deleteAll() {
        save(List.of());
    }

    private void updateID(List<CoworkingSpace> spaces) {
        nextId = spaces.stream().mapToLong(CoworkingSpace::getID).max().orElse(0L) + 1;
    }
}
