package org.reservationapplication.repository.hibernateRepos;

import jakarta.persistence.PersistenceException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.reservationapplication.HibernateUtil;
import org.reservationapplication.Loggers;
import org.reservationapplication.model.CoworkingSpace;
import org.reservationapplication.repository.EntityRepository;
import org.w3c.dom.Entity;

import java.sql.SQLException;
import java.util.List;

public class CoworkingSpaceRepositoryJPA implements EntityRepository<CoworkingSpace, Long> {

    @Override
    public void save(List<CoworkingSpace> coworkingSpaces) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            for (CoworkingSpace coworkingSpace : coworkingSpaces) {
                session.persist(coworkingSpace);
            }
            transaction.commit();
        } catch (ConstraintViolationException e) {
            if (transaction != null) transaction.rollback();
            Loggers.USER_LOGGER.error("Something went wrong, try again later");
            Loggers.TECHNICAL_LOGGER.error("Database constraint error: " + e.getMessage());
        } catch (PersistenceException e) {
            if (transaction != null) transaction.rollback();
            Loggers.USER_LOGGER.error("Something went wrong, try again later");
            Loggers.TECHNICAL_LOGGER.error("Error working with JPA: " + e.getMessage());
        }
    }

    @Override
    public List<CoworkingSpace> read() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM CoworkingSpace", CoworkingSpace.class).list();
        }
    }

    @Override
    public void create(CoworkingSpace coworkingSpace) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(coworkingSpace);
            transaction.commit();
        } catch (ConstraintViolationException e) {
            if (transaction != null) transaction.rollback();
            Loggers.USER_LOGGER.error("Something went wrong, try again later");
            Loggers.TECHNICAL_LOGGER.error("Database constraint error: " + e.getMessage());
        } catch (PersistenceException e) {
            if (transaction != null) transaction.rollback();
            Loggers.USER_LOGGER.error("Something went wrong, try again later");
            Loggers.TECHNICAL_LOGGER.error("Error working with JPA: " + e.getMessage());
        }
    }

    @Override
    public void deleteByID(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            CoworkingSpace coworkingSpace = session.get(CoworkingSpace.class, id);

            if (coworkingSpace != null) {
                session.delete(coworkingSpace);
                transaction.commit();
            }

        } catch (ConstraintViolationException e) {
            if (transaction != null) transaction.rollback();
            Loggers.TECHNICAL_LOGGER.error("Constraint violation error while deleting CoworkingSpace with ID " + id, e);
            Loggers.USER_LOGGER.error("Something went wrong, try again later");
        } catch (PersistenceException e) {
            if (transaction != null) transaction.rollback();
            Loggers.TECHNICAL_LOGGER.error("Persistence error occurred while deleting CoworkingSpace with ID " + id, e);
            Loggers.USER_LOGGER.error("Something went wrong, try again later");
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            Loggers.TECHNICAL_LOGGER.error("Unexpected error occurred while deleting CoworkingSpace with ID " + id, e);
            Loggers.USER_LOGGER.error("Something went wrong, try again later");
        }
    }

    @Override
    public void deleteAll() {

    }
}