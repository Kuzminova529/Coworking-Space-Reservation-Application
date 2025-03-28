package org.reservationapplication.service;

import org.reservationapplication.domain.exeption.BusinessException;
import org.reservationapplication.domain.exeption.DatabaseException;
import org.reservationapplication.domain.model.User;
import org.reservationapplication.domain.repository.EntityRepository;
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

    public User createUser(User user) {
        userRepository.create(user);
        return user;
    }

    public User getUserByID(Long id) {
        try {
            Optional<User> optionalUser = userRepository.getById(id);
            if (optionalUser.isPresent()) {
                return optionalUser.get();
            } else {
                throw new BusinessException("Failed to find user by id");
            }
        } catch (DatabaseException e){
            throw new BusinessException("Failed to find user by id");
        }
    }

    public List<User> getAllUsers() {
        return userRepository.read();
    }
}
