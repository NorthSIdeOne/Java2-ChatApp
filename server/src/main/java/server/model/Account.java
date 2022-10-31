package server.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name = "Account.findByUsername", query= "SELECT a FROM Account a where a.username = :username" ),
        @NamedQuery(name = "Account.getAllUsernames", query= "SELECT a.username FROM Account a"),
        @NamedQuery(name = "Account.findByUserId", query= "SELECT a.username FROM Account a where a.userId = :userId")
})

public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int accountId;

    /**
     * The username which will be displayed
     */
    @Column(unique = true)
    private String username;

    /**
     * The user details to log in
     */
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private User user;

    @Column(name = "USER_ID", insertable=false, updatable=false)
    private Integer userId;

    /**
     * The list of chats associated to an account
     */
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "CHATS_MAPPING",
    joinColumns = @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ID"),
    inverseJoinColumns = @JoinColumn(name = "CHAT_ID", referencedColumnName = "ID"))
    private List<Chat> chats = new ArrayList<>();


    public void addChat(Chat chat){
        chats.add(chat);
    }

    public int getAccountId() {
        return accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }
}
