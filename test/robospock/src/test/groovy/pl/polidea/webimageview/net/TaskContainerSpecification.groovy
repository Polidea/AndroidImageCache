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

    }

    def "should call callbacks connected with path "() {

    }

    def "should not call callbacks when path is not connected"() {

    }

    def "should call miss callback connected with path"() {

    }

    def "should not call miss callback when path is not connected"(){

    }

}
