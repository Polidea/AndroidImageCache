package pl.polidea.imagecache

import android.graphics.Bitmap
import com.xtremelabs.robolectric.shadows.ShadowLog
import pl.polidea.robospock.RoboSpecification
import pl.polidea.utils.Utils

class MemoryCacheSpecification extends RoboSpecification {

    def "should insert a bitmap to cache"() {
        given:
        MemoryCache memoryCache = new MemoryCache(102)
        def mock = mock(10, 10)

        when:
        def put = memoryCache.put("a", mock)

        then:
        put == null
    }

    def "should throw an exception when key is empty"() {
        given:
        MemoryCache memoryCache = new MemoryCache(102)
        def mock = mock(10, 10)

        when:
        memoryCache.put(null, mock)

        then:
        thrown(IllegalArgumentException)
    }

    def "should throw an exception when bitmap is null"() {
        given:
        MemoryCache memoryCache = new MemoryCache(102)

        when:
        memoryCache.put("a", null)

        then:
        thrown(IllegalArgumentException)
    }

    def "should throw an exception when inserted bitmap is bigger than cache"() {
        given:
        MemoryCache memoryCache = new MemoryCache(99)
        def mock = mock(10, 10)

        when:
        def put = memoryCache.put("a", mock)

        then:
        thrown(IllegalArgumentException)
    }

    def "should log event to logcat"(){
        given:
        Utils.setUseLogs(true)
        MemoryCache memoryCache = new MemoryCache(101)
        def mock = mock(10, 10)

        when:
        def put = memoryCache.put("a", mock)

        then:
        ShadowLog.logs
    }

    Bitmap mock(int rowBytes, int height) {
        def mock = Mock(Bitmap)
        mock.getRowBytes() >> rowBytes
        mock.getHeight() >> height
        return mock;
    }
}
