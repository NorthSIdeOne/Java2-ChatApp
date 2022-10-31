package client.gui;

import client.listeners.ChatEventListener;
import client.listeners.DispacherService;
import client.models.Chat;
import client.models.Message;
import client.services.ChatController;
import lib.dto.ChatDTO;
import lib.dto.MessageDTO;
import lib.dto.UserDTO;
import lib.event.ChatEvent;
import lib.event.NewChatEvent;
import lib.event.NewMessageEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainChat extends JFrame implements ChatEventListener {

    private final Logger logger = Logger.getLogger(MainChat.class.getName());
    private JPanel mainPanel;
    private JButton createChatButton;
    private JList chatList;
    private JTextArea chatArea;
    private JTextField typeMesasgeArea;
    private JButton sendButton;
    private JTextField findTextArea;
    private JLabel profileName;
    private DefaultListModel<Chat> model;
    private final String username;
    private Set<Chat> chats = new HashSet<>();
    private int currentChatId;

    public MainChat(final String username) {

        this.username = username;
        this.profileName.setText("Loged in as " + username);

        init();

    }

    /**
     * Initialize all the components
     */
    private void init() {
        configureChatArea();

        initSendMessageButton();

        initCreateChat();

        initChats();

        initEventListener();

        initMainChatWindow();
    }

    /**
     * Initialize the event listeners from the server
     */
    private void initEventListener() {
        addEventListener();
        removeEventListenerWhenClosing();
    }

    /**
     * Add this class as event listener at start
     */
    private void addEventListener() {
        DispacherService.getInstance(username)
                .addListener(this);
    }

    /**
     * Remove the event listener when the window is closed
     */
    private void removeEventListenerWhenClosing() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                DispacherService.getInstance(username).removeListener(MainChat.this);
                System.exit(0);
            }
        });
    }

    /**
     * Configure the main window
     */
    private void initMainChatWindow() {
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


    /**
     * This method initialize the chat list by requesting
     * all the chats for the current user and add the chats
     * to the gui.
     */
    private void initChats() {
        initChatList();

        try {
            initializeChatsList();
        } catch (RemoteException e) {
            logger.log(Level.INFO, "The chats couldn't be retrieved for " + this.username);
        }
        initGuiChats();
        setDefaultChat();
    }

    /**
     * Select first chat by default
     */
    private void setDefaultChat() {
        this.chatList.setSelectedIndex(0);
    }


    /**
     * Initialize the chat list values
     *
     * @throws RemoteException
     */
    private void initializeChatsList() throws RemoteException {

        ChatController.getInstance()
                .getAllChatsForUser(this.username)
                .forEach(this::addToChatList);
    }

    /**
     * Initialize the "send message" button
     */
    private void initSendMessageButton() {

        sendButton.addActionListener(ev -> {
            MessageDTO newMessage = createNewMessage();
            sendMessageToServer(newMessage);
        });
    }

    /**
     * Create a new message to be sent to the server
     *
     * @return a DTO Message
     */
    private MessageDTO createNewMessage() {
        final MessageDTO newMessage = new MessageDTO();
        newMessage.setMessage(typeMesasgeArea.getText());
        newMessage.setSender(this.username);
        newMessage.setReceiver(chatList.getSelectedValue().toString().trim());
        newMessage.setChatId(currentChatId);
        newMessage.setTimeStamp(new Date());
        return newMessage;
    }

    /**
     * Send the new message to the server
     *
     * @param newMessage new message
     */
    private void sendMessageToServer(final MessageDTO newMessage) {
        try {
            sendMessage(newMessage);
            getCurrentChat().ifPresent((Chat chat) -> {
                chat.addMessage(new Message(currentChatId, newMessage.getSender(),
                        newMessage.getMessage(), newMessage.getTimeStamp()));
                addMessagesToChat();
                typeMesasgeArea.setText("");
            });
        } catch (RemoteException e) {
            logger.log(Level.INFO, "The message couldn't be sent. Error with the server");
        }
    }

    /**
     * Convert an DTO Chat to Chat and add it to the chat list
     *
     * @param chat the DTO Chat
     */
    private void addToChatList(final ChatDTO chat) {
        Chat newChat = new Chat();
        newChat.setChatId(chat.getId());
        if (chat.getOwner() != null && chat.getReceiver() != null) {
            if (chat.getOwner().equals(this.username)) {
                newChat.setSender(chat.getReceiver());
            } else {
                newChat.setSender(chat.getOwner());
            }
            newChat.setMessageList(convertMessages(chat.getMessages()));
            chats.add(newChat);
        }
    }


    /**
     * Add a received chat from the server to the chat list
     *
     * @param receivedChat a new chat event
     */
    private void addToChatList(final NewChatEvent receivedChat) {
        Chat chat = new Chat();
        chat.setChatId(receivedChat.getChatId());
        chat.setSender(receivedChat.getSender());
        chat.setMessageList(new ArrayList<>());
        chat.setReceiver(receivedChat.getRecipient());
        this.chats.add(chat);
        this.model.addElement(chat);
    }

    /**
     * Convert a list of DTO Messages into a list of Messages
     *
     * @param messages a list of DTO messages
     * @return List of messages
     */
    public List<Message> convertMessages(final List<MessageDTO> messages) {
        final List<Message> convertedMessages = new ArrayList<>();

        messages.forEach((MessageDTO message) -> {
            convertedMessages.add(new Message(message.getChatId(),
                    message.getSender(), message.getMessage(), message.getTimeStamp()));
        });

        return convertedMessages;
    }

    /**
     * Initialize all the gui chats
     */
    private void initGuiChats() {
        chats.stream()
                .findFirst()
                .ifPresent((Chat chat) -> currentChatId = chat.getChatId());
        chats.forEach((Chat chat) -> model.addElement(chat));
        addMessagesToChat();
    }

    /**
     * This method is used to initialize the list of chats
     * from the chat main window
     */
    private void initChatList() {

        this.model = new DefaultListModel<>();
        this.chatList.setModel(model);
        this.chatList.setFixedCellHeight(60);

        this.chatList.addListSelectionListener(listSelectionEvent -> {
            if (!listSelectionEvent.getValueIsAdjusting()) {
                Optional<Chat> chatId = getChatId(chatList.getSelectedValue().toString());
                chatId.ifPresent(chat -> currentChatId = chat.getChatId());
                addMessagesToChat();
            }
        });
    }

    /**
     * Initialize the creat chat button
     */
    private void initCreateChat() {
        createChatButton.addActionListener(actionEvent -> {
            String usernameToFind = findTextArea.getText();
            ChatDTO chatDTO = createNewChat(usernameToFind);
            addNewChat(chatDTO, usernameToFind);
        });
    }


    /**
     * Create a new request to the server to create a new chat
     *
     * @param usernameToFind the other participant of the chat
     * @return DTO Chat
     */
    private ChatDTO createNewChat(final String usernameToFind) {
        try {
            return ChatController.getInstance()
                    .createNewChat(new UserDTO(this.username), new UserDTO(usernameToFind));
        } catch (RemoteException e) {
            logger.log(Level.INFO, "The new chat between " + this.username +
                    " and " + usernameToFind + " couldn't be created");
            return null;
        }
    }

    /**
     * Add new chat in chat list
     *
     * @param chatDTO        the chat returned from the server
     * @param usernameToFind the other participant of the chat
     */
    private void addNewChat(final ChatDTO chatDTO, final String usernameToFind) {
        if (chatDTO != null) {
            Chat newChat = new Chat(chatDTO.getId(), chatDTO.getReceiver(), chatDTO.getOwner(), new ArrayList<>());
            this.chats.add(newChat);
            this.model.addElement(newChat);
            logger.log(Level.INFO, "New chat is added!");
        } else {
            JOptionPane.showMessageDialog(null, "User " + usernameToFind + " does not exists!");
        }

    }

    /**
     * Configure the chat area style
     */
    private void configureChatArea() {
        this.chatArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        this.chatArea.setFont(new Font("Serif", Font.PLAIN, 20));
        this.chatArea.setMargin(new Insets(10, 10, 10, 10));
    }


    /**
     * Populate the Chat Area with all the messages from the current chat
     */
    private void addMessagesToChat() {
        chatArea.setText("");
        getCurrentChat().ifPresent((Chat chat) -> {
            chat.getMessageList().forEach((Message message) -> {
                chatArea.append("("+new Timestamp(message.getTimeStamp().getTime())+")  " +
                        message.getUser() + " : " + message.getMessage() + "\n");
            });
        });

    }

    /**
     * Return a chat between current user and another user
     *
     * @param user the other participant of the chat
     * @return an optional
     */
    private Optional<Chat> getChatId(final String user) {
        return chats.stream()
                .filter((Chat chat) -> user.contains(chat.getSender()))
                .findFirst();
    }

    /**
     * Used to know if an event is applicable or not for this listener
     *
     * @param eventClass the event class
     * @return boolean
     */
    @Override
    public boolean isApplicable(Class eventClass) {
        return NewMessageEvent.class.equals(eventClass)
                || NewChatEvent.class.equals(eventClass);
    }

    @Override
    public void accept(ChatEvent event) {

        if (event instanceof NewChatEvent) {
            addToChatList((NewChatEvent) event);

        } else if (event instanceof NewMessageEvent) {
            addMessage((NewMessageEvent) event);

        }
    }

    /**
     * Get the chat by id
     *
     * @param chatId the chat id
     * @return an optional
     */
    private Optional<Chat> getChat(final int chatId) {
        return this.chats.stream()
                .filter((Chat chatElement) -> chatElement.getChatId() == chatId)
                .findFirst();
    }

    /**
     * Add new message to the chat
     *
     * @param event new message event
     */
    private void addMessage(final NewMessageEvent event) {
        final Optional<Chat> chat = getChat(event.getChatId());

        if (chat.isPresent()) {
            chat.get().addMessage(new Message(event.getChatId(), event.getSender(),
                    event.getMessage(), event.getTimestamp()));
            if(event.getChatId() == currentChatId){
                chatArea.append("("+new Timestamp(event.getTimestamp().getTime())+")  " +
                        event.getSender() + " : " + event.getMessage() + "\n");
            }
        }
    }

    /**
     * Get the current chat open
     *
     * @return an optional
     */
    private Optional<Chat> getCurrentChat() {
        return chats.stream().filter((Chat chat) -> chat.getChatId() == currentChatId).findFirst();
    }

    /**
     * Sends a message to another user
     *
     * @param message the message object which will be sent to the server
     */
    private void sendMessage(MessageDTO message) throws RemoteException {
        ChatController.getInstance().sendMessage(message);
    }
}
