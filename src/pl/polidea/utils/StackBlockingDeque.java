package pl.polidea.utils;

import java.util.concurrent.TimeUnit;
import pl.polidea.thridparty.LinkedBlockingDeque;

public class StackBlockingDeque extends LinkedBlockingDeque<Runnable> {

    /**
     *
     */
    private static final long serialVersionUID = 1635542918696298694L;

    public StackBlockingDeque() {
        super();
    }

    public StackBlockingDeque(int capacity) {
        super(capacity);
    }

    @Override
    public boolean offer(Runnable runnable) {
        if (contains(runnable)) {
            return false;
        }

        if (remainingCapacity() > 0) {
            return super.offer(runnable);
        } else {
            pollFirst();
            return super.offer(runnable);
        }

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
