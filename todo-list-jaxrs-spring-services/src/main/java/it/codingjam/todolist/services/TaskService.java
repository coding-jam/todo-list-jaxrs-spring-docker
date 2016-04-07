package it.codingjam.todolist.services;

import it.codingjam.todolist.models.Task;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 */
@Named
public class TaskService {

    private static final Logger LOGGER = Logger.getLogger(TaskService.class.getName());

    @PostConstruct
    void init() {
        LOGGER.info("Init task service with hash " + hashCode());
    }

    public Optional<Task> getById(int id) {
        // TODO
        Task task = new Task();
        task.setId(id);
        task.setText("da fare");

        return Optional.of(task);
    }

    public List<Task> getAllTasks() {
        return Arrays.asList(getById(1).get(), getById(2).get(), getById(3).get());
    }
}
