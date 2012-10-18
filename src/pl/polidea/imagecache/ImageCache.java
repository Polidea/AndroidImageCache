/**
 * 
 */
package pl.polidea.imagecache;

import java.io.File;
import java.io.IOException;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

/**
 * @author Wojciech Piwonski
 * 
 */
public class ImageCache implements BitmapCache {

    private static final String TAG = ImageCache.class.getSimpleName();

    private static final int DEFAULT_WORKERS_NUMBER = 1;
    private static final CompressFormat DEFAULT_COMPRESS_FORMAT = CompressFormat.JPEG;
    private static final int DEFAULT_COMPRESS_QUALITY = 100;

    final MemoryCache memCache;
    final DiskCache diskCache;
    private final LinkedBlockingDeque<CacheTask> deque = new LinkedBlockingDeque<CacheTask>();
    private final Thread[] workers;
    private boolean areWorkersWork = false;

    /**
     * Creates image cache with default parameters.
     * 
     * @param context
     */
    public ImageCache(final Context context) {
        this(fillEmptyValuesWithDefault(context, new CacheConfig()));
    }

    /**
     * Creates image cache with default parameters stored in config parameter.
     * Empty config's fields will be replaced by default values.
     * 
     * @param context
     */
    public ImageCache(final Context context, final CacheConfig config) {
        this(fillEmptyValuesWithDefault(context, config));
    }

    /**
     * Creates image cache with parameters stored in config parameter. <br>
     * WARNING! All fields of the config have to be provided! If you want define
     * only some parameters, use constructor
     * {@link ImageCache#ImageCache(Context, CacheConfig)}
     * 
     * @param config
     *            cache configuration
     */
    public ImageCache(final CacheConfig config) {
        checkAllValuesFilled(config);
        memCache = new MemoryCache(config.getMemoryCacheSize());
        diskCache = new DiskCache(config.getDiskCachePath(), config.getDiskCacheSize(), config.getCompressFormat(),
                config.getCompressQuality());
        final int workersNumber = config.getWorkersNumber();
        workers = new Thread[workersNumber];
        for (int i = 0; i < workersNumber; ++i) {
            workers[i] = new Thread(new TaskExecutor());
        }
    }

    private void checkAllValuesFilled(final CacheConfig config) {
        checkConfigNotNull(config);
        if (config.getWorkersNumber() == null || config.getMemoryCacheSize() == null
                || config.getDiskCachePath() == null || config.getDiskCacheSize() == null
                || config.getCompressFormat() == null || config.getCompressQuality() == null) {
            throw new IllegalArgumentException("All config's fields have to be filled");
        }
    }

    /**
     * Fills empty configuration's values with default.
     * 
     * @param context
     * @param config
     *            cache configuration
     */
    private static CacheConfig fillEmptyValuesWithDefault(final Context context, final CacheConfig config) {
        checkConfigNotNull(config);
        if (config.getWorkersNumber() == null) {
            config.setWorkersNumber(DEFAULT_WORKERS_NUMBER);
        }
        if (config.getMemoryCacheSize() == null) {
            config.setMemoryCacheSize(getDefaultMemoryCacheSize(context));
        }
        if (config.getDiskCachePath() == null) {
            config.setDiskCachePath(getDefaultDiskCachePath(context));
        }
        if (config.getDiskCacheSize() == null) {
            config.setDiskCacheSize(getDefaultDiskCacheSize(context));
        }
        if (config.getCompressFormat() == null) {
            config.setCompressFormat(DEFAULT_COMPRESS_FORMAT);
        }
        if (config.getCompressQuality() == null) {
            config.setCompressQuality(DEFAULT_COMPRESS_QUALITY);
        }
        return config;
    }

    private static void checkConfigNotNull(final CacheConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Config cannot be null");
        }
    }

    private static String getDefaultDiskCachePath(final Context context) {
        return context.getCacheDir().getPath() + File.separator + "bitmaps";
    }

    private static int getDefaultMemoryCacheSize(final Context context) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final int memClass = activityManager.getMemoryClass();
        final int size = 1024 * 1024 * memClass / 8;
        Log.i(TAG, "Device memory class: " + memClass + " LRUCache size: " + size / 1000 + " kB");
        return size;
    }

    private static long getDefaultDiskCacheSize(final Context context) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        final int memClass = activityManager.getMemoryClass();
        final long size = 1024 * 1024 * memClass / 4;
        Log.i(TAG, "Device memory class: " + memClass + " DiskLruCache size: " + size / 1000 + " kB");
        return size;
    }

    @Override
    public void get(final String key, final OnCacheResultListener onCacheResultListener) {
        if (!areWorkersWork) {
            areWorkersWork = true;
            for (final Thread thread : workers) {
                thread.start();
            }
        }

        if (onCacheResultListener == null) {
            throw new IllegalArgumentException("onCacheResult cannot be null");
        }
        final Bitmap bitmap = memCache.get(key);
        if (bitmap != null) {
            onCacheResultListener.onCacheHit(key, bitmap);
        } else {
            deque.addFirst(new CacheTask(key, onCacheResultListener));
        }

    }

    @Override
    public boolean remove(final String key) {
        boolean removed = memCache.remove(key) != null;
        try {
            removed = removed || diskCache.remove(key);
        } catch (final IOException e) {
            Log.e(TAG, "Removing bitmap error");
        }
        return removed;
    }

    @Override
    public void put(final String key, final Bitmap bitmap) {
        memCache.put(key, bitmap);
        diskCache.put(key, bitmap);
    }

    @Override
    public void clear() {
        memCache.evictAll();
        diskCache.clearCache();
    }

    private class CacheTask {
        public String key;
        public OnCacheResultListener onCacheResultListener;

        public CacheTask(final String key, final OnCacheResultListener onCacheResultListener) {
            this.key = key;
            this.onCacheResultListener = onCacheResultListener;
        }
    }

    private class TaskExecutor implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    final CacheTask task = deque.takeFirst();
                    final Bitmap bitmap = diskCache.getBitmap(task.key);
                    if (bitmap == null || bitmap.isRecycled()) {
                        task.onCacheResultListener.onCacheMiss(task.key);
                    } else {
                        memCache.put(task.key, bitmap);
                        task.onCacheResultListener.onCacheHit(task.key, bitmap);
                    }
                }
            } catch (final InterruptedException e) {
                Log.e(TAG, e.getMessage());
            }

        }

    }

}
