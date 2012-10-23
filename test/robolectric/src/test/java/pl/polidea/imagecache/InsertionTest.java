/**
 * 
 */
package pl.polidea.imagecache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import android.graphics.Bitmap;

import com.xtremelabs.robolectric.Robolectric;

/**
 * @author Wojciech Piwonski
 * 
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
        imageCache = new ImageCache(context);
        final String key1 = "key1";
        final String key2 = "key2";

        // when
        final boolean isKey1 = isInCache(key1);
        final boolean isKey2 = isInCache(key2);

        // then
        assertFalse(isKey1);
        assertFalse(isKey2);
    }

    @Test
    public void emptyCacheSizeTest() {
        // when
        imageCache = new ImageCache(context);

        // then
        assertEquals(0, imageCache.getMemoryCacheSize());
    }

}
