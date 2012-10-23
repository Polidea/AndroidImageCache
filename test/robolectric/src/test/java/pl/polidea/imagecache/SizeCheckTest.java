/**
 * 
 */
package pl.polidea.imagecache;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import android.graphics.Bitmap;

import com.xtremelabs.robolectric.Robolectric;

/**
 * @author Wojciech Piwonski
 * 
 */
public class SizeCheckTest extends ImageCacheTest {

    @Before
    public void setup() {
        context = Robolectric.application;
        imageCache = null;
    }

    @Test
    public void emptyCacheSizeTest() {
        // when
        imageCache = new ImageCache(context);

        // then
        assertEquals(0, imageCache.getMemoryCacheSize());
    }

    @Test
    public void oneBitmapCacheSizeTest() {
        // given
        imageCache = new ImageCache(context);
        final Bitmap bitmap1 = getBitmap(512);
        final String key1 = "key1";

        // when
        imageCache.put(key1, bitmap1);

        // then
        assertEquals(512 * KB, imageCache.getMemoryCacheSize());
    }
}
