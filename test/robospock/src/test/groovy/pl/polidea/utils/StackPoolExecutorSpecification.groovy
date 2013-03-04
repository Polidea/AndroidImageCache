package pl.polidea.utils

import spock.lang.Specification
import spock.lang.Timeout

import java.util.concurrent.CountDownLatch
import java.util.concurrent.locks.ReentrantLock

class StackPoolExecutorSpecification extends Specification {

    @Timeout(1)
    def "should run 1 task while pending tasks are limited to 3 element list"() {
        given: 'awaiting task'
        def initLock = new ReentrantLock()
        initLock.lock()

        def waitingTask = { initLock.newCondition().await() } as Runnable

        and: 'simple runnables'
        def processingDoneLatch = new CountDownLatch(3)
        def doneRunnables = [] as Vector

        def runnables = (1..6).collect {
            new Runnable() {
                void run() {
                    processingDoneLatch.countDown()
                    doneRunnables.add(this)
                }

                String toString() { "Task $it" }
            }
        }

        and: 'tested executable'
        StackPoolExecutor executor = new StackPoolExecutor(1, 3)

        and: "submit locked task"
        executor.submit(waitingTask)

        and: "submit rest"
        runnables.each { executor.submit(it) }

        when: 'release waiting task'
        initLock.unlock()

        and: 'wait for the rest to finish'
        processingDoneLatch.await()

        then:
        executor.getQueue().size() == 0
        doneRunnables == runnables[5..3]
    }


    // TODO: test another constructors or failing situations
}