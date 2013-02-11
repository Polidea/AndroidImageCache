package pl.polidea.imagecache

import android.graphics.Bitmap
import com.xtremelabs.robolectric.Robolectric
import pl.polidea.robospock.RoboSpecification
import pl.polidea.robospock.UseShadows
import shadows.MyShadowActivityManager

/**
 * Created with IntelliJ IDEA.
 * User: rudy
 * Date: 2/11/13
 * Time: 11:06 AM
 * To change this template use File | Settings | File Templates.
 */
@UseShadows(MyShadowActivityManager)
class CacheConfigSpecification extends RoboSpecification {

    def "should create default compress quality"() {
        when:
        CacheConfig config = CacheConfig.buildDefault(Robolectric.application)

        then:
        config.compressQuality == 100
    }

    def "should create default compress format"() {
        when:
        CacheConfig config = CacheConfig.buildDefault(Robolectric.application)

        then:
        config.compressFormat == Bitmap.CompressFormat.PNG
    }

    def "should create default number of working threads"() {
        when:
        CacheConfig config = CacheConfig.buildDefault(Robolectric.application)

        then:
        config.workersNumber == 1
    }

    def "should create default memory cache size"() {
        when:
        CacheConfig config = CacheConfig.buildDefault(Robolectric.application)

        then:
        config.memoryCacheSize == 8388608
    }

    def "should create default disc cache size"() {
        when:
        CacheConfig config = CacheConfig.buildDefault(Robolectric.application)

        then:
        config.diskCacheSize == 16777216
    }

    def "should create default disc cache path"() {
        when:
        CacheConfig config = CacheConfig.buildDefault(Robolectric.application)

        then:
        config.diskCachePath == "/tmp/android-cache/bitmaps"
    }

    def "should allow overriding default configuration"(){
        given:
        CacheConfig config = new CacheConfig()
        config.memoryCacheSize = 1000

        when:
        config = CacheConfig.buildDefault(Robolectric.application, config)

        then:
        config.memoryCacheSize == 1000
    }

}
