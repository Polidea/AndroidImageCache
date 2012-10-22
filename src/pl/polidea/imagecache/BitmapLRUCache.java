package pl.polidea.imagecache;

import java.lang.reflect.Field;

import android.graphics.Bitmap;

public class BitmapLRUCache extends LruCache<String, Bitmap> {

    public BitmapLRUCache(final int maxSize) {
        super(maxSize);
        final Field[] fields = getClass().getFields();
        int a = 0;
        a++;

    }

    @Override
    protected int sizeOf(final String key, final Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

}
