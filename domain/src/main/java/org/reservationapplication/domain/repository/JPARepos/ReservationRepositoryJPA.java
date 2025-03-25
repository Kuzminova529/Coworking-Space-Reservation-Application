package org.reservationapplication.domain.repository.JPARepos;

import jakarta.persistence.*;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.reservationapplication.domain.repository.ReservationRepository;
import org.reservationapplication.logger.Loggers;
import org.reservationapplication.domain.model.Reservation;
import org.reservationapplication.domain.repository.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("jpaReservationRepository")
public class ReservationRepositoryJPA implements ReservationRepository {

    private final EntityManagerFactory emf;

    @Autowired
    public ReservationRepositoryJPA(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void save(List<Reservation> reservations) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            for (Reservation reservation : reservations) {
                entityManager.persist(reservation);
            }

            transaction.commit();
        } catch (OptimisticLockException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            Loggers.TECHNICAL_LOGGER.error("Optimistic lock failure: {}", e.getMessage());
            Loggers.USER_LOGGER.error("Something went wrong, please try again later.");
        } catch (JDBCException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            Loggers.TECHNICAL_LOGGER.error("JDBC error occurred: {}", e.getMessage());
            Loggers.USER_LOGGER.error("Something went wrong, please try again later.");
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            Loggers.TECHNICAL_LOGGER.error("Unexpected error occurred: {}", e.getMessage());
            Loggers.USER_LOGGER.error("Something went wrong, please try again later.");
        }
        finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    @Override
    public List<Reservation> read() {
        EntityManager entityManager = emf.createEntityManager();
        try {
            return entityManager.createQuery("from Reservation", Reservation.class)
                    .getResultList();
        } catch (Exception e) {
            Loggers.TECHNICAL_LOGGER.error("Unexpected error occurred: {}", e.getMessage());
            Loggers.USER_LOGGER.error("Something went wrong, please try again later.");
            return new ArrayList<>();
        }
        finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    public List<Reservation> readPersonalReservations(Long userID) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            LocalDateTime now = LocalDateTime.now();

            transaction.begin();
            entityManager.createQuery("UPDATE Reservation r SET r.isActive = false WHERE r.startDateTime < :now AND r.userID = :userID")
                    .setParameter("now", now)
                    .setParameter("userID", userID)
                    .executeUpdate();

            List<Reservation> reservations = entityManager.createQuery(
                            "FROM Reservation r WHERE r.userID = :userID AND r.isActive = true", Reservation.class)
                    .setParameter("userID", userID)
                    .getResultList();

            transaction.commit();
            return reservations;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            Loggers.TECHNICAL_LOGGER.error("Unexpected error occurred: {}", e.getMessage());
            Loggers.USER_LOGGER.error("Something went wrong, please try again later.");
            return new ArrayList<>();
        }
        finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    @Override
    public void create(Reservation reservation) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(reservation);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            Loggers.TECHNICAL_LOGGER.error("Unexpected error occurred while persisting reservation: {}", e.getMessage());
            Loggers.USER_LOGGER.error("Something went wrong while saving your reservation. Please try again later.");
        }
        finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    @Override
    public Optional<Reservation> getById(Long id) {
        EntityManager entityManager = emf.createEntityManager();
        try(entityManager) {
            Reservation reservation = entityManager.createQuery(
                            "FROM Reservation c WHERE c.id = :id AND c.isActive = true", Reservation.class)
                    .setParameter("id", id)
                    .getSingleResult();

            return Optional.ofNullable(reservation);
        } catch (Exception e) {
            Loggers.TECHNICAL_LOGGER.error("Unexpected error occurred while opening Hibernate session: {}", e.getMessage());
            Loggers.USER_LOGGER.error("Something went wrong. Please try again later.");
            return Optional.empty();
        }
    }

    @Override
    public void updateStatus(Long id) {
        EntityManager entityManager = emf.createEntityManager();
        try (entityManager) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                String jpql = "UPDATE Reservation r SET r.isActive = false WHERE r.id = :id";
                entityManager.createQuery(jpql)
                        .setParameter("id", id)
                        .executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                Loggers.TECHNICAL_LOGGER.error("Unexpected error occurred while updating reservation: {}", e.getMessage());
                Loggers.USER_LOGGER.error("Something went wrong while updating your reservation. Please try again later.");
            }
        } catch (HibernateException e) {
            Loggers.TECHNICAL_LOGGER.error("Error opening Hibernate session: {}", e.getMessage());
            Loggers.USER_LOGGER.error("Something went wrong while connecting to the database. Please try again later.");
        }
    }
}

