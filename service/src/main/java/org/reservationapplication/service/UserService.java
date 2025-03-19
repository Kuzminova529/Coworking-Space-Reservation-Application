package org.reservationapplication.service;

import org.reservationapplication.model.Admin;
import org.reservationapplication.model.User;
import org.reservationapplication.repository.JPARepos.UserRepositoryJPA;

import java.util.List;
import java.util.Optional;

public class UserService {
    private UserRepositoryJPA userRepository;

    public UserService(UserRepositoryJPA userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(User user) {
        userRepository.create(user);
    }

    public Optional<User> findUserByID(Long id) {
        return userRepository.getById(id);
    }

    public List<User> findAllUsers() {
        return userRepository.read();
    }
}
