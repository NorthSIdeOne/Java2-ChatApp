package server.model;

import javax.persistence.*;

/**
 * This class represent a user with all the data necessary to login into the app
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "User.findByEmail", query= "SELECT u FROM User u where u.email = :email" ),
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int userId;

    /**
     * Email address of the user which is unique
     */
    @Column(unique = true)
    private String email;

    /**
     * User password
     */
    private String password;

    public int getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}
