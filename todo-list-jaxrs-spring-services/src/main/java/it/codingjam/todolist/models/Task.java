package it.codingjam.todolist.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 */
public class Task {

    private long id;

    @NotNull
    private String text;

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
