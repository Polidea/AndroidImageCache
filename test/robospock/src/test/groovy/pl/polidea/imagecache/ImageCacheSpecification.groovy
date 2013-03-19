package pl.polidea.imagecache

import android.graphics.Bitmap
import com.xtremelabs.robolectric.Robolectric
import com.xtremelabs.robolectric.shadows.ShadowLog
import java.util.concurrent.ExecutorService
import pl.polidea.robospock.RoboSpecification
import pl.polidea.robospock.UseShadows
import pl.polidea.thridparty.DiskCache
import pl.polidea.utils.Utils
import shadows.MyShadowActivityManager

@UseShadows(MyShadowActivityManager)
class ImageCacheSpecification extends RoboSpecification {

    ImageCache cache
    def mockBitmap
    def mockMemCache
    def mockDiskCache

    OnCacheResultListener mockListener

    def "setup"() {
        cache = new ImageCache(CacheConfig.buildDefault(Robolectric.application))

        mockMemCache = Mock(MemoryCache)
        mockDiskCache = Mock(DiskCache)
        cache.diskCache = mockDiskCache
        cache.memCache = mockMemCache

        mockBitmap = Mock(Bitmap)
        mockBitmap.getRowBytes() >> 10
        mockBitmap.getHeight() >> 10

        mockListener = Mock(OnCacheResultListener)
    }

    def "should create new ImageCache based on config"() {
        given:
        def config = CacheConfig.buildDefault(Robolectric.application)

        when:
        def cache = new ImageCache(config)

        then:
        cache
    }

    def "should be able to put a new bitmap to cache"() {
        when:
        def localCache = new ImageCache(CacheConfig.buildDefault(Robolectric.application))
        localCache.put("aaa", mock(size, size))

        then:
        memSize == localCache.memCache.size()

        where:
        size | memSize
        10   | 100
        20   | 400
    }

    def "should throw an exception when inserting empty key"() {
        when:
        cache.put(key, Mock(Bitmap))

        then:
        thrown(IllegalArgumentException)

        where:
        key << [null, ""]
    }

    def "should throw an exception when inserting empty bitmap"() {
        when:
        cache.put("a", null)

        then:
        thrown(IllegalArgumentException)
    }

    def "should throw an exception when insterting bitmap is recycled"() {
        given:
        mockBitmap.isRecycled() >> true

        when:
        cache.put("a", mockBitmap)

        then:
        thrown(IllegalArgumentException)
    }

    def "should be able to remove a bitmap under key"() {
        given:
        mockMemCache.remove(_) >> mockBitmap

        when:
        def remove = cache.remove("a")

        then:
        remove
    }

    def "should log when removing bitmap from disk cache"() {
        given:
        Utils.setUseLogs(true)
        mockDiskCache.remove(_) >> {
            throw new IOException("a")
        }

        when:
        def remove = cache.remove("a")

        then:
        !remove
        ShadowLog.logs.collect { it.msg }.findAll { it == "Removing bitmap error" }
    }

    def "should return false when removing non existing key"() {
        when:
        def remove = cache.remove("a")

        then:
        !remove
    }

    def "should be able to remove all objects from cache"() {
        given: "put some fake elements to cache"
        cache.put("a", mock(10, 10))
        cache.put("b", mock(10, 10))

        when:
        cache.clear()

        then:
        cache.memCache.size() == 0
        cache.diskCache.getSize() == 0
    }

    def "should throw an exception when getting a bitmap with empty listener"() {
        when:
        cache.get("", null)

        then:
        thrown(IllegalArgumentException)
    }

    def "should call listener when bitmap is present in cache"() {
        given:
        mockMemCache.get(_) >> mockBitmap

        when:
        cache.get("a", mockListener)

        then:
        1 * mockListener.onCacheHit(_, _)
    }

    def "should put new task to deque when bitmap is not present in cache"() {
        given:
        cache.taskExecutor = Mock(ExecutorService)

        when:
        cache.get("aa", mockListener)

        then:
        1 * cache.taskExecutor.submit(_)
    }

    def "should be able to create a CacheTask"() {
        when:
        def task = cache.buildTask("a", "a", mockListener)

        then:
        task
    }

    def "should call cache miss when bitmap is not in cache"() {
        given:
        mockBitmap.isRecycled() >> true
        mockDiskCache.getBitmap(_) >>> [null, mockBitmap]
        and: "create a cache task"
        def task = cache.buildTask("a", "a", mockListener)

        when:
        2.times { task.run() }


        then:
        2 * mockListener.onCacheMiss("a")
    }

    def "should call cache hit when bitmap is available"() {
        given:
        mockDiskCache.getBitmap(_) >> mockBitmap

        and: "create a cache task"
        def task = cache.buildTask("a", "a", mockListener)

        when:
        task.run()

        then:
        1 * mockListener.onCacheHit("a", mockBitmap)
        1 * mockMemCache.put("a", mockBitmap)
    }

    Bitmap mock(int rowBytes, int height) {
        def mock = Mock(Bitmap)
        mock.getRowBytes() >> rowBytes
        mock.getHeight() >> height
        return mock
    }
}
