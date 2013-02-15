package pl.polidea.imagecache

import com.xtremelabs.robolectric.shadows.ShadowLog
import pl.polidea.robospock.RoboSpecification

class UtilsSpecification extends RoboSpecification {

    def "setup"() {
        Utils.setUseLogs(true);
    }

    def "should generate sha1 checksum "() {
        when:
        def sha1 = Utils.sha1("a")

        then:
        sha1 == "86f7e437faa5a7fce15d1ddcb9eaeaea377667b8"
    }

    def "should log event"() {
        when:
        Utils.log("event")

        then:
        ShadowLog.logs[0].msg == "event"
    }

    def "should logged event has good tag"() {
        when:
        Utils.log("e")

        then:
        ShadowLog.logs[0].tag == "ImageCache"

    }

    def "should log a throwable"() {
        when:
        Utils.log(Mock(Throwable))

        then:
        ShadowLog.logs
    }

    def "should log event with a throwable"() {
        when:
        Utils.log("adsa", Mock(Throwable))

        then:
        ShadowLog.logs
    }

    def "logs should be empty when event triggered"() {
        given:
        Utils.setUseLogs(false)

        when:
        Utils.log("adssa")

        then:
        ShadowLog.logs.empty
    }


}
