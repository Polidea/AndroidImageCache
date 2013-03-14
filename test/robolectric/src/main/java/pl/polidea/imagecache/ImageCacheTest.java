/**
 *
 */
package pl.polidea.imagecache;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import java.io.File;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

/**
 * @author Wojciech Piwonski
 */
@RunWith(ImageCacheTestRunner.class)
public abstract class ImageCacheTest {

    protected static final int KB = 1024;
    protected static final int MB = 1024 * KB;
    protected static final int DEFAULT_WORKERS_NUMBER = 1;
    protected static final CompressFormat DEFAULT_COMPRESS_FORMAT = CompressFormat.PNG;
    protected static final int DEFAULT_COMPRESS_QUALITY = 100;
    private static final int BYTES_PER_PIXEL = 4;
    protected Context context;
    protected ImageCache imageCache;

    protected CacheConfig prepareConfig(final Integer workersNumber, final Integer memoryCacheSize,
                                        final String diskCachePath, final Long diskCacheSize, final CompressFormat compressFormat,
                                        final Integer compressQuality) {
        final CacheConfig config = new CacheConfig();
        if (workersNumber != null) {
            config.workersNumber = workersNumber;
        }
        if (memoryCacheSize != null) {
            config.memoryCacheSize = memoryCacheSize;
        }
        if (diskCacheSize != null) {
            config.diskCacheSize = diskCacheSize;
        }
        if (diskCachePath != null) {
            config.diskCachePath = diskCachePath;
        }
        if (compressFormat != null) {
            config.compressFormat = compressFormat;
        }
        if (compressQuality != null) {
            config.compressQuality = compressQuality;
        }
        return config;
    }

    protected int getMemoryClass() {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        return activityManager.getMemoryClass();
    }

    protected String getDefaultCachePath() {
        return context.getCacheDir().getPath() + File.separator + "bitmaps";
    }

    protected int getDefaultDiskSize(final int memoryClass) {
        return memoryClass * MB / 4;
    }

    protected int getDefaultMemorySize(final int memoryClass) {
        return memoryClass * MB / 8;
    }

    protected boolean isInCache(final String bitmapKey) throws InterruptedException {
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

    /**
     * @param size size in KB
     * @return
     */
    protected Bitmap getBitmap(final int size) {
        final int height = 256;
        final int width = size * KB / height / BYTES_PER_PIXEL;
        return getBitmap(width, height);
    }

    protected Bitmap getBitmap(final int width, final int height) {
        final Bitmap b = Mockito.mock(Bitmap.class);
        Mockito.when(b.getHeight()).thenReturn(height);
        Mockito.when(b.getWidth()).thenReturn(width);
        Mockito.when(b.getRowBytes()).thenReturn(width * BYTES_PER_PIXEL);
        Mockito.when(b.compress(Mockito.any(CompressFormat.class), Mockito.anyInt(), Mockito.any(OutputStream.class)))
                .thenReturn(true);
        return b;
    }

    protected class Result {
        public boolean value = false;
    }

}
