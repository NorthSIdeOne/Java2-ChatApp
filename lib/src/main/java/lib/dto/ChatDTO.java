package lib.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChatDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;

    private List<MessageDTO> messages = new ArrayList<>();

    private String owner;

    private String receiver;

    public ChatDTO(int id, List<MessageDTO> messages,String sender,String receiver) {
        this.id = id;
        this.messages = messages;
        this.owner = sender;
        this.receiver = receiver;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDTO> messages) {
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

    @Override
    public String toString() {
        return "ChatDTO{" +
                "id=" + id +
                ", messages=" + messages +
                ", owner='" + owner + '\'' +
                ", receiver='" + receiver + '\'' +
                '}';
    }
}
