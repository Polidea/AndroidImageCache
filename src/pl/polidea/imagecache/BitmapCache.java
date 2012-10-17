/**
 * 
 */
package pl.polidea.imagecache;

import android.graphics.Bitmap;

/**
 * @author Wojciech Piwonski
 * 
 */
public interface BitmapCache {

    void get(String key, OnCacheResultListener onCacheResultListener);

    boolean remove(String key);

    void put(String key, Bitmap bitmap);

    void clear();
}
