package server.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerInstance {

    private EntityManager entityManager;

    public static  EntityManager getEntityManager(){
        return SingletonHolder.instance.entityManager;
    }

    private static class SingletonHolder{
        private static EntityManagerInstance instance = new EntityManagerInstance();
    }

    private EntityManagerInstance(){
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("chatUnit");
        this.entityManager = entityManagerFactory.createEntityManager();
    }

}
