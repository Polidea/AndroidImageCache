package pl.polidea.utils

import spock.lang.Specification

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class StackPoolExecutorSpecification extends Specification {

    Runnable runnable1 = Mock(Runnable)
    Runnable runnable2 = Mock(Runnable)
    Runnable runnable3 = Mock(Runnable)
    Runnable runnable4 = Mock(Runnable)
    Runnable runnable5 = Mock(Runnable)
    Runnable runnable6 = Mock(Runnable)
    Runnable runnable7 = Mock(Runnable)

    def "should run 1 task while pending tasks are limited to 3 element list"() {
        given:
        StackPoolExecutor executor = new StackPoolExecutor(1, 3)
        def initLatch = new CountDownLatch(1)
        def processingDoneLatch = new CountDownLatch(3)
        def holdingAllRunnables = new CountDownLatch(1)
        and: "submit long executive task"
        executor.submit(new LatchRunnable(initLatch))
        and: "submit other tasks"
        executor.submit(runnable2)
        executor.submit(runnable3)
        executor.submit(runnable4)
        executor.submit(new WaitingRunnable(processingDoneLatch, holdingAllRunnables))
        executor.submit(new WaitingRunnable(processingDoneLatch, holdingAllRunnables))
        executor.submit(new WaitingRunnable(processingDoneLatch, holdingAllRunnables))

        when:
        holdingAllRunnables.countDown()
        initLatch.countDown()
        processingDoneLatch.await()
        executor.awaitTermination(1L, TimeUnit.SECONDS)

        then:
        0 * runnable2.run()
        0 * runnable3.run()
        0 * runnable4.run()
        executor.getQueue().size() == 0
    }

    class LatchRunnable implements Runnable {

        CountDownLatch latch

        LatchRunnable(CountDownLatch latch) {
            this.latch = latch
        }

        @Override
        void run() {
            latch.await();
        }
    }


    class WaitingRunnable implements Runnable {

        CountDownLatch signalProcessingDone
        CountDownLatch waitFor

        WaitingRunnable(CountDownLatch signalProcessingDone, CountDownLatch waitFor) {
            this.signalProcessingDone = signalProcessingDone
            this.waitFor = waitFor;
        }

        @Override
        void run() {
            waitFor.await();
            signalProcessingDone.countDown();
        }
    }
}
