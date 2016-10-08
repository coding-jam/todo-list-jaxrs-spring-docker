package it.codingjam.todolist.api;

import it.codingjam.todolist.api.utils.APIVersion;
import it.codingjam.todolist.models.RoleType;
import it.codingjam.todolist.models.Task;
import it.codingjam.todolist.models.User;
import it.codingjam.todolist.services.TaskService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
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
@RolesAllowed({RoleType.ADMIN_STRING, RoleType.CUSTOMER_STRING})
public class TaskResource {

    @Context
    private UriInfo uriInfo;

    @Context
    private SecurityContext security;

    @Inject
    private TaskService taskService;

    @GET
    @Path("/all")
    @RolesAllowed(RoleType.ADMIN_STRING)
    public List<Task> getAllOfAllUsers() {
        return taskService.getAllTasks();
    }

    @GET
    public List<Task> getAll() {
        return taskService.getAllTasks(security.getUserPrincipal().getName());
    }

    @GET
    @Path("{taskId}")
    public Task getTask(@NotNull @Min(1) @PathParam("taskId") long id) {
        return taskService.getBy(id, security.getUserPrincipal().getName())
                .orElseThrow(() -> new NotFoundException(String.format("Task with id %d not found", id)));
    }

    @POST
    public Response newTask(@Valid Task task) {
        task.setUser(new User(security.getUserPrincipal().getName()));
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
        task.setUser(new User(security.getUserPrincipal().getName()));
        taskService.update(task);
    }

    @DELETE
    @Path("{taskId}")
    public Response removeTask(@NotNull @Min(1) @PathParam("taskId") long id) {
        String userName = security.getUserPrincipal().getName();
        boolean deleted = taskService.deleteBy(id, userName);
        if (deleted) {
            return Response.noContent().build();
        } else {
            throw new IllegalArgumentException(String.format("Task id %d cannot be deleted for user %s", id, userName));
        }
    }

}
