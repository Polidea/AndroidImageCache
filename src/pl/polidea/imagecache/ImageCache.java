/**
 *
 */
package pl.polidea.imagecache;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import pl.polidea.imagecache.thridparty.DiskCache;
import pl.polidea.imagecache.thridparty.LinkedBlockingDeque;

import java.io.File;
import java.io.IOException;

/**
 * @author Wojciech Piwonski
 */
public class ImageCache  {


    final MemoryCache memCache;
    final DiskCache diskCache;
    private final LinkedBlockingDeque<CacheTask> deque = new LinkedBlockingDeque();
    private final Thread[] workers;
    private boolean areWorkersWork = false;

    public ImageCache(final Context context) {
        this(fillEmptyValuesWithDefault(context, new CacheConfig()));
    }

    public ImageCache(final Context context, final CacheConfig config) {
        this(fillEmptyValuesWithDefault(context, config));
    }

    public ImageCache(final CacheConfig config) {
        checkAllValuesFilled(config);
        memCache = new MemoryCache(config.memoryCacheSize);
        diskCache = new DiskCache(config.diskCachePath, config.diskCacheSize, config.compressFormat,
                config.compressQuality);
        final int workersNumber = config.workersNumber;
        workers = new Thread[workersNumber];
        for (int i = 0; i < workersNumber; ++i) {
            workers[i] = new Thread(new TaskExecutor());
            workers[i].setPriority(Thread.MIN_PRIORITY);
        }
    }

    private static CacheConfig fillEmptyValuesWithDefault(final Context context, final CacheConfig config) {
        checkConfigNotNull(config);
        return CacheConfig.buildDefault(context, config);
    }

    private static void checkConfigNotNull(final CacheConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Config cannot be null");
        }
    }



    private void checkAllValuesFilled(final CacheConfig config) {
        checkConfigNotNull(config);
        if (config.workersNumber == null || config.memoryCacheSize == null
                || config.diskCachePath == null || config.diskCacheSize == null
                || config.compressFormat == null || config.compressQuality == null) {
            throw new IllegalArgumentException("All config's fields have to be filled");
        }
    }

    /**
     * Ask ImageCache for given key. If memory cache doesn't have the key it
     * continue operation in other thread asking disk cache for the key. Passing
     * null listener throws IllegalArgumentException because passing the key
     * value is always done by the listener.
     */
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
    public void put(final String key, final Bitmap bitmap) {
        final String hashedKey = Utils.sha1(key);
        memCache.put(hashedKey, bitmap);
        diskCache.put(hashedKey, bitmap);
    }

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

    private static class CacheTask {
        private final String hashedKey;
        public String key;
        public OnCacheResultListener onCacheResultListener;

        public CacheTask(final String key, final String hashedKey, final OnCacheResultListener onCacheResultListener) {
            this.key = key;
            this.hashedKey = hashedKey;
            this.onCacheResultListener = onCacheResultListener;
        }
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
}
