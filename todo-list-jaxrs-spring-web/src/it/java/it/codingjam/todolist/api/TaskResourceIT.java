package it.codingjam.todolist.api;

import it.codingjam.todolist.api.exceptions.ErrorMessage;
import it.codingjam.todolist.api.rules.WebClientRule;
import it.codingjam.todolist.api.utils.APIVersion;
import it.codingjam.todolist.models.Task;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import java.util.List;

import static org.junit.Assert.*;

/**
 */
public class TaskResourceIT {

    @ClassRule
    public static WebClientRule clientRule = new WebClientRule(APIVersion.V1);

    @Test
    public void shouldReturnAllTasks() {
        Response response = clientRule.getResource().path("/todos").request().get(Response.class);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        List<Task> tasks = response.readEntity(new GenericType<List<Task>>() {
        });
        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());
        assertEquals(3, tasks.size());
        assertEquals(1, tasks.get(0).getId());
        assertEquals(2, tasks.get(1).getId());
        assertEquals(3, tasks.get(2).getId());
    }

    @Test
    public void shouldReturnBadRequest() {
        Response response = clientRule.getResource().path("todos/1a").request().get(Response.class);
        ErrorMessage errorMessage = response.readEntity(ErrorMessage.class);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertNotNull(errorMessage);
        assertEquals("Validation error", errorMessage.getMessage());
        assertNotNull(errorMessage.getDetails());
        assertFalse(errorMessage.getDetails().isEmpty());
    }

}