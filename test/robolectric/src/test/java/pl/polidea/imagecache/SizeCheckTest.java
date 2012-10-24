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
    public void emptyCacheTest() {
        // when
        imageCache = new ImageCache(context);

        // then
        assertEquals(0, imageCache.getMemoryCacheSize());
    }

    @Test
    public void oneBitmapTest() {
        // given
        imageCache = new ImageCache(context);
        final int sizeInKb = 512;
        final Bitmap bitmap1 = getBitmap(sizeInKb);
        final String key1 = "key1";

        // when
        imageCache.put(key1, bitmap1);

        // then
        assertEquals(sizeInKb * KB, imageCache.getMemoryCacheSize());
    }

    @Test
    public void twoBitmapsTest() {
        // given
        imageCache = new ImageCache(context);
        final Bitmap bitmap1 = getBitmap(512);
        final String key1 = "key1";
        final Bitmap bitmap2 = getBitmap(1024);
        final String key2 = "key2";

        // when
        imageCache.put(key1, bitmap1);
        imageCache.put(key2, bitmap2);

        // then
        assertEquals(1536 * KB, imageCache.getMemoryCacheSize());
    }

    @Test
    public void removeBitmapTest() {
        // given
        imageCache = new ImageCache(context);
        final Bitmap bitmap1 = getBitmap(512);
        final String key1 = "key1";
        final Bitmap bitmap2 = getBitmap(1024);
        final String key2 = "key2";
        imageCache.put(key1, bitmap1);
        imageCache.put(key2, bitmap2);

        // when
        imageCache.remove(key1);

        // then
        assertEquals(1024 * KB, imageCache.getMemoryCacheSize());
    }

    @Test
    public void clearCacheTest() {
        // given
        imageCache = new ImageCache(context);
        final Bitmap bitmap1 = getBitmap(512);
        final String key1 = "key1";
        final Bitmap bitmap2 = getBitmap(1024);
        final String key2 = "key2";
        imageCache.put(key1, bitmap1);
        imageCache.put(key2, bitmap2);

        // when
        imageCache.clear();

        // then
        assertEquals(0, imageCache.getMemoryCacheSize());
    }

    @Test
    public void overrideOldBitmapTest() {
        // given
        final CacheConfig config = new CacheConfig();
        config.setMemoryCacheSize(1 * MB);
        imageCache = new ImageCache(context, config);
        final Bitmap bitmap1 = getBitmap(512);
        final String key1 = "key1";
        final Bitmap bitmap2 = getBitmap(768);
        final String key2 = "key2";

        // when
        imageCache.put(key1, bitmap1);
        imageCache.put(key2, bitmap2);

        // then
        assertEquals(768 * KB, imageCache.getMemoryCacheSize());
    }

}
