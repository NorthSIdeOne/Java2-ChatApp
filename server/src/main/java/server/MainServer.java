package server;

import server.service.AuthentificationServiceImpl;
import server.service.ChatServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
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

        em.close();
        emf.close();


    }
}
