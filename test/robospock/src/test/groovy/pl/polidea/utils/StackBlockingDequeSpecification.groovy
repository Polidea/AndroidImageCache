package pl.polidea.utils

import spock.lang.Specification

import java.util.concurrent.TimeUnit

class StackBlockingDequeSpecification extends Specification {

    Runnable runnable1 = Mock(Runnable)
    Runnable runnable2 = Mock(Runnable)
    Runnable runnable3 = Mock(Runnable)
    Runnable runnable4 = Mock(Runnable)
    Runnable runnable5 = Mock(Runnable)
    Runnable runnable6 = Mock(Runnable)
    Runnable runnable7 = Mock(Runnable)

    def "should poll return last element"() {
        given:
        def blockingDeque = new StackBlockingDeque()

        when:
        blockingDeque.offer(runnable1)
        blockingDeque.offer(runnable2)


        then:
        blockingDeque.poll() == runnable2
        blockingDeque.size() == 1
    }

    def "inserting duplicated should increase stack only by 1"() {
        given:
        def blockingDeque = new StackBlockingDeque()

        when:
        2.times { blockingDeque.offer(runnable1) }

        then:
        blockingDeque.size() == 1
    }

    def "should poll return last element after 1 millisecond"() {
        given:
        def blockingDeque = new StackBlockingDeque()

        when:
        blockingDeque.offer(runnable1)
        blockingDeque.offer(runnable2)


        then:
        blockingDeque.poll(1, TimeUnit.NANOSECONDS) == runnable2
        blockingDeque.size() == 1
    }

    def "should take return last element"() {
        given:
        def blockingDeque = new StackBlockingDeque()

        when:
        blockingDeque.offer(runnable1)
        blockingDeque.offer(runnable2)


        then:
        blockingDeque.poll() == runnable2
        blockingDeque.size() == 1
    }


    def "should limit number of elements in structure"() {
        given:
        def blockingDeque = new StackBlockingDeque(2)

        when:
        blockingDeque.offer(runnable1)
        blockingDeque.offer(runnable2)
        blockingDeque.offer(runnable3)
        blockingDeque.offer(runnable4)
        blockingDeque.offer(runnable5)
        blockingDeque.offer(runnable6)
        blockingDeque.offer(runnable7)

        then:
        blockingDeque.size() == 2
        blockingDeque.poll() == runnable7
        blockingDeque.poll() == runnable6
    }

}
