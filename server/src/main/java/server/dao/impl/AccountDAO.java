package server.dao.impl;

import server.dao.DAO;
import server.dao.DAOMechanism;
import server.model.Account;
import server.model.Chat;


import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class AccountDAO extends DAOMechanism implements DAO<Account> {

    public AccountDAO(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public void save(final Account account) {
        executeInsideTransaction(entityManager -> entityManager.persist(account));
    }

    @Override
    public Account find(int id) {
        return  entityManager.find(Account.class, id);
    }

    @Override
    public void remove(final Account account) {
        executeInsideTransaction(entityManager -> entityManager.remove(account));
    }

    public Optional<Account> findByUsername(String username){
    TypedQuery<Account> query = entityManager.createNamedQuery("Account.findByUsername" , Account.class);

    query.setParameter("username", username);
    return query.getResultStream().findFirst();

    }

    public boolean checkIfChatExistBetweenUsers(String sender, String receiver){

        final List<Account> accounts = findAccountsForTwoUsers(sender,receiver);
        return checkIfExistSameChat(accounts);
    }

    public List<Account> findAccountsForTwoUsers(final String sender, final String receiver){

        TypedQuery<Account> query = entityManager.createQuery("SELECT a FROM Account a where a.username = :sender or a.username = :receiver",Account.class);

        query.setParameter("sender", sender);
        query.setParameter("receiver", receiver);

        final List<Account> accounts = new ArrayList<>();
        query.getResultStream().limit(2).forEach(accounts::add);

        return accounts;
    }

    public boolean checkIfExistSameChat(final List<Account> accounts){

        if(accounts.size() == 2) {
            final List<Integer> chatIdForReceiver = new ArrayList<>();
            accounts.get(1).getChats()
                    .stream()
                    .map(Chat::getChatId)
                    .forEach(chatIdForReceiver::add);

            return accounts.get(0).getChats()
                    .stream()
                    .anyMatch(chat -> chatIdForReceiver.contains(chat.getChatId()));
        }

        return false;
    }


    public Optional<String> findByUserId(final int userId){
        TypedQuery<String > query = entityManager.createNamedQuery("Account.findByUserId",String.class);
        query.setParameter("userId",userId);

        return  query.getResultStream().findFirst();

    }





}
