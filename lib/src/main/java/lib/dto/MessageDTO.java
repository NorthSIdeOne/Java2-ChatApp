package lib.dto;

import java.io.Serializable;

public class MessageDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String message;
    private String sender;
    private String receiver;
    private int chatId;

    public MessageDTO(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    public MessageDTO() {

    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
