package pl.polidea.imagecache;

import android.graphics.Bitmap;
import pl.polidea.imagecache.thridparty.LruCache;

/**
 * Class extends generic LRU cache using Bitmap as a value. Size of each element
 * in Cache is bitmap size.
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
