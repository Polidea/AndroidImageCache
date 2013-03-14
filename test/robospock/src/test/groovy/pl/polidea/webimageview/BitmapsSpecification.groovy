package pl.polidea.webimageview

import android.graphics.BitmapFactory
import com.xtremelabs.robolectric.shadows.ShadowBitmapFactory
import pl.polidea.robospock.RoboSpecification
import pl.polidea.robospock.UseShadows
import shadows.MyShadowBitmap
import shadows.MyShadowBitmapFactory

@UseShadows([MyShadowBitmap, MyShadowBitmapFactory])
class BitmapsSpecification extends RoboSpecification {

    def name = "testBitmapName"
    def file = new File(name)

    def setup() {
        MyShadowBitmap.shouldThrowException = false
        MyShadowBitmapFactory.shouldThrowException = false
        file.createNewFile()
    }

    def cleanup() {
        file.delete()
        ShadowBitmapFactory.reset()
    }

    def "should throw exception when creating bitmaps with non existing file"() {
        when:
        def bitmaps = new Bitmaps(value)

        then:
        thrown(IllegalArgumentException)

        where:
        value << [null, "asdasdasd"]
    }


    def "should generate bitmap from file name"() {
        given: "provide bitmap"
        ShadowBitmapFactory.provideWidthAndHeightHints(name, 36, 37)
        def bitmaps = new Bitmaps(name)

        when:
        def bitmap = bitmaps.generateBitmap()

        then:
        bitmap
        bitmap.getWidth() == 36
        bitmap.getHeight() == 37
    }

    def "should generate bitmap scaled by width"() {
        given: "provide bitmap"
        ShadowBitmapFactory.provideWidthAndHeightHints(name, 1000, 800)
        def bitmaps = new Bitmaps(name)

        when:
        def bitmap = bitmaps.generateScaledWidthBitmap(320)

        then:
        bitmap
        bitmap.getWidth() == 320
        bitmap.getHeight() == 256
    }

    def "should generate bitmap scaled by height"() {
        given: "provide bitmap"
        ShadowBitmapFactory.provideWidthAndHeightHints(name, 1000, 800)
        def bitmaps = new Bitmaps(name)

        when:
        def bitmap = bitmaps.generateScaledHeightBitmap(256)

        then:
        bitmap
        bitmap.getWidth() == 320
        bitmap.getHeight() == 256
    }

    def "should generate bitmap scaled both width and height"() {
        given: "provide bitmap"
        ShadowBitmapFactory.provideWidthAndHeightHints(name, 36, 37)
        def bitmaps = new Bitmaps(name)

        when:
        def bitmap = bitmaps.generateBitmap(20, 20)

        then:
        bitmap
        bitmap.getWidth() == 19 // casing from int to float than to int
        bitmap.getHeight() == 20
    }

    def "should generate scaled bitmap"() {
        ShadowBitmapFactory.provideWidthAndHeightHints(name, 36, 37)
        def bitmaps = new Bitmaps(name)

        when:
        def bitmap = bitmaps.getBitmap(bitmaps.getOptions(), 20, 20)

        then:
        bitmap
        bitmap.getWidth() == 19 // casing from int to float then to int
        bitmap.getHeight() == 20
    }

    def "should throw exception when decode file lacks of memory"() {
        given:
        ShadowBitmapFactory.provideWidthAndHeightHints(name, 36, 37)
        def bitmaps = new Bitmaps(name)
        and: "simulate out of memory"
        MyShadowBitmapFactory.shouldThrowException = true
        and: "create bitmap options"
        def options = new BitmapFactory.Options();
        options.outWidth = 36
        options.outHeight = 37

        when:
        bitmaps.getBitmap(options, 20, 20)

        then:
        thrown(BitmapDecodeException)
    }

    def "should throw exception when created scale bitmap"() {
        given:
        ShadowBitmapFactory.provideWidthAndHeightHints(name, 36, 37)
        def bitmaps = new Bitmaps(name)
        and: "simulate out of memory"
        MyShadowBitmap.shouldThrowException = true
        and: "create bitmap options"
        def options = new BitmapFactory.Options();
        options.outWidth = 36
        options.outHeight = 37

        when:
        bitmaps.getBitmap(options, 20, 20)

        then:
        thrown(BitmapDecodeException)
    }
}
