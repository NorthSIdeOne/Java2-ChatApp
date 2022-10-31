package client.services;

import client.listeners.DispacherService;
import lib.dto.UserDTO;
import lib.service.AuthentificationService;
import lib.service.ChatService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Objects;

public class AuthentificationController {

    private final AuthentificationService authentificationService;

    private AuthentificationController(){
        try {
            Registry registry = LocateRegistry.getRegistry("localhost",4545);
            authentificationService = (AuthentificationService) registry.lookup("authenticationService");
        } catch (RemoteException | NotBoundException e) {
            throw  new RuntimeException(e);
        }
    }

    public static AuthentificationController getInstance() {
        return AuthentificationController.SingletonHolder.INSTANCE;
    }

    private final static class SingletonHolder{
        public static final AuthentificationController INSTANCE = new AuthentificationController();
    }



    public UserDTO login(UserDTO user) throws RemoteException {
        UserDTO userResult = authentificationService.login(user);
        if(userResult.getUsername() != null && !userResult.getUsername().isEmpty()){
            return userResult;
        }
        return null;
    }

    public boolean singUp(UserDTO user) throws RemoteException {
        return authentificationService.singUp(user);
    }

}
