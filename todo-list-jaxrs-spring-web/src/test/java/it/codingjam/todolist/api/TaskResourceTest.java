package it.codingjam.todolist.api;

import it.codingjam.todolist.services.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.NotFoundException;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

/**
 */
@RunWith(MockitoJUnitRunner.class)
public class TaskResourceTest {

    @InjectMocks
    private TaskResource taskResource;

    @Mock
    private TaskService taskService;

    @Test(expected = NotFoundException.class)
    public void shouldNotFindResource() {
        when(taskService.getById(anyInt())).thenReturn(Optional.empty());

        taskResource.getTask(1);
    }
}