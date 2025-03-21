package org.reservationapplication.repository.JPARepos;

import jakarta.persistence.*;
import org.hibernate.JDBCException;
import org.reservationapplication.Loggers;
import org.reservationapplication.model.User;
import org.reservationapplication.repository.EntityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryJPA implements EntityRepository<User, Long> {
    private EntityManagerFactory emf;

    public UserRepositoryJPA() {
        this.emf = Persistence.createEntityManagerFactory("my-persistence-unit");
    }

    @Override
    public void save(List<User> users) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            for (User user : users) {
                entityManager.merge(user);
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
    public List<User> read() {
        EntityManager entityManager = emf.createEntityManager();
        try {
            TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u", User.class);
            return query.getResultList();
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

    @Override
    public void create(User user) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            Loggers.TECHNICAL_LOGGER.error("Unexpected error occurred while persisting user: {}", e.getMessage());
            Loggers.USER_LOGGER.error("Something went wrong while saving your user. Please try again later.");
        }
        finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    @Override
    public Optional<User> getById(Long id) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            User user = entityManager.createQuery(
                            "FROM User u WHERE u.id = :id", User.class)
                    .setParameter("id", id)
                    .getSingleResult();

            return Optional.ofNullable(user);
        } catch (Exception e) {
            Loggers.TECHNICAL_LOGGER.error("Unexpected error occurred while opening Hibernate session: {}", e.getMessage());
            Loggers.USER_LOGGER.error("Something went wrong. Please try again later.");
            return Optional.empty();
        }
        finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
}
