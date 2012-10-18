/**
 * 
 */
package pl.polidea.imagecache;

import android.graphics.Bitmap.CompressFormat;

/**
 * Image cache configuration
 * 
 * @author Wojciech Piwonski
 * 
 */
public class CacheConfig {
    private Integer workersNumber;
    private Integer memoryCacheSize;
    private String diskCachePath;
    private Long diskCacheSize;
    private CompressFormat compressFormat;
    private Integer compressQuality;

    public Integer getWorkersNumber() {
        return workersNumber;
    }

    /**
     * Workers number defines how many threads will process cache's tasks
     * simultaneously. Default value is one thread. Small values are
     * recommended, too big can cause OutOfMemory exception during processing
     * tasks, because of decoding many bitmaps at the same time.
     * 
     * @param workersNumber
     *            number of threads
     */
    public void setWorkersNumber(final Integer workersNumber) {
        this.workersNumber = workersNumber;
    }

    /**
     * Returns size of memory cache in bytes
     * 
     * @return size of memory cache
     */
    public Integer getMemoryCacheSize() {
        return memoryCacheSize;
    }

    /**
     * Sets size of memory cache
     * 
     * @param memoryCacheSize
     *            size in bytes
     */
    public void setMemoryCacheSize(final Integer memoryCacheSize) {
        this.memoryCacheSize = memoryCacheSize;
    }

    /**
     * @return path of disk cache
     */
    public String getDiskCachePath() {
        return diskCachePath;
    }

    /**
     * @param diskCachePath
     *            path of disk cache
     */
    public void setDiskCachePath(final String diskCachePath) {
        this.diskCachePath = diskCachePath;
    }

    /**
     * @return size of disk cache in bytes
     */
    public Long getDiskCacheSize() {
        return diskCacheSize;
    }

    /**
     * @param diskCacheSize
     *            size of disk cache in bytes
     */
    public void setDiskCacheSize(final Long diskCacheSize) {
        this.diskCacheSize = diskCacheSize;
    }

    public CompressFormat getCompressFormat() {
        return compressFormat;
    }

    public void setCompressFormat(final CompressFormat compressFormat) {
        this.compressFormat = compressFormat;
    }

    public Integer getCompressQuality() {
        return compressQuality;
    }

    public void setCompressQuality(final Integer compressQuality) {
        this.compressQuality = compressQuality;
    }

}
