package client.models;

import java.util.List;

public class Chat {

    private int chatId;
    private String sender;
    private String receiver;
    private List<Message> messageList;

    public Chat(int chatId, String sender, String receiver, List<Message> messageList) {
        this.chatId = chatId;
        this.sender = sender;
        this.receiver = receiver;
        this.messageList = messageList;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Chat(){

    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void addMessage(final Message message){
        this.messageList.add(message);
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }


    @Override
    public String toString() {
        return "     " + this.sender;
    }
}
