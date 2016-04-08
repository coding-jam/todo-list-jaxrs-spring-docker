package it.codingjam.todolist.models;

import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 */
@Entity
@Table(name = "users")
@NamedQuery(name = User.COUNT_USERS, query = "select count(u) from User u")
public class User {

    public static final String COUNT_USERS = "Users.count";

    @Id
    @NotNull
    @Column(name = "user_name")
    private String userName;

    @NotNull
    private String password;


    private String firstName;

    private String lastName;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_name")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;

    @PrePersist
    void encryptPassword() {
        this.password = DigestUtils.sha1Hex(this.password);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
