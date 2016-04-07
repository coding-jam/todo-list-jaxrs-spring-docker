package it.codingjam.todolist.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * Task entity
 *
 */
@Entity
@Table(name = "tasks")
@NamedQuery(name = Task.GET_ALL, query = "select t from Task t")
public class Task {

    public static final String GET_ALL = "Task.getAll";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String text;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TaskStatus status = TaskStatus.UNDONE;

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

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", status=" + status +
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
