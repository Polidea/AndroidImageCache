package pl.polidea.webimageview.net

import pl.polidea.robospock.RoboSpecification

class TaskContainerSpecification extends RoboSpecification {

    def "should allow inserting new elements to container"() {
        given:
        def taskContainer = new TaskContainer()

        when:
        def inserted = taskContainer.addTask("a", Mock(WebCallback))

        then:
        inserted
    }

    def "should allow size equals number of inserted keys"() {
        given:
        def taskContainer = new TaskContainer()
        def mockCallback = Mock(WebCallback)

        when:
        taskContainer.addTask("a", mockCallback)
        taskContainer.addTask("1", mockCallback)
        taskContainer.addTask("s", mockCallback)
        taskContainer.addTask("d", mockCallback)

        then:
        taskContainer.size() == 4
    }

    def "should inserting duplicates don't increase container size"() {
        given:
        def taskContainer = new TaskContainer()
        def mockCallback = Mock(WebCallback)

        when:
        (1..5).each {
            taskContainer.addTask("a", mockCallback)
        }

        then:
        taskContainer.size() == 1
    }

    def "allow to remove set of listeners bound to keys"() {
        given:
        def taskContainer = new TaskContainer()
        def mockCallback = Mock(WebCallback)
        taskContainer.addTask("a", mockCallback)

        when:
        taskContainer.remove("a")

        then:
        taskContainer.size() == 0
    }

    def "should throw exception when inserting null or empty key"() {
        given:
        def taskContainer = new TaskContainer()
        def mockCallback = Mock(WebCallback)

        when:
        taskContainer.addTask(key, mockCallback)

        then:
        thrown(IllegalArgumentException)

        where:
        key << [null, ""]
    }

    def "should calculate number of all callback registered"() {
        given:
        def taskContainer = new TaskContainer()
        def mockCallback = Mock(WebCallback)

        when:
        ('a'..'f').each { taskContainer.addTask(it, mockCallback) }

        then:
        taskContainer.callbackSize() == 6
    }

    def "should call callbacks connected with path "() {
        given:
        def taskContainer = new TaskContainer()

        and: "define mock callbacks"
        def mockCallback = Mock(WebCallback)
        def mockCallback2 = Mock(WebCallback)

        and: "add callbacks under same key"
        taskContainer.addTask("a", mockCallback)
        taskContainer.addTask("a", mockCallback2)

        when:
        taskContainer.performCallbacks("a", null);

        then:
        1 * mockCallback.onWebHit("a", null)
        1 * mockCallback2.onWebHit("a", null)
    }

    def "should not call callbacks when path is not connected"() {
        given:
        def taskContainer = new TaskContainer()

        and: "define mock callbacks"
        def mockCallback = Mock(WebCallback)
        def mockCallback2 = Mock(WebCallback)

        and: "add callbacks under different key"
        taskContainer.addTask("a", mockCallback)
        taskContainer.addTask("b", mockCallback2)

        when:
        taskContainer.performCallbacks("a", null);

        then:
        1 * mockCallback.onWebHit("a", null)
        0 * mockCallback2.onWebHit("a", null)
    }

    def "should not perform callbacks on key which doesn't exist"(){
        given:
        def taskContainer = new TaskContainer()

        and: "define mock callbacks"
        def mockCallback = Mock(WebCallback)

        and: "add callbacks under different key"
        taskContainer.addTask("a", mockCallback)

        when:
        taskContainer.performCallbacks("b", null);

        then:
        0 * mockCallback.onWebHit(_,_)
    }

    def "should call miss callback connected with path"() {
        given:
        def taskContainer = new TaskContainer()

        and: "define mock callbacks"
        def mockCallback = Mock(WebCallback)
        def mockCallback2 = Mock(WebCallback)

        and: "add callbacks under same key"
        taskContainer.addTask("a", mockCallback)
        taskContainer.addTask("a", mockCallback2)

        when:
        taskContainer.performMissCallbacks("a");

        then:
        1 * mockCallback.onWebMiss("a")
        1 * mockCallback2.onWebMiss("a")
    }

    def "should not call miss callback when path is not connected"() {
        given:
        def taskContainer = new TaskContainer()

        and: "define mock callbacks"
        def mockCallback = Mock(WebCallback)
        def mockCallback2 = Mock(WebCallback)

        and: "add callbacks different same key"
        taskContainer.addTask("a", mockCallback)
        taskContainer.addTask("b", mockCallback2)

        when:
        taskContainer.performMissCallbacks("a");

        then:
        1 * mockCallback.onWebMiss("a")
        0 * mockCallback2.onWebMiss(_)
    }

    def "should not perform miss callbacks on key which doesn't exist"(){
        given:
        def taskContainer = new TaskContainer()

        and: "define mock callbacks"
        def mockCallback = Mock(WebCallback)

        and: "add callbacks under different key"
        taskContainer.addTask("a", mockCallback)

        when:
        taskContainer.performMissCallbacks("b");

        then:
        0 * mockCallback.onWebMiss(_)
    }

}
