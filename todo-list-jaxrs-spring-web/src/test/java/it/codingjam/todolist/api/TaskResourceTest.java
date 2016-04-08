package it.codingjam.todolist.api;

import it.codingjam.todolist.services.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.SecurityContext;

import java.nio.file.attribute.UserPrincipal;
import java.util.Optional;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class TaskResourceTest {

    @InjectMocks
    private TaskResource taskResource;

    @Mock
    private TaskService taskService;

    @Mock
    private SecurityContext securityContext;

    @Test(expected = NotFoundException.class)
    public void shouldNotFindResource() {
        when(taskService.getBy(anyInt(), anyString())).thenReturn(Optional.empty());
        when(securityContext.getUserPrincipal()).thenReturn((UserPrincipal) () -> "admin");

        taskResource.getTask(1);
    }
}