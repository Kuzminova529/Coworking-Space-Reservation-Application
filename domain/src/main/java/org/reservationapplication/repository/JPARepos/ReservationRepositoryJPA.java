package org.reservationapplication.repository.JPARepos;

import jakarta.persistence.*;
import org.hibernate.JDBCException;
import org.reservationapplication.Loggers;
import org.reservationapplication.model.Reservation;
import org.reservationapplication.repository.EntityRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReservationRepositoryJPA implements EntityRepository<Reservation, Long> {

    private EntityManagerFactory emf;

    public ReservationRepositoryJPA() {
        this.emf = Persistence.createEntityManagerFactory("my-persistence-unit");
    }

    @Override
    public void save(List<Reservation> reservations) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            for (Reservation reservation : reservations) {
                entityManager.merge(reservation);
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
                            "FROM Reservation c WHERE c.id = :id", Reservation.class)
                    .setParameter("id", id)
                    .getSingleResult();

            return Optional.ofNullable(reservation);
        } catch (Exception e) {
            Loggers.TECHNICAL_LOGGER.error("Unexpected error occurred while opening Hibernate session: {}", e.getMessage());
            Loggers.USER_LOGGER.error("Something went wrong. Please try again later.");
            return Optional.empty();
        }
    }

    public void updateReservationStatus(Long userID) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            String jpql = "UPDATE Reservation r SET r.isActive = false WHERE r.userID = :userID";
            entityManager.createQuery(jpql)
                    .setParameter("userID", userID)
                    .executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            Loggers.TECHNICAL_LOGGER.error("Unexpected error occurred while updating reservations: {}", e.getMessage());
            Loggers.USER_LOGGER.error("Something went wrong while updating your reservations. Please try again later.");
        }
        finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
}

