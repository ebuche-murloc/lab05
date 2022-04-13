package services.dbService;

import model.UserProfile;
import javax.persistence.*;

@Entity
@Table(name="users")
public class UsersDataSet {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String login;
    @Column(name = "password")
    private String pass;
    @Column(name = "email")
    private String email;

    public UsersDataSet() {

    }

    public UsersDataSet(UserProfile userProfile) {
        this.login = userProfile.getLogin();
        this.pass = userProfile.getPass();
        this.email = userProfile.getEmail();
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public String getEmail() {
        return email;
    }
}