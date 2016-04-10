package it.codingjam.todolist.services;

import it.codingjam.todolist.models.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.Optional;

import static org.junit.Assert.assertFalse;

/**
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContextTest.xml")
public class TaskServiceIT {

    @Inject
    private TaskService taskService;

    @Test
    public void shouldNotFindTask() {
        Optional<Task> user = taskService.getBy(-1, "testUser");

        assertFalse(user.isPresent());
    }
}