package com.hotel.wildcat_hotel.repository;

import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.hotel.wildcat_hotel.project.User;

public class UserRepository extends AbstractHibernateRepository<User> {

    public UserRepository(SessionFactory sessionFactory) {
        super(sessionFactory, User.class);
    }

    public Optional<User> authenticate(String username, String password) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            User user = session.createQuery(
                            "from User u where u.username = :username and u.password = :password",
                            User.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .uniqueResult();
            session.getTransaction().commit();
            return Optional.ofNullable(user);
        }
    }
}