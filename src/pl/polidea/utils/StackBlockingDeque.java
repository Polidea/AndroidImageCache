package pl.polidea.utils;

import pl.polidea.thridparty.LinkedBlockingDeque;

import java.util.concurrent.TimeUnit;

public class StackBlockingDeque extends LinkedBlockingDeque<Runnable> {

    /**
     *
     */
    private static final long serialVersionUID = 1635542918696298694L;

    @Override
    public boolean offer(Runnable runnable) {
        if (contains(runnable)) {
            remove(runnable);
        }
        return super.offer(runnable);
    }

    @Override
    public Runnable poll() {
        return super.pollLast();
    }

    @Override
    public Runnable take() throws InterruptedException {
        return super.takeLast();
    }

    @Override
    public Runnable poll(long timeout, TimeUnit unit) throws InterruptedException {
        return super.pollLast(timeout, unit);
    }

}
