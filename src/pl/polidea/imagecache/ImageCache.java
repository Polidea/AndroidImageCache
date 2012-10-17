/**
 * 
 */
package pl.polidea.imagecache;

import java.io.File;
import java.io.IOException;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

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
        this(context, getDefaultDiskCachePath(context), getDefaultDiskCacheSize(context));
    }

    public ImageCache(final Context context, final String path) {
        this(context, path, getDefaultDiskCacheSize(context));
    }

    public ImageCache(final Context context, final long diskCacheSize) {
        this(context, getDefaultDiskCachePath(context), diskCacheSize);
    }

    public ImageCache(final Context context, final int memoryCacheSize) {
        this(context, memoryCacheSize, getDefaultDiskCacheSize(context));
    }

    public ImageCache(final Context context, final String path, final long diskCacheSize) {
        this(getDefaultMemoryCacheSize(context), path, diskCacheSize);
    }

    public ImageCache(final Context context, final int memoryCacheSize, final long diskCacheSize) {
        this(memoryCacheSize, getDefaultDiskCachePath(context), diskCacheSize);
    }

    public ImageCache(final Context context, final int memoryCacheSize, final String path) {
        this(memoryCacheSize, path, getDefaultDiskCacheSize(context));
    }

    public ImageCache(final int memoryCacheSize, final String path, final long diskCacheSize) {
        memCache = new MemoryCache(memoryCacheSize);
        diskCache = new DiskCache(path, diskCacheSize);
        workers = new Thread[WORKERS_NUMBER];
        for (int i = 0; i < WORKERS_NUMBER; ++i) {
            workers[i] = new Thread(new TaskExecutor());
        }
    }

    private static String getDefaultDiskCachePath(final Context context) {
        return context.getCacheDir().getPath() + File.separator + "thumbnails";
    }

    private static int getDefaultMemoryCacheSize(final Context context) {
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
        return alternativeSize;
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
