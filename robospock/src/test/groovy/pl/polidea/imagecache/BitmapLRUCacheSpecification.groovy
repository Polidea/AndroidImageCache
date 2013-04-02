package pl.polidea.imagecache

import android.graphics.Bitmap
import pl.polidea.robospock.RoboSpecification

class BitmapLRUCacheSpecification extends RoboSpecification {


    def "test creating a BitmapLRUCache"() {
        when:
        def bitmapLRUCache = new BitmapLRUCache(20)

        then:
        bitmapLRUCache.maxSize() == 20
    }

    def "creating BitmapLRUCache with size less than zero should throw exception"() {
        when:
        new BitmapLRUCache(-1)

        then:
        thrown(IllegalArgumentException)
    }

    def "check if cache calculates good item size"() {
        given:
        def cache = new BitmapLRUCache(20)
        def bitmapMock = Mock(Bitmap)
        bitmapMock.getRowBytes() >> 10
        bitmapMock.getHeight() >> 12

        when:
        def sizeOf = cache.sizeOf("", bitmapMock)

        then:
        sizeOf == 120
    }
}
