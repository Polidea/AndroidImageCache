/**
 * 
 */
package pl.polidea.imagecache;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import com.xtremelabs.robolectric.Robolectric;

/**
 * @author Wojciech Piwonski
 * 
 */
@RunWith(ImageCacheTestRunner.class)
public class InsertionTest {

    private static final int KB = 1024;

    private Context context;
    private ImageCache imageCache;

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

    private boolean isInCache(final String bitmapKey) throws InterruptedException {
        final Result result = new Result();
        final CountDownLatch latch = new CountDownLatch(1);
        imageCache.get(bitmapKey, new OnCacheResultListener() {

            @Override
            public void onCacheMiss(final String key) {
                result.value = false;
                latch.countDown();
            }

            @Override
            public void onCacheHit(final String key, final Bitmap bitmap) {
                result.value = true;
                latch.countDown();
            }
        });
        latch.await();
        return result.value;
    }

    private boolean isInCache(final String bitmapKey, final OnCacheResultListener listener) throws InterruptedException {
        final Result result = new Result();
        imageCache.get(bitmapKey, listener);
        return result.value;
    }

    private class Result {
        public boolean value = false;
    }

    /**
     * @param size
     *            size in KB
     * @return
     */
    private Bitmap getBitmap(final int size) {
        final int height = KB / 4;
        final int width = size * KB / height;
        return getBitmap(width, height);
    }

    private Bitmap getBitmap(final int width, final int height) {
        final Bitmap b = Mockito.mock(Bitmap.class);
        Mockito.when(b.getHeight()).thenReturn(height);
        Mockito.when(b.getWidth()).thenReturn(width);
        Mockito.when(b.getRowBytes()).thenReturn(width * 4);
        Mockito.when(b.compress(Mockito.any(CompressFormat.class), Mockito.anyInt(), Mockito.any(OutputStream.class)))
                .thenReturn(true);
        return b;
    }
}
