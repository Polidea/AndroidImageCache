package pl.polidea.imagecache

import android.graphics.Bitmap
import com.xtremelabs.robolectric.Robolectric
import com.xtremelabs.robolectric.shadows.ShadowLog
import pl.polidea.robospock.RoboSpecification
import pl.polidea.robospock.UseShadows
import pl.polidea.thridparty.DiskCache
import pl.polidea.utils.Utils
import shadows.MyShadowActivityManager

import java.util.concurrent.ExecutorService

@UseShadows(MyShadowActivityManager)
class ImageCacheSpecification extends RoboSpecification {

    def "should create new ImageCache based on Context"() {
        when:
        def cache = new ImageCache(Robolectric.application)

        then:
        cache
    }

    def "should create new ImageCache based on context nad config"() {
        given:
        def config = new CacheConfig()
        config.memoryCacheSize = -1

        when:
        def cache = new ImageCache(Robolectric.application, config)

        then:
        cache
    }

    def "should throw an exception when null config is passed to constructor"() {
        when:
        new ImageCache((CacheConfig) null)

        then:
        thrown(IllegalArgumentException)
    }

    def "should throw an exception when config with null values is passed to constructor"() {
        given:
        def config = new CacheConfig()

        when:
        new ImageCache(config)

        then:
        thrown(IllegalArgumentException)
    }

    def "should be able to put a new bitmap to cache"() {
        given:
        def cache = new ImageCache(Robolectric.application);

        when:
        cache.put("aaa", mock(size, size))

        then:
        memSize == cache.memCache.size()

        where:
        size | memSize
        10   | 100
        20   | 400
    }

    def "should throw an exception when inserting empty key"() {
        given:
        def cache = new ImageCache(Robolectric.application);

        when:
        cache.put(key, Mock(Bitmap))

        then:
        thrown(IllegalArgumentException)

        where:
        key << [null, ""]
    }

    def "should throw an exception when inserting empty bitmap"() {
        given:
        def cache = new ImageCache(Robolectric.application)

        when:
        cache.put("a", null)

        then:
        thrown(IllegalArgumentException)
    }

    def "should throw an exception when insterting bitmap is recycled"() {
        given:
        def cache = new ImageCache(Robolectric.application)
        def recycledBitmapMock = Mock(Bitmap)
        recycledBitmapMock.isRecycled() >> true

        when:
        cache.put("a", recycledBitmapMock)

        then:
        thrown(IllegalArgumentException)
    }

    def "should be able to remove a bitmap under key"() {
        given:
        def cache = new ImageCache(Robolectric.application)
        cache.put("a", mock(10, 10))

        when:
        def remove = cache.remove("a")

        then:
        remove
    }

    def "should log when removing bitmap from disk cache"() {
        given:
        Utils.setUseLogs(true)
        def cache = new ImageCache(Robolectric.application)
        and: "mock the disk cache"
        def diskCacheMock = Mock(DiskCache)
        diskCacheMock.remove(_) >> {
            throw new IOException("a")
        }
        cache.diskCache = diskCacheMock

        when:
        def remove = cache.remove("a")

        then:
        !remove
        ShadowLog.logs.collect { it.msg }.findAll { it == "Removing bitmap error" }
    }

    def "should return false when removing non existing key"() {
        given:
        def cache = new ImageCache(Robolectric.application)

        when:
        def remove = cache.remove("a")

        then:
        !remove
    }

    def "should be able to remove all objects from cache"() {
        given:
        def cache = new ImageCache(Robolectric.application)
        and: "put some fake elements to cache"
        cache.put("a", mock(10, 10))
        cache.put("b", mock(10, 10))

        when:
        cache.clear()

        then:
        cache.memCache.size() == 0
        cache.diskCache.getSize() == 0
    }

    def "should throw an exception when getting a bitmap with empty listener"() {
        given:
        def cache = new ImageCache(Robolectric.application)

        when:
        cache.get("", null)

        then:
        thrown(IllegalArgumentException)
    }

    def "should call listener when bitmap is present in cache"() {
        given:
        def cache = new ImageCache(Robolectric.application)
        def listenerMock = Mock(OnCacheResultListener)
        and: "adding some bitmap to cache"
        cache.put("a", mock(10, 10))

        when:
        cache.get("a", listenerMock)

        then:
        1 * listenerMock.onCacheHit(_, _)
    }

    def "should put new task to deque when bitmap is not present in cache"() {
        given:
        def cache = new ImageCache(Robolectric.application)
        and: " mock the executor"
        cache.taskExecutor = Mock(ExecutorService)

        when:
        cache.get("aa", Mock(OnCacheResultListener))

        then:
        1 * cache.taskExecutor.submit(_)
    }






    Bitmap mock(int rowBytes, int height) {
        def mock = Mock(Bitmap)
        mock.getRowBytes() >> rowBytes
        mock.getHeight() >> height
        return mock;
    }

}
