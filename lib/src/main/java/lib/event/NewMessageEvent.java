package lib.event;

import lib.dto.MessageDTO;

import java.util.Date;

/**
 * This class represent an event which is built when a new message is sent
 * between 2 users.
 */
public class NewMessageEvent implements ChatEvent{
    private static final long serialVersionUID = 1L;

    /**
     * Holds the DTO message object
     */
    private final MessageDTO message;

    public NewMessageEvent(MessageDTO message) {
        this.message = message;
    }

    /**
     * Get the message content
     * @return String
     */
    public String getMessage(){
        return this.message.getMessage();
    }

    /**
     * Get sender username
     * @return string
     */
    public String getSender(){
        return this.message.getSender();
    }

    /**
     * Get receiver username
     * @return string
     */
    public String getReceiver(){
        return this.message.getReceiver();
    }

    /**
     * Get the chat id in which the message was sent
     * @return int
     */
    public int getChatId(){
        return this.message.getChatId();
    }

    public Date getTimestamp(){
        return this.message.getTimeStamp();
    }

}
