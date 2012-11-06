/**
 *
 */
package pl.polidea.webimageview;

import static org.junit.Assert.assertFalse;
import junit.framework.Assert;

import org.junit.Test;

import pl.polidea.imagecache.ImageCacheTest;

import com.xtremelabs.robolectric.Robolectric;

/**
 * The Class WebImageTest.
 */
public class WebImageTest extends ImageCacheTest {

    @Test
    public void testNullUrl() {
        // given
        final WebImageView imageView = new WebImageView(Robolectric.application);

        // when
        imageView.setImageURL((String) null);

        // then
        Assert.assertNull(imageView.getDrawable());
    }

    @Test
    public void testEmptyUrl() {
        // given
        final WebImageView imageView = new WebImageView(Robolectric.application);

        // when
        imageView.setImageURL("");

        // then
        Assert.assertNull(imageView.getDrawable());
    }

    @Test
    public void testDisablingDefaultBitmapProcessor() {
        // given
        final WebImageView imageView = new WebImageView(Robolectric.application);

        // when
        imageView.disableBitmapProcessor();

        // then
        assertFalse(imageView.getBitmapProcessor() instanceof DefaultBitmapProcessor);
    }
}
