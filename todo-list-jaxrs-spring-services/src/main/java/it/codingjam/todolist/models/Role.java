package it.codingjam.todolist.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 */
@Entity
@Table(name = "roles")
public class Role implements Serializable {

    @Id
    @NotNull
    @Enumerated(EnumType.STRING)
    private RoleType id;

    private String description;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    public void add(User user) {
        if (this.users == null) {
            this.users = new ArrayList<>();
        }
        this.users.add(user);
    }

    public RoleType getId() {
        return id;
    }

    public void setId(RoleType id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
