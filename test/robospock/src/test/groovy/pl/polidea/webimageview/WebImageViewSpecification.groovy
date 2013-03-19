package pl.polidea.webimageview

import android.graphics.Bitmap
import com.xtremelabs.robolectric.Robolectric
import pl.polidea.imagecache.ImageCacheFactory
import pl.polidea.robospock.RoboSpecification
import pl.polidea.robospock.UseShadows
import pl.polidea.webimageview.net.WebClientFactory
import pl.polidea.webimageview.processor.BitmapProcessor
import shadows.HighDensityShadowResources
import shadows.MyShadowActivityManager
import spock.lang.Unroll

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@polidea.pl>
 */
@UseShadows([MyShadowActivityManager, HighDensityShadowResources])
@Unroll
class WebImageViewSpecification extends RoboSpecification {

    WebImageView imageView;

    def setup(){
        imageView = new WebImageView(Robolectric.application);
    }

    def "should not return DefaultBitmapProcessor when disableBitmapProcessor was called"() {
        when:
        imageView.disableBitmapProcessor();

        then:
        imageView.getBitmapProcessor() == BitmapProcessor.DEFAULT
    }

    def "should call listener after requesting for image"() {
        given:
        imageView.imageCache.put("any", Mock(Bitmap.class));
        WebImageListener mock = Mock(WebImageListener.class);

        when: 
        imageView.setImageURL("any", mock);

        then:
        1 * mock.onImageFetchedSuccessfully("any");
    }

    def "should set another image cache factory"(){
        given:
        ImageCacheFactory diffrentImageCacheFactory = Mock(ImageCacheFactory);

        when:
        imageView.setImageCacheFactory(diffrentImageCacheFactory)

        then:
        1 * diffrentImageCacheFactory.create(_)
    }

    def "should set another web client factory cache"(){
        given:
        WebClientFactory diffrentWebClientFactory = Mock(WebClientFactory);

        when:
        imageView.setWebClientFactory(diffrentWebClientFactory)

        then:
        1* diffrentWebClientFactory.create(_)
    }

    def "should throws exception when empty url"(){
        when:
        imageView.setImageURL(emptyUrl)

        then:
        def thrown = thrown(IllegalArgumentException)
        thrown.message == "Image url cannot be empty";

        where:
        emptyUrl << [null, ""];
    }
}
