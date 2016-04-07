package it.codingjam.todolist.api;

import it.codingjam.todolist.api.exceptions.ErrorMessage;
import it.codingjam.todolist.api.rules.WebClientRule;
import it.codingjam.todolist.api.utils.APIVersion;
import it.codingjam.todolist.models.Task;
import it.codingjam.todolist.models.TaskStatus;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Integration tests on {@link TaskResource}
 */
public class TaskResourceIT {

    @ClassRule
    public static WebClientRule clientRule = new WebClientRule(APIVersion.V1);

    @Test
    public void shouldReturnAllTasks() {
        addNewTask();

        Response response = clientRule.getResource().path("/todos").request().get(Response.class);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        List<Task> tasks = response.readEntity(new GenericType<List<Task>>() {
        });
        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());
    }

    @Test
    public void shouldReturnBadRequestOnGetTask() {
        Response response = clientRule.getResource().path("todos/-1").request().get(Response.class);
        ErrorMessage errorMessage = response.readEntity(ErrorMessage.class);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertNotNull(errorMessage);
        assertEquals("Validation error", errorMessage.getMessage());
        assertNotNull(errorMessage.getDetails());
        assertFalse(errorMessage.getDetails().isEmpty());
    }

    @Test
    public void shouldReturnNotFound() {
        Response response = clientRule.getResource().path("todos/900000").request().get(Response.class);
        ErrorMessage errorMessage = response.readEntity(ErrorMessage.class);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertNotNull(errorMessage);
        assertNotNull(errorMessage.getMessage());
    }

    @Test
    public void shouldAddNewTask() {
        List<Task> tasksBeforeTest = getTasks();

        Task task = new Task();
        task.setText("Nuovo task da fare");
        Response response = clientRule.getResource().path("/todos").request().post(Entity.json(task), Response.class);
        Task newTask = response.readEntity(Task.class);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertTrue(response.getHeaderString("location").endsWith("/todos/" + newTask.getId()));
        assertTrue(newTask.getId() > 0);
        assertEquals(task.getText(), newTask.getText());
        assertFalse(tasksBeforeTest.contains(newTask));
    }

    @Test
    public void shouldReturnBadRequestOnAddNewTask() {
        Response response = clientRule.getResource().path("/todos").request().post(Entity.json("{}"), Response.class);
        ErrorMessage errorMessage = response.readEntity(ErrorMessage.class);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertNotNull(errorMessage);
        assertNotNull(errorMessage.getMessage());
    }

    @Test
    public void shouldDeleteTask() {
        List<Task> tasksBeforeTest = getTasks();
        Task task = tasksBeforeTest.get(tasksBeforeTest.size() - 1);

        Response response = clientRule.getResource().path("/todos/" +  task.getId()).request().delete(Response.class);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

        List<Task> tasksAfterTest = getTasks();
        assertEquals(tasksBeforeTest.size() - 1, tasksAfterTest.size());
    }

    @Test
    public void shouldUpdateTask() {
        List<Task> tasksBeforeTest = getTasks();

        Task aTask = tasksBeforeTest.get(0);
        TaskStatus newStatus = aTask.getStatus() == TaskStatus.DONE ? aTask.getStatus() : TaskStatus.UNDONE;
        aTask.setStatus(newStatus);

        Response response = clientRule.getResource().path("/todos/" + aTask.getId()).request().put(Entity.json(aTask));

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

        Task updatedTask = getTask(aTask.getId());
        assertEquals(newStatus, updatedTask.getStatus());
    }

    private List<Task> getTasks() {
        return clientRule.getResource().path("/todos").request().get(new GenericType<List<Task>>() {
        });
    }

    private Task getTask(long id) {
        return clientRule.getResource().path("todos/" + id).request().get(Task.class);
    }

    private void addNewTask() {
        Task task = new Task();
        task.setText("Task di prova");
        clientRule.getResource().path("/todos").request().post(Entity.json(task));
    }
}