package pl.polidea.imagecache

import com.xtremelabs.robolectric.Robolectric
import pl.polidea.imagecache.shadows.MyShadowActivityManager
import pl.polidea.robospock.RoboSpecification
import pl.polidea.robospock.UseShadows

@UseShadows(MyShadowActivityManager)
class StaticImageCacheFactorySpecification extends RoboSpecification {

    StaticCachedImageCacheFactory factory

    def "setup"() {
        factory = new StaticCachedImageCacheFactory();
    }

    def "should return cached instance"() {
        when:
        def instance1 = factory.create(Robolectric.application)
        def instance2 = factory.create(Robolectric.application)

        then:
        instance1 == instance2
    }
}
