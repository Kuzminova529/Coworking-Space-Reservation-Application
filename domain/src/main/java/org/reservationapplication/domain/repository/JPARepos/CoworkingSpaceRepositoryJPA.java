package org.reservationapplication.domain.repository.JPARepos;

import jakarta.persistence.*;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.reservationapplication.domain.exeption.DatabaseErrorCode;
import org.reservationapplication.domain.exeption.DatabaseException;
import org.reservationapplication.domain.repository.CoworkingSpaceRepository;
import org.reservationapplication.logger.Loggers;
import org.reservationapplication.domain.model.CoworkingSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CoworkingSpaceRepositoryJPA implements CoworkingSpaceRepository {

    private EntityManagerFactory emf;

    @Autowired
    public CoworkingSpaceRepositoryJPA(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void saveAll(List<CoworkingSpace> coworkingSpaces) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try (entityManager)  {
            transaction.begin();

            for (CoworkingSpace space : coworkingSpaces) {
                entityManager.persist(space);
            }

            transaction.commit();
        } catch (JDBCException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            Loggers.TECHNICAL_LOGGER.error("JDBC error occurred: {}", e.getMessage());
        } catch (OptimisticLockException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            Loggers.TECHNICAL_LOGGER.error("Optimistic lock failure: {}", e.getMessage());
        } catch (PersistenceException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            Loggers.TECHNICAL_LOGGER.error("Persistence error occurred: {}", e.getMessage());
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            Loggers.TECHNICAL_LOGGER.error("Unexpected error occurred: {}", e.getMessage());
        }
    }

    @Override
    public List<CoworkingSpace> findAll() {
        EntityManager entityManager = emf.createEntityManager();
        try (entityManager){
            TypedQuery<CoworkingSpace> query = entityManager.createQuery("SELECT c FROM CoworkingSpace c", CoworkingSpace.class);
            return query.getResultList();
        } catch (Exception e) {
            Loggers.TECHNICAL_LOGGER.error("Unexpected error occurred: {}", e.getMessage());
            return new ArrayList<>();
        }
    }


    @Override
    public CoworkingSpace save(CoworkingSpace coworkingSpace) {
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
        }
        return coworkingSpace;
    }

    @Override
    public void updateStatus(Long id) {
        EntityManager entityManager = emf.createEntityManager();
        try (entityManager) {
            EntityTransaction transaction = entityManager.getTransaction();
            try {
                transaction.begin();

                CoworkingSpace coworkingSpace = entityManager.find(CoworkingSpace.class, id);
                if (coworkingSpace == null) {
                    throw new DatabaseException("Coworking space with ID " + id + " not found", DatabaseErrorCode.DATA_NOT_FOUND);
                }

                coworkingSpace.setActive(false);

                transaction.commit();
            } catch (Exception e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                Loggers.TECHNICAL_LOGGER.error("Unexpected error occurred while updating reservation: {}", e.getMessage());
            }
        } catch (HibernateException e) {
            Loggers.TECHNICAL_LOGGER.error("Error opening Hibernate session: {}", e.getMessage());
        }
    }

    @Override
    public Optional<CoworkingSpace> findByIdCustom(Long id) {
        EntityManager entityManager = emf.createEntityManager();
        try(entityManager) {
            CoworkingSpace coworkingSpace = entityManager.createQuery(
                            "SELECT c FROM CoworkingSpace c WHERE c.id = :id and c.isActive = true", CoworkingSpace.class)
                    .setParameter("id", id)
                    .getSingleResult();

            return Optional.ofNullable(coworkingSpace);
        } catch (Exception e) {
            Loggers.TECHNICAL_LOGGER.error("Unexpected error occurred while opening Hibernate session: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<CoworkingSpace> getCoworkingSpaceWithReservations(Long coworkingSpaceId) {
        try (EntityManager em = emf.createEntityManager()) {
            return Optional.ofNullable(em.createQuery(
                    "SELECT c FROM CoworkingSpace c LEFT JOIN FETCH c.reservations WHERE c.id = :id",
                    CoworkingSpace.class
            ).setParameter("id", coworkingSpaceId).getSingleResult());
        } catch (Exception e) {
            Loggers.TECHNICAL_LOGGER.error("Unexpected error occurred while opening Hibernate session: {}", e.getMessage());
            return Optional.empty();
        }
    }
}