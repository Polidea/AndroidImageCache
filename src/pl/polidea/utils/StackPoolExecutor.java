package pl.polidea.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class StackPoolExecutor extends ThreadPoolExecutor {
    public StackPoolExecutor(int poolSize) {
        super(
                poolSize,
                poolSize,
                10L,
                TimeUnit.SECONDS,
                new StackBlockingDeque(),
                new LowPriorityThreadFactory()
        );
    }

    public StackPoolExecutor(int poolSize, int maximumPoolSize) {
        super(poolSize, maximumPoolSize, 10L, TimeUnit.SECONDS, new StackBlockingDeque(maximumPoolSize), new LowPriorityThreadFactory(), new DiscardPolicy());
    }

    public static class LowPriorityThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable runnable) {
            final Thread thread = new Thread(runnable, "Image downloading thread");
            thread.setPriority(Thread.MIN_PRIORITY);
            return thread;
        }
    }

}
