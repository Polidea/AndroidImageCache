/**
 *
 */
package pl.polidea.imagecache;

import android.graphics.Bitmap;
import com.xtremelabs.robolectric.Robolectric;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Wojciech Piwonski
 */
public class InsertionTest extends ImageCacheTest {

    @Before
    public void setup() {
        context = Robolectric.application;
        imageCache = null;
    }

    @Test
    public void insertionTest() throws InterruptedException {
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
        assertTrue(isInCache(key1));
        assertTrue(isInCache(key2));
    }

    @Test
    public void deletionFirstBitmapTest() throws InterruptedException {
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
        assertFalse(isInCache(key1));
        assertTrue(isInCache(key2));
    }

    @Test
    public void deletionLastBitmapTest() throws InterruptedException {
        // given
        imageCache = new ImageCache(context);
        final Bitmap bitmap1 = getBitmap(512);
        final String key1 = "key1";
        final Bitmap bitmap2 = getBitmap(1024);
        final String key2 = "key2";
        imageCache.put(key1, bitmap1);
        imageCache.put(key2, bitmap2);

        // when
        imageCache.remove(key2);

        // then
        assertTrue(isInCache(key1));
        assertFalse(isInCache(key2));
    }

    @Test
    public void deletionAllBitmapsTest() throws InterruptedException {
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
        imageCache.remove(key2);

        // then
        assertFalse(isInCache(key1));
        assertFalse(isInCache(key2));
    }

    @Test
    public void clearCacheTest() throws InterruptedException {
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
        assertFalse(isInCache(key1));
        assertFalse(isInCache(key2));
    }

    @Test
    public void getNotExistingBitmapFailTest() throws InterruptedException {
        // given
        final String key1 = "key1";
        final String key2 = "key2";

        // when
        imageCache = new ImageCache(context);

        // then
        assertFalse(isInCache(key1));
        assertFalse(isInCache(key2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void bitmapExceedingCacheSizeTest() throws InterruptedException {
        // given
        final CacheConfig config = new CacheConfig();
        config.memoryCacheSize = 2 * MB;
        imageCache = new ImageCache(context, config);
        final Bitmap bitmap = getBitmap(3 * 1024);
        final String key = "key";

        // when
        imageCache.put(key, bitmap);

        // then
        // see the annotation
    }

}
