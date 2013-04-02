package pl.polidea.imagecache;

import android.graphics.Bitmap;


/**
 * @author Przemysław Jakubczyk <przemyslaw.jakubczyk@pl.polidea.pl>
 */
public interface OnCacheResultListener {

    void onCacheHit(String key, Bitmap bitmap);

    void onCacheMiss(String key);
}
