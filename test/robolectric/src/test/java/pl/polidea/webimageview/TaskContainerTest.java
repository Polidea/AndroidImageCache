package pl.polidea.webimageview;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.Mockito;

public class TaskContainerTest {

    @Test
    public void testAddingNewTask() {
        // given
        final String path = "path";
        final WebCallback listener = Mockito.mock(WebCallback.class);
        final TaskContainer container = new TaskContainer();

        // when
        container.addTask(path, listener);

        // then
        assertEquals(1, container.size());
    }

    @Test
    public void testAddingTasksWithSamePath() {
        // given
        final String path = "path";
        final WebCallback listener = Mockito.mock(WebCallback.class);
        final WebCallback listener1 = Mockito.mock(WebCallback.class);
        final TaskContainer container = new TaskContainer();

        // when
        container.addTask(path, listener);
        container.addTask(path, listener1);

        // then
        assertEquals(1, container.size());
    }

    @Test
    public void testRemovingTask() {
        // given
        final String path = "path";
        final String path1 = "path1";
        final WebCallback listener = Mockito.mock(WebCallback.class);
        final WebCallback listener1 = Mockito.mock(WebCallback.class);
        final TaskContainer container = new TaskContainer();
        container.addTask(path, listener);
        container.addTask(path1, listener1);

        // when
        container.remove(path);

        // then
        assertEquals(1, container.size());
    }

    @Test
    public void testCallbacksNumber() {
        // given
        final String path = "path";
        final String path1 = "path1";
        final WebCallback listener = Mockito.mock(WebCallback.class);
        final WebCallback listener1 = Mockito.mock(WebCallback.class);
        final TaskContainer container = new TaskContainer();

        // when
        container.addTask(path, listener);
        container.addTask(path1, listener1);

        // then
        assertEquals(2, container.callbackSize());
    }
}
