package org.service;

import org.domain.model.User;
import org.domain.repository.JPARepos.UserRepositoryJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepositoryJPA userRepository;

    @Autowired
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
