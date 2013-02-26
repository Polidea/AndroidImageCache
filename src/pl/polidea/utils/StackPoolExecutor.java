package pl.polidea.utils;

import java.util.concurrent.*;

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

    public static class MyPolicy implements RejectedExecutionHandler {
        /**
         * Creates a {@code DiscardOldestPolicy} for the given executor.
         */
        public MyPolicy() {

        }

        /**
         * Obtains and ignores the next task that the executor
         * would otherwise execute, if one is immediately available,
         * and then retries execution of task r, unless the executor
         * is shut down, in which case task r is instead discarded.
         *
         * @param r the runnable task requested to be executed
         * @param e the executor attempting to execute this task
         */
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            System.out.println("Exe");

//            while (e.getMaximumPoolSize() < e.getQueue().size()) {
            System.out.println("Reject " );
            e.getQueue().poll();
//                }
//                e.execute(r);
//            }
        }
    }

    public static class MyRun implements Runnable {

        public int n;

        MyRun(int n) {
            this.n = n;
        }

        @Override
        public void run() {
            System.out.println("AHAHA " + n);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

}
