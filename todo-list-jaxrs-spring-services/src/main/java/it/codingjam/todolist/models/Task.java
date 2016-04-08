package it.codingjam.todolist.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

/**
 * Task entity
 */
@Entity
@Table(name = "tasks")
@NamedQueries({
        @NamedQuery(name = Task.GET_ALL, query = "select t from Task t"),
        @NamedQuery(name = Task.GET_ALL_FOR_USER, query = "select t from Task t where t.user.userName = :userName"),
        @NamedQuery(name = Task.GET_FOR_USER_BY_ID, query = "select t from Task t where t.id = :id and t.user.userName = :userName")})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Task implements Serializable {

    public static final String GET_ALL = "Task.getAll";

    public static final String GET_ALL_FOR_USER = "Task.getAllForUser";

    public static final String GET_FOR_USER_BY_ID = "Task.getForUserById";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String text;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TaskStatus status = TaskStatus.UNDONE;

    @ManyToOne
    @JoinColumn(name = "user_name")
    @XmlTransient
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", status=" + status +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;

        Task task = (Task) o;

        if (id != task.id) return false;
        if (text != null ? !text.equals(task.text) : task.text != null) return false;
        return status == task.status;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
