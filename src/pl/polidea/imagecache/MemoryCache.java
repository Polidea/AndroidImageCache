package pl.polidea.imagecache;

import java.util.Map;

import android.graphics.Bitmap;
import android.util.Log;

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

    @Override
    public int hashCode() {
        return cache.hashCode();
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

    public final void put(final String key, final Bitmap value) {
        if (key == null || value == null) {
            return;
        }
        final int size = value.getRowBytes() * value.getHeight();
        if (size > maxSize()) {
            throw new RuntimeException("Tried to put bitmap of size: " + size / 1024
                    + " KB, while maximum memory cache size is: " + maxSize() / 1024 + " KB.");
        }
        cache.put(key, value);
        Log.i(TAG,
                "Inserting " + key + " into LRU Cache Bitmap with size: " + size + "B " + " width:" + value.getWidth()
                        + "\theight: " + value.getHeight() + " Cache size: " + size() / 1000 + " KB");
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
