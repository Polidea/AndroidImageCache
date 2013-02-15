package pl.polidea.imagecache;

import java.util.Map;

import android.graphics.Bitmap;

/**
 * The Class MemoryCache.
 */
public class MemoryCache {

    private static final String TAG = MemoryCache.class.getSimpleName();

    private final BitmapLRUCache cache;

    public MemoryCache(final int size) {
        cache = new BitmapLRUCache(size);
    }

    public final int createCount() {
        return cache.createCount();
    }

    public final void evictAll() {
        cache.evictAll();
    }

    public final int evictionCount() {
        return cache.evictionCount();
    }

    public final Bitmap get(final String key) {
        return cache.get(key);
    }

    public final int hitCount() {
        return cache.hitCount();
    }

    public final int maxSize() {
        return cache.maxSize();
    }

    public final int missCount() {
        return cache.missCount();
    }

    /**
     * Puts bitmap to cache. Both key and value can't be null. Inserting bitmap
     * bigger than MemoryCache size throw IllegalArgumentException.
     *
     * @param key
     * @param bitmap
     */
    public final Bitmap put(final String key, final Bitmap bitmap) {
        if (key == null || bitmap == null) {
            throw new NullPointerException("key == null || value == null");
        }
        final int size = bitmap.getRowBytes() * bitmap.getHeight();
        if (size > maxSize()) {
            throw new IllegalArgumentException("Tried to put bitmap of size: " + size / 1024
                    + " KB, while maximum memory cache size is: " + maxSize() / 1024 + " KB.");
        }
        Bitmap put = cache.put(key, bitmap);
        Utils.log("Inserting " + key + " into LRU Cache Bitmap with size: " + size + "B " + " width:"
                + bitmap.getWidth() + "\theight: " + bitmap.getHeight() + " Cache size: " + size() / 1000 + " KB");

        return put;
    }

    public final int putCount() {
        return cache.putCount();
    }

    public final Bitmap remove(final String key) {
        return cache.remove(key);
    }

    public final int size() {
        return cache.size();
    }

    public final Map<String, Bitmap> snapshot() {
        return cache.snapshot();
    }

    @Override
    public final String toString() {
        return cache.toString();
    }
}
