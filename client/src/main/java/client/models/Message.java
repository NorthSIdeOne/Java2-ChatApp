package client.models;

public class Message {

    private int chatId;
    private String user;
    private String message;

    public Message(int chatId, String user, String message) {
        this.chatId = chatId;
        this.user = user;
        this.message = message;
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
}
