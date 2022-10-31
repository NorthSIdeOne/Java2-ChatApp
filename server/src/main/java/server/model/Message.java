package server.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int messageId;

    private String message;

    private String username;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private final Date timestamp;

    public Message(){
        this.timestamp = new Date();
    }

    public int getMessageId() {
        return messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
