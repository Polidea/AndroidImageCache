/**
 * 
 */
package pl.polidea.imagecache;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * @author Wojciech Piwonski
 * 
 */
public class ImageCache implements BitmapCache {

    private static final String TAG = ImageCache.class.getSimpleName();

    private static final int WORKERS_NUMBER = 1;

    MemoryCache memCache;
    DiskCache diskCache;
    private final LinkedBlockingDeque<CacheTask> deque = new LinkedBlockingDeque<CacheTask>();
    private final Thread[] workers;
    private boolean areWorkersWork = false;

    public ImageCache(final Context context) {
        memCache = new MemoryCache(context);
        diskCache = new DiskCache(context);
        workers = new Thread[WORKERS_NUMBER];
        for (int i = 0; i < WORKERS_NUMBER; ++i) {
            workers[i] = new Thread(new TaskExecutor());
        }
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
