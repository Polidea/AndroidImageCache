/**
 *
 */
package pl.polidea.imagecache;

import android.graphics.Bitmap.CompressFormat;

/**
 * Image cache configuration.
 *
 * @author Wojciech Piwonski
 */
public class CacheConfig {
    /**
     * Workers number defines how many threads will process cache's tasks
     * simultaneously. Default value is one thread. Small values are
     * recommended, too big can cause OutOfMemory exception during processing
     * tasks, because of decoding many bitmaps at the same time.
     */
    Integer workersNumber;
    Integer memoryCacheSize;
    String diskCachePath;
    Long diskCacheSize;
    CompressFormat compressFormat;
    Integer compressQuality;
}
