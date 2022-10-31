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
import java.util.*;
import java.util.List;

public class MainChat extends JFrame implements ChatEventListener {
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

    public MainChat(final String username)  {

        this.username = username;
        this.profileName.setText("Loged in as " + username);
        this.model = new DefaultListModel<>();


        chatList.setModel(model);
        chatList.setFixedCellHeight(60);
        chatArea.setFont(new Font("Serif",Font.PLAIN,20));
        chatArea.setMargin( new Insets(10,10,10,10));


        sendButton.addActionListener(ev -> {
            MessageDTO newMessage = new MessageDTO();
            newMessage.setMessage(typeMesasgeArea.getText());
            newMessage.setSender(this.username);
            newMessage.setReceiver(chatList.getSelectedValue().toString().trim());
            newMessage.setChatId(currentChatId);
            try {
                sendMessage(newMessage);
                getCurrentChat().ifPresent((Chat chat) ->{
                            chat.addMessage(new Message(currentChatId,newMessage.getSender(),newMessage.getMessage()));
                            addMessagesToChat();
                            typeMesasgeArea.setText("");
                        });


            } catch (RemoteException e) {
                e.printStackTrace();
            }

        });
        createChatButton.addActionListener(ev -> {
            String usernameToFind = findTextArea.getText();
            ChatDTO chatDTO = null;
            try {
                chatDTO = ChatController.getInstance()
                        .createNewChat(new UserDTO(this.username),new UserDTO(usernameToFind));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if(chatDTO != null){
                Chat newChat =  new Chat(chatDTO.getId(), chatDTO.getReceiver(),chatDTO.getOwner(), new ArrayList<>());
                chats.add(newChat);
                model.addElement(newChat);
            }else {
                JOptionPane.showMessageDialog(null,"User "+ usernameToFind + " does not exists!");
            }
        });

        chatList.addListSelectionListener(lse -> {
            if (!lse.getValueIsAdjusting()) {
                Optional<Chat> chatId = getChatId(chatList.getSelectedValue().toString());
                if(chatId.isPresent()){
                    currentChatId = chatId.get().getChatId();
                }

                addMessagesToChat();
            }
        });


        try {
            initializeChats();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        DispacherService.getInstance(username)
                .addListner(this);

        try {
            ChatController.getInstance().refresh();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
               DispacherService.getInstance(username).removeListner(MainChat.this);
            }
        });

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }

    private void initializeChats() throws RemoteException {
        initializeChatsList();
        initalizeGuiChats();

    }
    private void initializeChatsList() throws RemoteException {

        ChatController.getInstance()
                .getAllChatsForUser(this.username)
                .forEach((ChatDTO chat) -> {
                    Chat newChat = new Chat();
                    newChat.setChatId(chat.getId());
                    if(chat.getOwner() != null && chat.getReceiver() != null){
                        if(chat.getOwner().equals(this.username)){
                            newChat.setSender(chat.getReceiver());
                        }else {
                            newChat.setSender(chat.getOwner());
                        }
                    }
                    newChat.setMessageList(convertMessages(chat.getMessages()));
                    chats.add(newChat);
                });
    }

    public List<Message> convertMessages(final List<MessageDTO> messages){
        List<Message> convertedMessages = new ArrayList<>();

        messages.forEach((MessageDTO message) -> {
            convertedMessages.add(new Message(message.getChatId(),message.getSender(),message.getMessage()));
        });

        return convertedMessages;
    }

    private void initalizeGuiChats(){
        chats.stream()
                .findFirst()
                .ifPresent((Chat chat) -> currentChatId = chat.getChatId());
        chats.forEach((Chat chat) -> model.addElement(chat));

        addMessagesToChat();


    }

    private void addMessagesToChat(){
        chatArea.setText("");
        getCurrentChat().ifPresent((Chat chat) -> {
            chat.getMessageList().forEach((Message message) -> {
                chatArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
                System.out.println(message.getUser() + " : " + message.getMessage()+ "\n");
                chatArea.append(message.getUser() + " : "+ message.getMessage()+ "\n");
            });
        });

    }

    private ComponentOrientation getOrientation(String user){
        if(user.equals(this.username)) {
            return ComponentOrientation.RIGHT_TO_LEFT;
        }else{
            return ComponentOrientation.LEFT_TO_RIGHT;
        }
    }
    private Optional<Chat> getChatId(final String user){
        return chats.stream()
                .peek(chat -> System.out.println("CHAT: " + chat.getSender() +" User: "+user  + " id " + chat.getChatId()))
                .filter((Chat chat) -> user.contains(chat.getSender()))
                .findFirst();
    }

    @Override
    public boolean isApplicable(Class eventClass) {
        System.out.println("NEW EVEEEEEEEEEEEEEEEEENT ACEEEPT");
        return NewMessageEvent.class.equals(eventClass)
                || NewChatEvent.class.equals(eventClass);
    }

    @Override
    public void accept(ChatEvent event) {
        System.out.println("NEW EVEEEEEEEEEEEEEEEEENT");
        if(event instanceof NewChatEvent){
            System.out.println("NEW CHAT EVENT!!!!!");
            Chat newChat = new Chat();
            NewChatEvent newChatReceived = (NewChatEvent) event;
            newChat.setChatId(newChatReceived.getChatId());
            newChat.setSender(newChatReceived.getSender());
            newChat.setMessageList(new ArrayList<>());
            newChat.setReceiver(newChatReceived.getRecipient());
            System.out.println(newChat.getChatId() + " :  " + newChat.getSender() + " :  " + newChat.getMessageList());
            this.chats.add(newChat);
            this.model.addElement(newChat);
            System.out.println("Chat Added");

        }else if(event instanceof NewMessageEvent){
              System.out.println("NEW CHAT EVEEEEEEEEEEEEEEENT ########");


              NewMessageEvent newMessage = (NewMessageEvent) event;
              System.out.println("CHAT ID : " + newMessage.getChatId());

              Optional<Chat> chat = this.chats.stream()
                      .filter((Chat chatElement) -> chatElement.getChatId() == newMessage.getChatId())
                      .findFirst();
                if(chat.isPresent()){
                    chat.get().addMessage(new Message(
                            newMessage.getChatId(),
                            newMessage.getSender(),
                            newMessage.getMessage()));
                    chatArea.append(newMessage.getSender() + ": "+ newMessage.getMessage()+ "\n");
                }


        }
    }

    private Optional<Chat> getCurrentChat(){
        return chats.stream().filter((Chat chat) ->chat.getChatId() == currentChatId ).findFirst();
    }

    private void sendMessage(MessageDTO message) throws RemoteException {
        ChatController.getInstance().sendMessage(message);
    }
}
