package server.service;

import junit.framework.TestCase;
import lib.dto.ChatDTO;
import lib.dto.MessageDTO;
import lib.dto.UserDTO;
import lib.service.ChatService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import server.dao.impl.AccountDAO;
import server.model.Account;
import server.model.Chat;
import server.model.User;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Optional;


class ChatServiceImplTest extends TestCase {

    final EntityManager em = Persistence.createEntityManagerFactory("chatUnit").createEntityManager();
    Registry registry = LocateRegistry.getRegistry("localhost",4545);
    ChatService service1 = (ChatService) registry.lookup("chatService");
    ChatDTO chat;

    ChatServiceImplTest() throws RemoteException, NotBoundException {
//        try {
//            deleteAllData();
//        }catch (Exception e){
//
//        }
      //  initDataset();

    }

    public void deleteAllData(){
        em.getTransaction().begin();
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        em.createNativeQuery("drop TABLE if EXISTS account").executeUpdate();
        em.createNativeQuery("drop TABLE if EXISTS user").executeUpdate();
        em.createNativeQuery("drop TABLE if EXISTS chat").executeUpdate();
        em.createNativeQuery("drop TABLE if EXISTS message").executeUpdate();
        em.createNativeQuery("drop TABLE if EXISTS chats_mapping").executeUpdate();
        em.createNativeQuery("drop TABLE if EXISTS hibernate_sequence").executeUpdate();
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
        em.getTransaction().commit();
    }

    public void initDataset(){

        User user1 = new User();
        user1.setEmail("stefan3@gmail.com");
        user1.setPassword("test");

        Account account1 = new Account();
        account1.setUser(user1);
        account1.setUsername("Stefan");

        User user2 = new User();
        user2.setEmail("bogdan@gmail.com");
        user2.setPassword("test");

        Account account2 = new Account();
        account2.setUser(user2);
        account2.setUsername("Bogdan");


        AccountDAO accountDAO = new AccountDAO(em);
        accountDAO.save(account1);
        accountDAO.save(account2);
    }

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();



    }

    @Test
    void findUsers() {

    }

    @Test
    void createChat() throws RemoteException {
        UserDTO user1 = new UserDTO("Stefan","","");
        UserDTO user2 = new UserDTO("Bogdan","","");
        this.chat = service1.createChat(user1,user2);
        assertNotNull("Create new chat failed: New chat for 2 users couldn't be created"
                ,this.chat);
        assertNull("Create chat failed: two chats for the same users",
                service1.createChat(user1,user2));

    }

//    @Test
//    void sendMessage() throws RemoteException {
//        AccountDAO accountDAO = new AccountDAO(em);
//        Optional<Account> account = accountDAO.findByUsername("Stefan");
//        assertTrue("The user Stefan doesn't exist " ,account.isPresent());
//        Account testAccount = account.get();
//        MessageDTO newMessage = new MessageDTO();
//        newMessage.setChatId(7);
//        newMessage.setSender("Stefan");
//        newMessage.setReceiver("Bogdan");
//        newMessage.setMessage("This a test");
//        service1.sendMessage(newMessage);
//        service1.notifyAllUsers();
//    }
//
//
//    @Test
//    void getAllChats() throws RemoteException {
//        List<ChatDTO> chatList = service1.getAllChats(new UserDTO("Stefan", "", ""));
//        assertTrue(!chatList.isEmpty());
//    }

}