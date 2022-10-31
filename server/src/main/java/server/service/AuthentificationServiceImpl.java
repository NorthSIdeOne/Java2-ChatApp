package server.service;

import lib.dto.UserDTO;
import lib.service.AuthentificationService;
import server.dao.EntityManagerInstance;
import server.dao.impl.AccountDAO;
import server.dao.impl.UserDAO;
import server.model.Account;
import server.model.User;

import javax.persistence.EntityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;
import java.util.Optional;

public class AuthentificationServiceImpl  extends UnicastRemoteObject  implements AuthentificationService {


    private final EntityManager entityManager;

    public AuthentificationServiceImpl() throws RemoteException {
        this.entityManager = EntityManagerInstance.getEntityManager();
    }

    @Override
    public UserDTO login(final UserDTO user) throws RemoteException{
        UserDAO userDAO = new UserDAO(entityManager);
        UserDTO result = new UserDTO();
        Optional<User> returnedOptionalUser = userDAO.findByEmail(user.getEmail());
       if(returnedOptionalUser.isPresent()
               && Objects.equals(returnedOptionalUser.get().getPassword(), user.getPassword())){
           AccountDAO accountDAO = new AccountDAO(entityManager);
           accountDAO.findByUserId(returnedOptionalUser.get().getUserId())
                   .ifPresent(result::setUsername);
       }

        return result;
    }

    @Override
    public boolean singUp(UserDTO user) throws RemoteException{
        try {
            User newUser = new User();
            newUser.setEmail(user.getEmail());
            newUser.setPassword(user.getPassword());
            Account newAccount = new Account();
            newAccount.setUser(newUser);
            newAccount.setUsername(user.getUsername());
            AccountDAO accountDAO = new AccountDAO(entityManager);
            accountDAO.save(newAccount);
            return true;
        }catch (Exception e){
            return false;
        }

    }
}
