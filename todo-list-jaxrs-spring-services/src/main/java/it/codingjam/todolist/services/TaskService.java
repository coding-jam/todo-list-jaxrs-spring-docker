package it.codingjam.todolist.services;

import com.google.common.collect.Lists;
import it.codingjam.todolist.models.Task;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 */
@Named
public class TaskService {

    private static final Logger LOGGER = Logger.getLogger(TaskService.class.getName());

    private List<Task> data = Lists.newArrayList(newTask(1), newTask(2), newTask(3));

    @PostConstruct
    void init() {
        LOGGER.info("Init task service with hash " + hashCode());
    }

    public Optional<Task> getById(long id) {
        return data.stream().filter(task -> task.getId() == id).findFirst();
    }

    public List<Task> getAllTasks() {
        return data;
    }

    public void save(Task task) {
        LOGGER.info(String.format("Saving new task %s", task));
        Optional<Long> max = data.stream().map(Task::getId).max((value1, value2) -> Long.valueOf(value1).compareTo(value2));
        task.setId(max.get() + 1);
        data.add(task);
    }

    public void deleteById(long id) {
        LOGGER.info(String.format("Removing task %s", id));
        List<Task> taskList = data.stream().filter(task -> task.getId() != id).collect(Collectors.toList());
        synchronized (data) {
            data = taskList;
        }
    }

    public void update(Task task) {
        LOGGER.info(String.format("Updating task %s", task.getId()));
        Task taskFromList = data.stream().filter(taskInList -> taskInList.getId() == task.getId()).findFirst().orElseThrow(() -> new NoSuchElementException());
        synchronized (data) {
            taskFromList.setText(task.getText());
            taskFromList.setStatus(task.getStatus());
        }
    }

    private Task newTask(int id) {
        Task task = new Task();
        task.setId(id);
        task.setText("da fare");
        return task;
    }
}
