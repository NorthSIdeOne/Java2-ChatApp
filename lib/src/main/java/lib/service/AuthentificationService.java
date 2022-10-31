package lib.service;

import lib.dto.UserDTO;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuthentificationService extends Remote {

    UserDTO login(final UserDTO user)throws RemoteException ;
    boolean singUp(final UserDTO user)throws RemoteException;

}
