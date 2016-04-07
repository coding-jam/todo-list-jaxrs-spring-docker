package it.codingjam.todolist.api;

import it.codingjam.todolist.api.utils.APIVersion;
import it.codingjam.todolist.models.Task;
import it.codingjam.todolist.services.TaskService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

/**
 *
 */
@Path(APIVersion.V1 + "/todos")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class TaskResource {

    @Context
    private UriInfo uriInfo;

    @Inject
    private TaskService taskService;

    @GET
    public List<Task> getAll() {
        return taskService.getAllTasks();
    }

    @GET
    @Path("{taskId}")
    public Task getTask(@NotNull @Min(1) @PathParam("taskId") long id) {
        return taskService.getById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Task with id %d not found", id)));
    }

    @POST
    public Response newTask(@Valid Task task) {
        taskService.save(task);
        URI location = URI.create(uriInfo.getAbsolutePath() + "/" + task.getId());
        return Response.created(location).entity(task).build();
    }

    @PUT
    @Path("{taskId}")
    public void updateTask(@NotNull @Min(1) @PathParam("taskId") long id, @Valid Task task) {
        if (task.getId() == 0) {
            task.setId(id);
        }
        taskService.update(task);
    }

    @DELETE
    @Path("{taskId}")
    public Response removeTask(@NotNull @Min(1) @PathParam("taskId") long id) {
        taskService.deleteById(id);
        return Response.noContent().build();
    }

}
