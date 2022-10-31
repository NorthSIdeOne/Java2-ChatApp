package server.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int chatId;

    private String owner;
    private String receiver;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "CHAT_ID", referencedColumnName = "ID")
    private List<Message> messages = new ArrayList<>();

    public void addMessage(Message message){
        messages.add(message);
    }

    public int getChatId() {
        return chatId;
    }


    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
