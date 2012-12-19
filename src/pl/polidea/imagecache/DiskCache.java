package pl.polidea.imagecache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

import com.jakewharton.DiskLruCache;

/**
 * The Class DiskCache.
 */
public class DiskCache {

    private static final String TAG = DiskCache.class.getSimpleName();
    private static final int APP_VERSION = 1;
    private static final int VALUE_COUNT = 1;

    private final CompressFormat compressFormat;
    private final int compressQuality;
    private DiskLruCache mDiskCache;

    /**
     * Creates disk cache in specified directory and size.
     * 
     * @param path
     *            path of the cache directory
     * @param size
     *            cache size in bytes
     * @throws IOException
     *             if creating disk cache error occured
     */
    public DiskCache(final String path, final long size, final CompressFormat compressFormat, final int compressQuality) {
        this.compressFormat = compressFormat;
        this.compressQuality = compressQuality;
        try {
            mDiskCache = openDiskLruCache(new File(path), APP_VERSION, VALUE_COUNT, size);
        } catch (final IOException e) {
            throw new IllegalArgumentException("Creating disk cache failed", e);
        }
    }

    private DiskLruCache openDiskLruCache(final File directory, final int appVersion, final int valueCount,
            final long size) throws IOException {
        return DiskLruCache.open(directory, appVersion, valueCount, size);
    }

    private boolean writeBitmapToFile(final Bitmap bitmap, final DiskLruCache.Editor editor) throws IOException {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(editor.newOutputStream(0), 1024);
            return bitmap.isRecycled() ? true : bitmap.compress(compressFormat, compressQuality, out);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public void put(final String key, final Bitmap data) {
        if (data == null) {
            Utils.log("null ERROR on: image put on disk cache " + key);
            return;
        }
        DiskLruCache.Editor editor = null;
        try {
            editor = mDiskCache.edit(key);
            if (editor == null) {
                return;
            }

            if (writeBitmapToFile(data, editor)) {
                mDiskCache.flush();
                editor.commit();
                Utils.log("image put on disk cache " + key);
            } else {
                editor.abort();
                Utils.log("abort ERROR on: image put on disk cache " + key);
            }
        } catch (final IOException e) {
            Utils.log("IOException ERROR on: image put on disk cache " + key);
            try {
                if (editor != null) {
                    editor.abort();
                }
            } catch (final IOException ignored) {
                Utils.log("Editor abort error");
            }
        }

        Utils.log("Cache disk current size: " + mDiskCache.size());

    }

    public Bitmap getBitmap(final String key) {

        Bitmap bitmap = null;
        DiskLruCache.Snapshot snapshot = null;
        try {

            snapshot = mDiskCache.get(key);
            if (snapshot == null) {
                return null;
            }
            final InputStream in = snapshot.getInputStream(0);
            if (in != null) {
                final BufferedInputStream buffIn = new BufferedInputStream(in, 1024);
                try {
                    Utils.log("Loading bitmap from disk");
                    bitmap = BitmapFactory.decodeStream(buffIn);
                } catch (final OutOfMemoryError e) {
                    Utils.log(e);
                }
            }
        } catch (final IOException e) {
            Utils.log("Loading bitmap from disk error.");
        } finally {
            if (snapshot != null) {
                snapshot.close();
            }
        }

        Utils.log(bitmap == null ? "" : "image read from disk " + key);

        return bitmap;

    }

    public boolean containsKey(final String key) {

        boolean contained = false;
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = mDiskCache.get(key);
            contained = snapshot != null;
        } catch (final IOException e) {
            Utils.log("Reading disk cache error");
        } finally {
            if (snapshot != null) {
                snapshot.close();
            }
        }

        return contained;

    }

    public boolean remove(final String key) throws IOException {
        return mDiskCache.remove(key);
    }

    public void clearCache() {
        final File directory = getDirectory();
        final long maxSize = getMaxSize();
        try {
            mDiskCache.delete();
            final String message = "disk cache CLEARED";
            Utils.log(message);
        } catch (final IOException e) {
            final File[] listFiles = directory.listFiles();
            deleteFiles(listFiles);
            Utils.log("Clearing disk cache error.", e);
        }
        try {
            mDiskCache = openDiskLruCache(directory, APP_VERSION, VALUE_COUNT, maxSize);
        } catch (final IOException e) {
            Utils.log("Opening disk cache error", e);
        }
    }

    /**
     * @param listFiles
     */
    private void deleteFiles(final File[] listFiles) {
        for (final File file : listFiles) {
            if (file.isDirectory()) {
                deleteFiles(file.listFiles());
                Utils.log("File " + file.getAbsolutePath() + " deleted with success? " + file.delete());
            }
            Utils.log("File " + file.getAbsolutePath() + " deleted with success? " + file.delete());
        }
    }

    public long getSize() {
        return mDiskCache.size();
    }

    public long getMaxSize() {
        return mDiskCache.maxSize();
    }

    public File getDirectory() {
        return mDiskCache.getDirectory();
    }

    public CompressFormat getCompressFormat() {
        return compressFormat;
    }

    public int getCompressQuality() {
        return compressQuality;
    }

}
