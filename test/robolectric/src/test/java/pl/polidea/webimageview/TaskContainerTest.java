package pl.polidea.webimageview;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import android.graphics.Bitmap;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
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

    @Test
    public void testCallingHitCallbacks() {
        // given
        final String path = "path";
        final WebCallback listener = Mockito.mock(WebCallback.class);
        final WebCallback listener1 = Mockito.mock(WebCallback.class);
        final Bitmap bitmap = Mockito.mock(Bitmap.class);
        final TaskContainer container = new TaskContainer();
        container.addTask(path, listener);
        container.addTask(path, listener1);

        // when
        container.performCallbacks(path, bitmap);

        // when
        Mockito.verify(listener, Mockito.times(1)).onWebHit(path, bitmap);
        Mockito.verify(listener1, Mockito.times(1)).onWebHit(path, bitmap);
    }

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
