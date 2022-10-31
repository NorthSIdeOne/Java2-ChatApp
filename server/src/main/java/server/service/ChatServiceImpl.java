package server.service;

import lib.dto.ChatDTO;
import lib.dto.MessageDTO;
import lib.dto.UserDTO;
import lib.event.ChatEvent;
import lib.service.ChatService;
import server.dao.EntityManagerInstance;
import server.dao.impl.AccountDAO;
import server.dao.impl.ChatDAO;
import server.model.Account;
import server.model.Chat;
import server.model.Message;

import javax.persistence.EntityManager;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.rmi.server.UnicastRemoteObject;

public class ChatServiceImpl extends UnicastRemoteObject implements ChatService {

    private final  EventService eventService;
    private final  EntityManager entityManager;

    public ChatServiceImpl() throws RemoteException {
        entityManager = EntityManagerInstance.getEntityManager();
        eventService = new EventService();
    }

    /**
     * Create a new chat instance for 2 users if they don't have any chat
     * @param sender the user which want to create the chat
     * @param receiver the user which is the receiver
     * @return ChatDTO
     */
    @Override
    public ChatDTO createChat(UserDTO sender, UserDTO receiver) throws RemoteException{
        AccountDAO accountDAO = new AccountDAO(this.entityManager);

        Optional<Account> senderAccount = accountDAO.findByUsername(sender.getUsername());
        Optional<Account> receiverAccount = accountDAO.findByUsername(receiver.getUsername());

        if(senderAccount.isPresent()
                && receiverAccount.isPresent()
                && !accountDAO.checkIfChatExistBetweenUsers(sender.getUsername(), receiver.getUsername())){

            ChatDTO newChat = addNewChatToAccounts(senderAccount.get(), receiverAccount.get());
            eventService.createEventQueue(receiver.getUsername());
            eventService.addNewChatEvent(newChat.getId(),sender.getUsername(),receiver.getUsername());
            return newChat;

        }

        return null;
    }

    /**
     * Send a message to a user
     * @param sentMessage the message to be sent
     */
    @Override
    public void sendMessage(final MessageDTO sentMessage) throws RemoteException{

        final ChatDAO chatDao = new ChatDAO(this.entityManager);
        final Chat chat = chatDao.find(sentMessage.getChatId());
        final Message message = new Message();
        message.setUsername(sentMessage.getSender());
        message.setMessage(sentMessage.getMessage());
        chat.addMessage(message);
        chatDao.save(chat);
        sentMessage.setChatId(chat.getChatId());
        eventService.addNewMessageEvent(sentMessage);
    }


    /**
     * Return a list of chats for a user
     * @param user the user who asked for all the chats
     */
    @Override
    public List<ChatDTO> getAllChats(UserDTO user) throws RemoteException{
        final List<ChatDTO> chatList = new ArrayList<>();
        final AccountDAO accountDAO = new AccountDAO(this.entityManager);
        accountDAO.findByUsername(user.getUsername())
                .orElseGet(Account::new)
                .getChats()
                .forEach((Chat chat) ->
                        chatList.add(new ChatDTO(chat.getChatId(),
                                convertMessageToMessageDTO(chat.getMessages()),
                                chat.getOwner(),chat.getReceiver())));
        return chatList;
    }

    /**
     * Convert a list of  entities Message to DTO Message
     * @param messages the messages to be converted
     * @return
     */
    private List<MessageDTO> convertMessageToMessageDTO(final List<Message> messages){
        List<MessageDTO> convertedMessaged = new ArrayList<>();
        messages.forEach(message -> {
            MessageDTO messageDTO = new MessageDTO(message.getMessage(),message.getUsername());
            convertedMessaged.add(messageDTO);
        });

        return convertedMessaged;
    }

    /**
     * Get all events for a user
     * @param username the user which is getting the events
     * @return
     */
    @Override
    public List<ChatEvent> getEvents(String username) {
        return eventService.getEvents(username);
    }


    /**
     * Add a new chat
     * @param sender the owner of the chat
     * @param receiver the other participant of the chat
     * @return
     */
    private ChatDTO addNewChatToAccounts(final Account sender, final Account receiver){

        final AccountDAO accountDAO = new AccountDAO(this.entityManager);
        Chat chat = new Chat();
        chat.setOwner(sender.getUsername());
        chat.setReceiver(receiver.getUsername());
        sender.addChat(chat);
        receiver.addChat(chat);
        accountDAO.save(sender);
        accountDAO.save(receiver);

        return new ChatDTO(chat.getChatId(), new ArrayList<>(),sender.getUsername(),receiver.getUsername());
    }
}
