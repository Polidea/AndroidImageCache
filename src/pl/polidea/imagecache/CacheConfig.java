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
     * Workers number defines how many threads will process cache tasks
     * simultaneously. Default value is one thread.
     * 
     * @param workersNumber
     *            number of threads
     */
    public void setWorkersNumber(final Integer workersNumber) {
        this.workersNumber = workersNumber;
    }

    public Integer getMemoryCacheSize() {
        return memoryCacheSize;
    }

    public void setMemoryCacheSize(final Integer memoryCacheSize) {
        this.memoryCacheSize = memoryCacheSize;
    }

    public String getDiskCachePath() {
        return diskCachePath;
    }

    public void setDiskCachePath(final String diskCachePath) {
        this.diskCachePath = diskCachePath;
    }

    public Long getDiskCacheSize() {
        return diskCacheSize;
    }

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
