/**
 * 
 */
package pl.polidea.imagecache;

import android.graphics.Bitmap;

/**
 * @author Wojciech Piwonski
 * 
 */
public interface OnCacheResultListener {

    void onCacheHit(String key, Bitmap bitmap);

    void onCacheMiss(String key);
}
