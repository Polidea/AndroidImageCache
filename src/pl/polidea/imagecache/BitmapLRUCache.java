package pl.polidea.imagecache;

import android.graphics.Bitmap;

/**
 * The Class BitmapLRUCache.
 */
public class BitmapLRUCache extends LruCache<String, Bitmap> {

    public BitmapLRUCache(final int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(final String key, final Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

}
