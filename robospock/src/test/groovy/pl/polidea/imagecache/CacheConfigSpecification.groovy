package pl.polidea.imagecache

import android.content.Context
import android.graphics.Bitmap
import com.xtremelabs.robolectric.Robolectric
import pl.polidea.imagecache.shadows.MyShadowActivityManager
import pl.polidea.robospock.RoboSpecification
import pl.polidea.robospock.UseShadows

@UseShadows(MyShadowActivityManager)
class CacheConfigSpecification extends RoboSpecification {

    def "should fail when null context"() {
        given:
        Context nullContext = null;

        when:
        CacheConfig.buildDefault(nullContext)

        then:
        def caugth = thrown(IllegalArgumentException)
        caugth.message == "Context cannot be null"
    }

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
        config.workersNumber > 0
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
        CacheConfig config = CacheConfig.buildDefault(Robolectric.application)
        config.memoryCacheSize = 1000

        when:
        config = CacheConfig.buildDefault(Robolectric.application, config)

        then:
        config.memoryCacheSize == 1000
    }

    def "should fix negative numbers in config"(){
        given:
        CacheConfig config  = CacheConfig.buildDefault(Robolectric.application);
        config.memoryCacheSize = -1
        config.compressQuality = -1
        config.diskCacheSize = -1
        config.workersNumber = -1

        when:
        config = CacheConfig.buildDefault(Robolectric.application, config)

        then:
        config.memoryCacheSize == 8388608
        config.diskCacheSize == 16777216
        config.workersNumber > 0
        config.compressQuality == 100
    }


}
