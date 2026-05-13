package com.hotel.wildcat_hotel.repository;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.hotel.wildcat_hotel.core.Repository;

public abstract class AbstractHibernateRepository<T> implements Repository<T> {

    protected final SessionFactory sessionFactory;
    private final Class<T> entityClass;

    protected AbstractHibernateRepository(SessionFactory sessionFactory, Class<T> entityClass) {
        this.sessionFactory = sessionFactory;
        this.entityClass = entityClass;
    }

    protected SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Override
    public List<T> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from " + entityClass.getSimpleName(), entityClass).list();
        }
    }

    @Override
    public Optional<T> findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(entityClass, id));
        }
    }

    @Override
    public T save(T entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
            return entity;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T update(T entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            T merged = (T) session.merge(entity);
            session.getTransaction().commit();
            return merged;
        }
    }

    @Override
    public boolean deleteById(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            int deleted = session.createMutationQuery(
                            "delete from " + entityClass.getSimpleName() + " where entityId = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            session.getTransaction().commit();
            return deleted > 0;
        }
    }
}