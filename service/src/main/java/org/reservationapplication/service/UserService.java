package org.reservationapplication.service;

import org.reservationapplication.domain.model.User;
import org.reservationapplication.domain.repository.EntityRepository;
import org.reservationapplication.domain.repository.JPARepos.UserRepositoryJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private EntityRepository<User, Long> userRepository;

    @Autowired
    public UserService(@Qualifier("jpaUserRepository") EntityRepository<User, Long> userRepository) {
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
