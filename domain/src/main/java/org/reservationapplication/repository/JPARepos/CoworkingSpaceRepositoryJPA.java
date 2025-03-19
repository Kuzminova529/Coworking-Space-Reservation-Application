package org.reservationapplication.repository.JPARepos;

import jakarta.persistence.*;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.reservationapplication.Loggers;
import org.reservationapplication.model.CoworkingSpace;
import org.reservationapplication.repository.EntityRepository;

import java.util.*;

public class CoworkingSpaceRepositoryJPA implements EntityRepository<CoworkingSpace, Long> {

    private EntityManagerFactory emf;

    public CoworkingSpaceRepositoryJPA() {
        this.emf = Persistence.createEntityManagerFactory("my-persistence-unit");
    }

    @Override
    public void save(List<CoworkingSpace> coworkingSpaces) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try (entityManager)  {
            transaction.begin();

            for (CoworkingSpace space : coworkingSpaces) {
                entityManager.merge(space);
            }

            transaction.commit();
        } catch (JDBCException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            Loggers.TECHNICAL_LOGGER.error("JDBC error occurred: {}", e.getMessage());
            Loggers.USER_LOGGER.error("Something went wrong, please try again later.");
        } catch (OptimisticLockException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            Loggers.TECHNICAL_LOGGER.error("Optimistic lock failure: {}", e.getMessage());
            Loggers.USER_LOGGER.error("Something went wrong, please try again later.");
        } catch (PersistenceException e) { // PersistenceException вместо HibernateException
            if (transaction.isActive()) {
                transaction.rollback();
            }
            Loggers.TECHNICAL_LOGGER.error("Persistence error occurred: {}", e.getMessage());
            Loggers.USER_LOGGER.error("Something went wrong, please try again later.");
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            Loggers.TECHNICAL_LOGGER.error("Unexpected error occurred: {}", e.getMessage());
            Loggers.USER_LOGGER.error("Something went wrong, please try again later.");
        }
    }

    @Override
    public List<CoworkingSpace> read() {
        EntityManager entityManager = emf.createEntityManager();
        try (entityManager){
            TypedQuery<CoworkingSpace> query = entityManager.createQuery("SELECT c FROM CoworkingSpace c", CoworkingSpace.class);
            return query.getResultList();
        } catch (Exception e) {
            Loggers.TECHNICAL_LOGGER.error("Unexpected error occurred: {}", e.getMessage());
            Loggers.USER_LOGGER.error("Something went wrong, please try again later.");
            return new ArrayList<>();
        }
    }


    @Override
    public void create(CoworkingSpace coworkingSpace) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try (entityManager){
            transaction.begin();
            entityManager.persist(coworkingSpace);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            Loggers.TECHNICAL_LOGGER.error("Unexpected error occurred while persisting coworking space: {}", e.getMessage());
            Loggers.USER_LOGGER.error("Something went wrong while saving your coworking space. Please try again later.");
        }
    }

    public void makeUnavailable(Long id) {
        EntityManager entityManager = emf.createEntityManager();
        try (entityManager) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                String jpql = "UPDATE CoworkingSpace r SET r.isAvailable = false WHERE r.id = :id";
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


    @Override
    public Optional<CoworkingSpace> getById(Long id) {
        EntityManager entityManager = emf.createEntityManager();
        try(entityManager) {
            CoworkingSpace coworkingSpace = entityManager.createQuery(
                            "FROM CoworkingSpace c WHERE c.id = :id", CoworkingSpace.class)
                    .setParameter("id", id)
                    .getSingleResult();

            return Optional.ofNullable(coworkingSpace);
        } catch (Exception e) {
            Loggers.TECHNICAL_LOGGER.error("Unexpected error occurred while opening Hibernate session: {}", e.getMessage());
            Loggers.USER_LOGGER.error("Something went wrong. Please try again later.");
            return Optional.empty();
        }
    }
}