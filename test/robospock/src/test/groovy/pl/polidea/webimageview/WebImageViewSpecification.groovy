package pl.polidea.webimageview

import android.graphics.Bitmap
import com.xtremelabs.robolectric.Robolectric
import pl.polidea.robospock.RoboSpecification
import pl.polidea.robospock.UseShadows
import pl.polidea.webimageview.processor.BitmapProcessor
import shadows.HighDensityShadowResources
import shadows.MyShadowActivityManager
import spock.lang.Unroll

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@polidea.pl>
 */
@UseShadows([MyShadowActivityManager, HighDensityShadowResources])
class WebImageViewSpecification extends RoboSpecification {

    WebImageView imageView;

    def setup(){
        imageView = new WebImageView(Robolectric.application);
    }

    @Unroll
    def "should return null drawable on url = #url"() {
        when:
        imageView.setImageURL(url);

        then:
        imageView.getDrawable() == null;

        where:
        url << ["", null]
    }

    def "should not return DefaultBitmapProcessor when disableBitmapProcessor was called"() {
        when:
        imageView.disableBitmapProcessor();

        then:
        imageView.getBitmapProcessor() == BitmapProcessor.DEFAULT
    }

    def "should call listener after requesting for image"() {
        given:
        WebImageView.getCache(Robolectric.application).put("any", Mock(Bitmap.class));
        WebImageView.WebImageListener mock = Mock(WebImageView.WebImageListener.class);

        when: 
        imageView.setImageURL("any", mock);

        then:
        1 * mock.imageSet("any");
    }
}
