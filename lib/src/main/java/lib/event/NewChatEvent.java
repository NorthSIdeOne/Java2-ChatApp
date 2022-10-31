package lib.event;

public class NewChatEvent implements ChatEvent{
    private static final long serialVersionUID = 1L;

    private int chatId;
    private String recipient;
    private String sender;

    public NewChatEvent(int chatId, String sender,String recipient) {
        this.chatId = chatId;
        this.sender = sender;
        this.recipient = recipient;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }
}
