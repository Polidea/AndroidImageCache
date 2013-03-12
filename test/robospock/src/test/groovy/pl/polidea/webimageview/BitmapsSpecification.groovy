package pl.polidea.webimageview

import com.xtremelabs.robolectric.shadows.ShadowBitmapFactory
import pl.polidea.robospock.RoboSpecification
import pl.polidea.robospock.UseShadows
import shadows.MyShadowBitmap
import shadows.MyShadowBitmapFactory

@UseShadows([MyShadowBitmap, MyShadowBitmapFactory])
class BitmapsSpecification extends RoboSpecification {

    def name = "testBitmapName"

    def setup() {
        MyShadowBitmap.shouldThrowException = false
        MyShadowBitmapFactory.shouldThrowException = false
    }

    def "should generate bitmap from file name"() {
        given: "provide bitmap"
        def bitmaps = new Bitmaps()
        ShadowBitmapFactory.provideWidthAndHeightHints(name, 36, 37)

        when:
        def bitmap = bitmaps.generateBitmap(name)

        then:
        bitmap
        bitmap.getWidth() == 36
        bitmap.getHeight() == 37
    }

    def "should generate bitmap scaled by width"() {
        given: "provide bitmap"
        def bitmaps = new Bitmaps()
        ShadowBitmapFactory.provideWidthAndHeightHints(name, 1000, 800)

        when:
        def bitmap = bitmaps.generateScaledWidthBitmap(name, 320)

        then:
        bitmap
        bitmap.getWidth() == 320
        bitmap.getHeight() == 256
    }

    def "should generate bitmap scaled by height"() {
        given: "provide bitmap"
        def bitmaps = new Bitmaps()
        ShadowBitmapFactory.provideWidthAndHeightHints(name, 1000, 800)

        when:
        def bitmap = bitmaps.generateScaledHeightBitmap(name, 256)

        then:
        bitmap
        bitmap.getWidth() == 320
        bitmap.getHeight() == 256
    }

    def "should generate bitmap scaled both width and height"() {
        given: "provide bitmap"
        def bitmaps = new Bitmaps()
        ShadowBitmapFactory.provideWidthAndHeightHints(name, 36, 37)

        when:
        def bitmap = bitmaps.generateBitmap(name, 20, 20)

        then:
        bitmap
        bitmap.getWidth() == 19 // casing from int to float than to int
        bitmap.getHeight() == 20
    }

    def "should generate scaled bitmap"() {
        def bitmaps = new Bitmaps()
        ShadowBitmapFactory.provideWidthAndHeightHints(name, 36, 37)

        when:
        def bitmap = bitmaps.getBitmap(name, bitmaps.getOptions(name), 20, 20)

        then:
        bitmap
        bitmap.getWidth() == 19 // casing from int to float then to int
        bitmap.getHeight() == 20
    }

    def "should throw exception when decode file lacks of memory"() {
        given:
        def bitmaps = new Bitmaps()
        ShadowBitmapFactory.provideWidthAndHeightHints(name, 36, 37)
        and: "simulate out of memory"
        MyShadowBitmapFactory.shouldThrowException = true

        when:
        bitmaps.getBitmap(name, bitmaps.getOptions(name), 20, 20)

        then:
        thrown(OutOfMemoryError)
    }

    def "should throw exception when created scale bitmap"() {
        given:
        def bitmaps = new Bitmaps()
        ShadowBitmapFactory.provideWidthAndHeightHints(name, 36, 37)
        and: "simulate out of memory"
        MyShadowBitmap.shouldThrowException = true

        when:
        bitmaps.getBitmap(name, bitmaps.getOptions(name), 20, 20)

        then:
        thrown(OutOfMemoryError)
    }
}
