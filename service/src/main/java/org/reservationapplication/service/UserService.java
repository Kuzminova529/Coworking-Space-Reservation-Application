package org.reservationapplication.service;

import org.reservationapplication.domain.exeption.BusinessException;
import org.reservationapplication.domain.exeption.DatabaseException;
import org.reservationapplication.domain.model.User;
import org.reservationapplication.domain.model.UserRole;
import org.reservationapplication.domain.repository.SpringDataJPARepos.UserRepositorySpring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepositorySpring userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(@Qualifier("userRepositorySpring") UserRepositorySpring userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerNewUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User with such name already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null) {
            user.setRole(UserRole.ROLE_CUSTOMER);
        }

        user.setActive(true);

        userRepository.save(user);
    }

    public User getUserByID(Long id) {
        try {
            Optional<User> optionalUser = userRepository.getUserById(id);
            if (optionalUser.isPresent()) {
                return optionalUser.get();
            } else {
                throw new BusinessException("Failed to find user by id");
            }
        } catch (DatabaseException e){
            throw new BusinessException("Failed to find user by id");
        }
    }

    public User getUserByUsername(String username) {
        try {
            Optional<User> optionalUser = userRepository.findByUsername(username);
            if (optionalUser.isPresent()) {
                return optionalUser.get();
            } else {
                throw new BusinessException("Failed to find user by username");
            }
        } catch (DatabaseException e){
            throw new BusinessException("Failed to find user by username");
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
