package it.codingjam.todolist.services;

import it.codingjam.todolist.models.Task;
import it.codingjam.todolist.interceptors.Profiling;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 */
@Named
@Profiling
public class TaskService {

    private static final Logger LOGGER = Logger.getLogger(TaskService.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    void init() {
        LOGGER.info("Init task service with hash " + hashCode());
    }

    public Optional<Task> getById(long id) {
        Task task = this.entityManager.find(Task.class, id);
        return Optional.ofNullable(task);
    }

    public List<Task> getAllTasks() {
        return this.entityManager.createNamedQuery(Task.GET_ALL, Task.class).getResultList();
    }

    @Transactional
    public void save(Task task) {
        LOGGER.info(String.format("Saving new task %s", task));
        this.entityManager.persist(task);
    }

    @Transactional
    public void deleteById(long id) {
        LOGGER.info(String.format("Removing task %s", id));
        this.entityManager.createNamedQuery("Task.remove").setParameter("id", id).executeUpdate();
    }

    @Transactional
    public void update(Task task) {
        LOGGER.info(String.format("Updating task %s", task.getId()));
        this.entityManager.merge(task);
    }

    private Task newTask(int id) {
        Task task = new Task();
        task.setId(id);
        task.setText("da fare");
        return task;
    }
}
