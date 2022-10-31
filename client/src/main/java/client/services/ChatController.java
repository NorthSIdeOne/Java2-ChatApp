package client.services;

import lib.dto.ChatDTO;
import lib.dto.MessageDTO;
import lib.dto.UserDTO;
import lib.event.ChatEvent;
import lib.service.ChatService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class ChatController {

    private ChatService chatService;

    public static ChatController getInstance() {
        return SingletonHolder.INSTANCE;
    }


    private final static class SingletonHolder{

        public static final ChatController INSTANCE = new ChatController();

    }

    private ChatController(){
        try {
            Registry registry = LocateRegistry.getRegistry("localhost",4545);
            chatService = (ChatService) registry.lookup("chatService");
        } catch (RemoteException | NotBoundException e) {
            throw  new RuntimeException(e);
        }
    }

    public List<ChatEvent> getEvents(String username) {
        try {
            return chatService.getEvents(username);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ChatDTO> getAllChatsForUser(final String username) throws RemoteException {
        return chatService.getAllChats(new UserDTO(username));
    }

    public ChatDTO createNewChat(UserDTO sender, UserDTO receiver) throws RemoteException {
        return chatService.createChat(sender,receiver);
    }

    public void sendMessage(final MessageDTO message) throws RemoteException {
         chatService.sendMessage(message);
    }
    public void refresh() throws RemoteException {
        chatService.refresh();
    }
}
