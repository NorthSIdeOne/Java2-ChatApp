package server.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.function.Consumer;

public abstract class DAOMechanism{

    /**
     * The entity manager which is received from a service
     */
    protected final EntityManager entityManager;

    /**
     * Initialize the entity manager
     * @param entityManager
     */
    public DAOMechanism(final EntityManager entityManager){
        this.entityManager = entityManager;
    }

    /**
     * Execute any action inside a transaction
     */
    protected void executeInsideTransaction(final Consumer<EntityManager> action) {
        final EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            action.accept(entityManager);
            transaction.commit();
        }
        catch (RuntimeException e) {
            transaction.rollback();
            throw e;
        }
    }
}
