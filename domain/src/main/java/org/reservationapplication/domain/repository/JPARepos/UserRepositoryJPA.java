package org.reservationapplication.domain.repository.JPARepos;

import jakarta.persistence.*;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.reservationapplication.logger.Loggers;
import org.reservationapplication.domain.model.User;
import org.reservationapplication.domain.repository.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryJPA implements EntityRepository<User, Long> {
    private EntityManagerFactory emf;

    @Autowired
    public UserRepositoryJPA(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void saveAll(List<User> users) {
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
    public List<User> findAll() {
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
    public void save(User user) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(user);
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
    public Optional<User> getByIdOptional(Long id) {
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

    @Override
    public void updateStatus(Long id) {
        EntityManager entityManager = emf.createEntityManager();
        try (entityManager) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                String jpql = "UPDATE User u SET u.isActive = false WHERE u.id = :id";
                entityManager.createQuery(jpql)
                        .setParameter("id", id)
                        .executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                Loggers.TECHNICAL_LOGGER.error("Unexpected error occurred while updating user: {}", e.getMessage());
                Loggers.USER_LOGGER.error("Something went wrong while updating your reservation. Please try again later.");
            }
        } catch (HibernateException e) {
            Loggers.TECHNICAL_LOGGER.error("Error opening Hibernate session: {}", e.getMessage());
            Loggers.USER_LOGGER.error("Something went wrong while connecting to the database. Please try again later.");
        }
    }
}
