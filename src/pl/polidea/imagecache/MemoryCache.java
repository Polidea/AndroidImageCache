package pl.polidea.imagecache;

import java.util.Map;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class MemoryCache {

    private static final String TAG = MemoryCache.class.getSimpleName();

    private final BitmapLRUCache cache;

    public MemoryCache(final Context context) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final int memClass = activityManager.getMemoryClass();
        final int size = 1024 * 1024 * memClass / 8;
        Log.i(TAG, "Device memory class: " + memClass + " LRUCache size: " + size / 1000 + " kB");
        final Display display = windowManager.getDefaultDisplay();
        final int height = display.getHeight();
        final int width = display.getWidth();
        final int fullScreenSize = height * width * 4;
        final int alternativeSize = (int) (fullScreenSize * 3.5);
        Log.i(TAG, "LRUCache alternative size: " + alternativeSize / 1000 + " kB");
        cache = new BitmapLRUCache(alternativeSize);
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
        cache.put(key, value);
        Log.i(TAG, "Inserting " + key + " into LRU Cache Bitmap with size: " + value.getRowBytes() * value.getHeight()
                + "B " + " witdth:" + value.getWidth() + "\theight: " + value.getHeight() + " Cache size: " + size()
                / 1000 + " KB");
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
