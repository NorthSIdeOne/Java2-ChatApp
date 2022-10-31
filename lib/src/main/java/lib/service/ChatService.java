package lib.service;

import lib.dto.ChatDTO;
import lib.dto.MessageDTO;
import lib.dto.UserDTO;
import lib.event.ChatEvent;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChatService extends Remote {

     ChatDTO createChat(final UserDTO sender, final UserDTO receiver) throws RemoteException;
     void sendMessage(final MessageDTO message) throws RemoteException;
     List<ChatDTO> getAllChats(final UserDTO user)throws RemoteException;
     List<ChatEvent> getEvents(String username) throws RemoteException;


}
