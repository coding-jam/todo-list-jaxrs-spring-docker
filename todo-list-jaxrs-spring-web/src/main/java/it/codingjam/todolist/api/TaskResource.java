package it.codingjam.todolist.api;

import it.codingjam.todolist.api.utils.APIVersion;
import it.codingjam.todolist.models.Task;
import it.codingjam.todolist.services.TaskService;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 *
 */
@Path(APIVersion.V1 + "/todos")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public class TaskResource {

    @Inject
    private TaskService taskService;

    @GET
    public List<Task> getAll() {
        return taskService.getAllTasks();
    }

    @GET
    @Path("{taskId}")
    public Task getTask(@NotNull @Pattern(regexp = "[1-9]+") @PathParam("taskId") String id) {
        return taskService.getById(Integer.valueOf(id))
                .orElseThrow(() -> new NotFoundException(String.format("Task with id %s not found", id)));
    }
}
