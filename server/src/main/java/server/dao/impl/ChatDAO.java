package server.dao.impl;

import server.dao.DAO;
import server.dao.DAOMechanism;
import server.model.Chat;

import javax.persistence.EntityManager;

public class ChatDAO extends DAOMechanism implements DAO<Chat> {
    /**
     * Initialize the entity manager
     *
     * @param entityManager
     */
    public ChatDAO(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public void save(Chat chat) {
        executeInsideTransaction(entityManager -> entityManager.persist(chat));
    }

    @Override
    public Chat find(int id) {
        return  entityManager.find(Chat.class, id);
    }

    @Override
    public void remove(Chat chat) {
        executeInsideTransaction(entityManager -> entityManager.remove(chat));
    }
}
