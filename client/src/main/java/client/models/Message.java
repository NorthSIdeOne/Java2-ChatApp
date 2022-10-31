package client.models;

import java.util.Date;

public class Message {

    private int chatId;
    private String user;
    private String message;
    private Date timeStamp;

    public Message(int chatId, String user, String message, Date timeStamp) {
        this.chatId = chatId;
        this.user = user;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
