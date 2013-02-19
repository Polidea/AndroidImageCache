/**
 *
 */
package pl.polidea.imagecache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.text.TextUtils;
import pl.polidea.thridparty.DiskCache;
import pl.polidea.utils.StackPoolExecutor;
import pl.polidea.utils.Utils;

import java.io.IOException;
import java.util.concurrent.ExecutorService;


public class ImageCache {

    MemoryCache memCache;
    DiskCache diskCache;
    ExecutorService taskExecutor;

    public ImageCache(final Context context) {
        this(CacheConfig.buildDefault(context));
    }

    public ImageCache(final Context context, final CacheConfig config) {
        this(CacheConfig.buildDefault(context, config));
    }

    public ImageCache(final CacheConfig config) {
        // XXX: this is done in UI thread !
        checkAllValuesFilled(config);
        memCache = new MemoryCache(config.memoryCacheSize);
        diskCache = new DiskCache(config.diskCachePath, config.diskCacheSize, config.compressFormat,
                config.compressQuality);

        taskExecutor = new StackPoolExecutor(config.workersNumber);
    }

    private static void checkConfigNotNull(final CacheConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Config cannot be null");
        }
    }

    void checkAllValuesFilled(final CacheConfig config) {
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

        if (onCacheResultListener == null) {
            throw new IllegalArgumentException("onCacheResult cannot be null");
        }
        final Bitmap bitmap = memCache.get(hashedKey);
        if (bitmap == null) {
            taskExecutor.submit(new CacheTask(key, hashedKey, onCacheResultListener));
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
        if (TextUtils.isEmpty(key) || bitmap == null || bitmap.isRecycled())
            throw new IllegalArgumentException("Key is empty either bitmap isn't valid");
        final String hashedKey = Utils.sha1(key);
        memCache.put(hashedKey, bitmap);
        diskCache.put(hashedKey, bitmap);
    }

    public void clear() {
        memCache.evictAll();
        diskCache.clearCache();
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

    class CacheTask implements Runnable {
        String hashedKey;
        String key;
        OnCacheResultListener onCacheResultListener;

        public CacheTask(String key, String hashedKey, OnCacheResultListener onCacheResultListener) {
            this.key = key;
            this.hashedKey = hashedKey;
            this.onCacheResultListener = onCacheResultListener;
        }

        @Override
        public void run() {
            Bitmap bitmap = diskCache.getBitmap(hashedKey);
            if (bitmap == null || bitmap.isRecycled()) {
                onCacheResultListener.onCacheMiss(key);
            } else {
                memCache.put(hashedKey, bitmap);
                onCacheResultListener.onCacheHit(key, bitmap);
            }
        }
    }

}
