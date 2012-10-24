/**
 *
 */
package pl.polidea.webimageview;

import junit.framework.Assert;

import org.junit.Test;

import pl.polidea.imagecache.ImageCacheTest;

import com.xtremelabs.robolectric.Robolectric;

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
}
