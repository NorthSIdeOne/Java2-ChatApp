package server.dao.impl;

import server.dao.DAO;
import server.dao.DAOMechanism;
import server.model.Account;
import server.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO extends DAOMechanism implements DAO<User> {

    /**
     * Initialize the entity manager
     */
    public UserDAO(final EntityManager entityManager){
        super(entityManager);
      //  entityManager.getEntityManagerFactory().getCache().evictAll();
    }

    /**
     * Save an instance of user in the database
     * @param user object
     */
    @Override
    public void save(final User user) {
        executeInsideTransaction(entityManager -> entityManager.persist(user));
    }

    @Override
    public User find(int id) {
        return  entityManager.find(User.class, id);
    }

    @Override
    public void remove(final User user) {
     executeInsideTransaction(entityManager -> entityManager.remove(user));
    }

    public Optional<User> findByEmail(final String email){
        TypedQuery<User> query = entityManager.createNamedQuery("User.findByEmail", User.class);
        query.setParameter("email", email);

        return query.getResultStream().findFirst();
    }


}
