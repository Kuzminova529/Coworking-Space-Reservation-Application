package org.reservationapplication.domain.repository.SpringDataJPARepos;

import jakarta.transaction.Transactional;
import org.reservationapplication.domain.model.Reservation;
import org.reservationapplication.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositorySpring extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> getUserById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isActive = false WHERE u.id = :id")
    void updateStatus(@Param("id") Long id);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);
}