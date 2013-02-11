package pl.polidea.webimageview;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import pl.polidea.webimageview.net.TaskContainer;
import pl.polidea.webimageview.net.WebCallback;

/**
 * The Class TaskContainerTest.
 */
@RunWith(RobolectricTestRunner.class)
public class TaskContainerTest {

    /**
     * Test adding new task.
     */
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

    /**
     * Test adding tasks with same path.
     */
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

    /**
     * Test result adding tasks with same path.
     */
    @Test
    public void testResultAddingTasksWithSamePath() {
        // given
        final String path = "path";
        final WebCallback listener = Mockito.mock(WebCallback.class);
        final WebCallback listener1 = Mockito.mock(WebCallback.class);
        final TaskContainer container = new TaskContainer();

        // when
        final boolean add1 = container.addTask(path, listener);
        final boolean add2 = container.addTask(path, listener1);

        // then
        assertTrue(add1);
        assertFalse(add2);
    }

    /**
     * Test removing task.
     */
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

    /**
     * Test callbacks number.
     */
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

    /**
     * Test calling hit callbacks.
     */
    @Test
    public void testCallingHitCallbacks() {
        // given
        final String path = "path";
        final WebCallback listener = Mockito.mock(WebCallback.class);
        final WebCallback listener1 = Mockito.mock(WebCallback.class);
        final File file = Mockito.mock(File.class);
        final TaskContainer container = new TaskContainer();
        container.addTask(path, listener);
        container.addTask(path, listener1);

        // when
        container.performCallbacks(path, file);

        // when
        Mockito.verify(listener, Mockito.times(1)).onWebHit(path, file);
        Mockito.verify(listener1, Mockito.times(1)).onWebHit(path, file);
    }

    /**
     * Test calling miss callbacks.
     */
    @Test
    public void testCallingMissCallbacks() {
        // given
        final String path = "path";
        final WebCallback listener = Mockito.mock(WebCallback.class);
        final WebCallback listener1 = Mockito.mock(WebCallback.class);
        final TaskContainer container = new TaskContainer();
        container.addTask(path, listener);
        container.addTask(path, listener1);

        // when
        container.performMissCallbacks(path);

        // when
        Mockito.verify(listener, Mockito.times(1)).onWebMiss(path);
        Mockito.verify(listener1, Mockito.times(1)).onWebMiss(path);
    }
}
