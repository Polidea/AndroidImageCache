/**
 *
 */
package pl.polidea.webimageview;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import android.graphics.Bitmap;
import com.xtremelabs.robolectric.Robolectric;
import junit.framework.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import pl.polidea.imagecache.ImageCacheTest;
import pl.polidea.webimageview.WebImageView.WebImageListener;

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

    @Test
    public void testActionOnListenerAfterRequestingForImage() {
        // given
        final WebImageView imageView = new WebImageView(Robolectric.application);
        WebImageView.getCache(Robolectric.application).put("any", Mockito.mock(Bitmap.class));
        final WebImageListener mock = Mockito.mock(WebImageListener.class);

        // when
        imageView.setImageURL("any", mock);

        // then
        verify(mock, times(1)).imageSet("any");
    }

}
