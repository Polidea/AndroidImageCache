package pl.polidea.webimageview.net

import com.xtremelabs.robolectric.Robolectric
import pl.polidea.robospock.RoboSpecification

import java.util.concurrent.ExecutorService

class WebClientSpecification extends RoboSpecification {

    private List<IntRange> collect

    def "should be able to create instance"() {
        when:
        def webClient = new WebClient(Robolectric.application)

        then:
        webClient
        webClient.cacheDir.path
    }

    def "should throw an exception when requesting without webCallback"() {
        given:
        def webClient = new WebClient(Robolectric.application)

        when:
        webClient.requestForImage("a", null)

        then:
        thrown(IllegalArgumentException)
    }

    def "should be able to override default web interface"() {
        given:
        def webClient = new WebClient(Robolectric.application)
        and: "create mock web interface"
        def webInterfaceMock = Mock(WebInterface)

        when:
        webClient.setWebInterface(webInterfaceMock)

        then:
        webClient.webInterface == webInterfaceMock
    }

    def "should be able to override default executor service"() {
        given:
        def webClient = new WebClient(Robolectric.application)
        and: "create mock executor service"
        def executorServiceMock = Mock(ExecutorService)

        when:
        webClient.setTaskExecutor(executorServiceMock)

        then:
        webClient.taskExecutor == executorServiceMock
    }

    def "adding new task should trigger executor"() {
        given:
        def webClient = new WebClient(Robolectric.application)
        and: "setup mock executor"
        def executorMock = Mock(ExecutorService)
        webClient.taskExecutor = executorMock

        when:
        webClient.requestForImage("a", Mock(WebCallback))

        then:
        1 * executorMock.submit(_)
    }

    def "adding two same tasks should trigger executor only once"() {
        given:
        def webClient = new WebClient(Robolectric.application)
        and: "setup mock executor"
        def executorMock = Mock(ExecutorService)
        webClient.taskExecutor = executorMock

        when:
        webClient.requestForImage("a", Mock(WebCallback))
        webClient.requestForImage("a", Mock(WebCallback))

        then:
        1 * executorMock.submit(_)
    }

    def "adding a task which already is running shouldn't trigger executor"() {
        given:
        def webClient = new WebClient(Robolectric.application)
        and: "setup mock executor"
        def executorMock = Mock(ExecutorService)
        webClient.taskExecutor = executorMock
        and: "setup mock pending tasks"
        def pendingTasksMock = Mock(TaskContainer)
        webClient.pendingTasks = pendingTasksMock
        pendingTasksMock.addTask(_, _) >> false

        when:
        webClient.requestForImage("a", Mock(WebCallback))

        then:
        0 * executorMock.submit(_)
    }

    def "should be able to create new download task"() {
        given:
        def webClient = new WebClient(Robolectric.application)

        when:
        def task = webClient.buildTask("a")

        then:
        task
    }

    def "should download task trigger web interface"() {
        given:
        def webClient = new WebClient(Robolectric.application)
        and: "setup mock web interface"
        def webInterfaceMock = Mock(WebInterface)
        webClient.webInterface = webInterfaceMock
        and: "create download task"
        def task = webClient.buildTask("a")

        when:
        task.run()

        then:
        1 * webInterfaceMock.execute("a")
    }

    def "should download task trigger callbacks on task container"() {
        given:
        def webClient = new WebClient(Robolectric.application)
        and: "setup mock task container"
        def taskContainerMock = Mock(TaskContainer)
        webClient.pendingTasks = taskContainerMock
        and: "setup mock web interface"
        def webInterfaceMock = Mock(WebInterface)
        webClient.webInterface = webInterfaceMock
        webInterfaceMock.execute(_) >> { new BufferedInputStream(new ByteArrayInputStream(new byte[1])) }
        and: "create download task"
        def task = webClient.buildTask("a")

        when:
        task.run()

        then:
        1 * taskContainerMock.performCallbacks("a", _)
        1 * taskContainerMock.remove("a")
    }

    def "should download task trigger miss callback on Exception"() {
        given:
        def webClient = new WebClient(Robolectric.application)
        and: "setup mock task container"
        def taskContainerMock = Mock(TaskContainer)
        webClient.pendingTasks = taskContainerMock
        and: "setup mock web interface"
        def webInterfaceMock = Mock(WebInterface)
        webClient.webInterface = webInterfaceMock
        webInterfaceMock.execute("a") >> { throw new IOException("error") }
        and: "create download task"
        def task = webClient.buildTask("a")

        when:
        task.run()

        then:
        taskContainerMock.performMissCallbacks("a")
    }

    def "should download task eventually create a file"() {
        given:
        def webClient = new WebClient(Robolectric.application)
        and: "setup mock task container"
        def taskContainerMock = Mock(TaskContainer)
        webClient.pendingTasks = taskContainerMock
        and: "grep called file"
        File file;
        taskContainerMock.performCallbacks(_, _) >> {
            file = it[1]
        }
        and: "setup mock web interface"
        def webInterfaceMock = Mock(WebInterface)
        webClient.webInterface = webInterfaceMock
        webInterfaceMock.execute(_) >> { new BufferedInputStream(new ByteArrayInputStream("aaa" as byte[])) }
        and: "create download task"
        def task = webClient.buildTask("a")

        when:
        task.run()

        then:
        file
    }

    // TODO: well I should find a way to test saveStreamToFile
}
