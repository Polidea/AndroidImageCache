/**
 *
 */
package pl.polidea.imagecache;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap.CompressFormat;

import java.io.File;

/**
 * Image cache configuration.
 *
 * @author Wojciech Piwonski
 */
public class CacheConfig {

    public static final int DEFAULT_WORKERS_NUMBER = 1;
    public static final CompressFormat DEFAULT_COMPRESS_FORMAT = CompressFormat.PNG;
    public static final int DEFAULT_COMPRESS_QUALITY = 100;
    /**
     * Workers number defines how many threads will process cache's tasks
     * simultaneously. Default value is one thread. Small values are
     * recommended, too big can cause OutOfMemory exception during processing
     * tasks, because of decoding many bitmaps at the same time.
     */
    Integer workersNumber;
    Integer memoryCacheSize;
    String diskCachePath;
    Long diskCacheSize;
    CompressFormat compressFormat;
    Integer compressQuality;

    public static CacheConfig buildDefault(Context context) {
        return buildDefault(context, null);
    }

    public static CacheConfig buildDefault(Context context, CacheConfig cacheConfig) {
        if (cacheConfig == null) cacheConfig = new CacheConfig();

        if (cacheConfig.workersNumber == null || cacheConfig.workersNumber < 1) {
            cacheConfig.workersNumber = DEFAULT_WORKERS_NUMBER;
        }
        if (cacheConfig.memoryCacheSize == null || cacheConfig.memoryCacheSize < 1) {
            cacheConfig.memoryCacheSize = getDefaultMemoryCacheSize(context);
        }
        if (cacheConfig.diskCachePath == null) {
            cacheConfig.diskCachePath = getDefaultDiskCachePath(context);
        }
        if (cacheConfig.diskCacheSize == null || cacheConfig.diskCacheSize < 1) {
            cacheConfig.diskCacheSize = getDefaultDiskCacheSize(context);
        }
        if (cacheConfig.compressFormat == null) {
            cacheConfig.compressFormat = DEFAULT_COMPRESS_FORMAT;
        }
        if (cacheConfig.compressQuality == null || cacheConfig.compressQuality < 1) {
            cacheConfig.compressQuality = DEFAULT_COMPRESS_QUALITY;
        }

        return cacheConfig;
    }

    private static String getDefaultDiskCachePath(final Context context) {
        return context.getCacheDir().getPath() + File.separator + "bitmaps";
    }

    private static int getDefaultMemoryCacheSize(final Context context) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final int memClass = activityManager.getMemoryClass();
        final int size = 1024 * 1024 * memClass / 8;
        Utils.log("Device memory class: " + memClass + " LRUCache size: " + size / 1000 + " kB");
        return size;
    }

    private static long getDefaultDiskCacheSize(final Context context) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        final int memClass = activityManager.getMemoryClass();
        final long size = 1024 * 1024 * memClass / 4;
        Utils.log("Device memory class: " + memClass + " DiskLruCache size: " + size / 1000 + " kB");
        return size;
    }
}
