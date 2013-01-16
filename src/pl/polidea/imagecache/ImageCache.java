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

/**
 * @author Wojciech Piwonski
 * 
 */
public class ImageCache implements IBitmapCache {

    private static final String TAG = ImageCache.class.getSimpleName();

    private static final int DEFAULT_WORKERS_NUMBER = 1;
    private static final CompressFormat DEFAULT_COMPRESS_FORMAT = CompressFormat.PNG;
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
     * @throws IOException
     */
    public ImageCache(final Context context) {
        this(fillEmptyValuesWithDefault(context, new CacheConfig()));
    }

    /**
     * Creates image cache with default parameters stored in config parameter.
     * Empty config's fields will be replaced by default values.
     * 
     * @param context
     * @throws IOException
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
     * @throws IOException
     *             if disk cache creation failed
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
            workers[i].setPriority(Thread.MIN_PRIORITY);
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

    /**
     * Ask ImageCache for given key. If memory cache doesn't have the key it
     * continue operation in other thread asking disk cache for the key. Passing
     * null listener throws IllegalArgumentException because passing the key
     * value is always done by the listener.
     */
    @Override
    public void get(final String key, final OnCacheResultListener onCacheResultListener) {
        final String hashedKey = Utils.sha1(key);
        if (!areWorkersWork) {
            areWorkersWork = true;
            for (final Thread thread : workers) {
                thread.start();
            }
        }

        if (onCacheResultListener == null) {
            throw new IllegalArgumentException("onCacheResult cannot be null");
        }
        final Bitmap bitmap = memCache.get(hashedKey);
        if (bitmap == null) {
            deque.addFirst(new CacheTask(key, hashedKey, onCacheResultListener));
        } else {
            onCacheResultListener.onCacheHit(key, bitmap);
        }

    }

    /**
     * Removes bitmpa under key from memory and disc cache.
     */
    @Override
    public boolean remove(final String key) {
        final String hashedKey = Utils.sha1(key);
        boolean removed = memCache.remove(hashedKey) != null;
        try {
            removed = diskCache.remove(hashedKey) || removed;
        } catch (final IOException e) {
            Utils.log("Removing bitmap error");
        }
        return removed;
    }

    /**
     * Puts bitmap to both memory and disc cache.
     */
    @Override
    public void put(final String key, final Bitmap bitmap) {
        final String hashedKey = Utils.sha1(key);
        memCache.put(hashedKey, bitmap);
        diskCache.put(hashedKey, bitmap);
    }

    @Override
    public void clear() {
        memCache.evictAll();
        diskCache.clearCache();
    }

    public int getWorkersNumber() {
        return workers.length;
    }

    public int getMemoryCacheSize() {
        return memCache.size();
    }

    public int getMemoryCacheMaxSize() {
        return memCache.maxSize();
    }

    public long getDiskCacheSize() {
        return diskCache.getSize();
    }

    public long getDiskCacheMaxSize() {
        return diskCache.getMaxSize();
    }

    public String getDiskCachePath() {
        return diskCache.getDirectory().getPath();
    }

    public CompressFormat getCompressFormat() {
        return diskCache.getCompressFormat();
    }

    public int getCompressQuality() {
        return diskCache.getCompressQuality();
    }

    private class TaskExecutor implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    final CacheTask task = deque.takeFirst();
                    final Bitmap bitmap = diskCache.getBitmap(task.hashedKey);
                    if (bitmap == null || bitmap.isRecycled()) {
                        task.onCacheResultListener.onCacheMiss(task.key);
                    } else {
                        memCache.put(task.hashedKey, bitmap);
                        task.onCacheResultListener.onCacheHit(task.key, bitmap);
                    }
                }
            } catch (final InterruptedException e) {
                Utils.log(e.getMessage());
            }

        }
    }

    private static class CacheTask {
        public String key;
        public OnCacheResultListener onCacheResultListener;
        private final String hashedKey;

        public CacheTask(final String key, final String hashedKey, final OnCacheResultListener onCacheResultListener) {
            this.key = key;
            this.hashedKey = hashedKey;
            this.onCacheResultListener = onCacheResultListener;
        }
    }
}
