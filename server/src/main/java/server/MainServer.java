package server;

import lib.dto.UserDTO;
import server.dao.impl.AccountDAO;
import server.model.Account;
import server.model.Chat;
import server.model.Message;
import server.model.User;
import server.service.AuthentificationServiceImpl;
import server.service.ChatServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class MainServer {

    public static void main(String[] args) throws RemoteException {

        var emf = Persistence.createEntityManagerFactory("chatUnit");
        EntityManager em = emf.createEntityManager();
        Registry registry;
        registry = LocateRegistry.createRegistry(4545);
        registry.rebind("chatService", new ChatServiceImpl());
        registry.rebind("authenticationService", new AuthentificationServiceImpl());
//
//        User user1 = new User();
//        user1.setEmail("stefan3@gmail.com");
//        user1.setPassword("test");
//
//        Account account1 = new Account();
//        account1.setUser(user1);
//        account1.setUsername("Stefan");
//
//        User user2 = new User();
//        user2.setEmail("bogdan@gmail.com");
//        user2.setPassword("test");
//
//        Account account2 = new Account();
//        account2.setUser(user2);
//        account2.setUsername("Bogdan");
//
//
//        AccountDAO accountDAO = new AccountDAO(em);
//        accountDAO.save(account1);
//        accountDAO.save(account2);


//        ChatServiceImpl service1 = new ChatServiceImpl();
//        UserDTO user1 = new UserDTO("Stefan","","");
//        UserDTO user2 = new UserDTO("Bogdan","","");
//        service1.createChat(user1,user2);
//        em.getTransaction().begin();
//        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
//        em.createNativeQuery("truncate table account").executeUpdate();
//        em.createNativeQuery("truncate table user").executeUpdate();
//        em.createNativeQuery("truncate table chat").executeUpdate();
//        em.createNativeQuery("truncate table message").executeUpdate();
//        em.createNativeQuery("truncate table chats_mapping").executeUpdate();
//        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
//        em.getTransaction().commit();
        //TypedQuery<Account> query = em.createQuery("SELECT u FROM Account u", Account.class);

//        em.getTransaction().begin();
//      //  em.persist(chat1);
//        em.persist(account);
//      //  em.persist(user);
//        em.getTransaction().commit();

        em.close();
        emf.close();


    }
}
