package pl.polidea.imagecache;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TestExecutorService implements ExecutorService {

    List<Runnable> commands = new LinkedList<Runnable>();

    @Override
    public void execute(final Runnable command) {
        commands.add(command);
    }

    public void startCommands() {
        for (final Runnable command : commands) {
            command.run();
        }

        commands.clear();
    }

    @Override
    public void shutdown() {

    }

    @Override
    public List<Runnable> shutdownNow() {
        return null;
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public <T> Future<T> submit(final Callable<T> task) {
        return null;
    }

    @Override
    public <T> Future<T> submit(final Runnable task, final T result) {
        return null;
    }

    @Override
    public Future< ? > submit(final Runnable task) {
        commands.add(task);
        return null;
    }

    @Override
    public <T> List<Future<T>> invokeAll(final Collection< ? extends Callable<T>> tasks) throws InterruptedException {
        return null;
    }

    @Override
    public <T> List<Future<T>> invokeAll(final Collection< ? extends Callable<T>> tasks, final long timeout,
            final TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
    public <T> T invokeAny(final Collection< ? extends Callable<T>> tasks) throws InterruptedException,
            ExecutionException {
        return null;
    }

    @Override
    public <T> T invokeAny(final Collection< ? extends Callable<T>> tasks, final long timeout, final TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }

}
