package pl.polidea.utils

import com.xtremelabs.robolectric.Robolectric
import pl.polidea.robospock.RoboSpecification
import pl.polidea.robospock.UseShadows
import shadows.HighDensityShadowResources
import shadows.MyShadowActivityManager
import spock.lang.Unroll

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@polidea.pl>
 */
@UseShadows([MyShadowActivityManager, HighDensityShadowResources])
class DimensionCalculatorSpecification extends RoboSpecification {

    @Unroll
    def "should calculate = #expectedValue for input as string = #inputAsString"() {
        when:
        int value = DimensionCalculator.toRoundedPX(Robolectric.application, inputAsString);

        then:
        value == expectedValue;

        where:
        inputAsString | expectedValue
        "40dip"       | 60
        "40dp"        | 60
        "60px"        | 60
    }

    def "should throw exception on invalid unit = #invalidUnit"() {
        when:
        DimensionCalculator.toRoundedPX(Robolectric.application, inputAsString)

        then:
        def e = thrown(IllegalArgumentException)
        e.message == "Unknown dimension unit: " + inputAsString;

        where:
        inputAsString << ["40fg", "40", ""]
    }
}
