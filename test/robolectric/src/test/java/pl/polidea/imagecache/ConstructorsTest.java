/**
 * 
 */
package pl.polidea.imagecache;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import android.graphics.Bitmap.CompressFormat;

import com.xtremelabs.robolectric.Robolectric;

/**
 * @author Wojciech Piwonski
 * 
 */
public class ConstructorsTest extends ImageCacheTest {

    @Before
    public void setup() {
        context = Robolectric.application;
        imageCache = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullConfigFailTest() {
        // when
        final CacheConfig config = null;
        imageCache = new ImageCache(config);

        // then
        // see annotation param
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullConfigContextFailTest() {
        // when
        final CacheConfig config = null;
        imageCache = new ImageCache(context, config);

        // then
        // see annotation param
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyConfigFailTest() {
        // given
        final CacheConfig config = new CacheConfig();

        // when
        imageCache = new ImageCache(config);

        // then
        // see annotation param
    }

    @Test
    public void defaultValuesTest() {
        // when
        imageCache = new ImageCache(context);

        // then
        final int memoryClass = getMemoryClass();
        assertEquals(DEFAULT_WORKERS_NUMBER, imageCache.getWorkersNumber());
        assertEquals(getDefaultMemorySize(memoryClass), imageCache.getMemoryCacheMaxSize());
        assertEquals(getDefaultDiskSize(memoryClass), imageCache.getDiskCacheMaxSize());
        assertEquals(getDefaultCachePath(), imageCache.getDiskCachePath());
        assertEquals(DEFAULT_COMPRESS_FORMAT, imageCache.getCompressFormat());
        assertEquals(DEFAULT_COMPRESS_QUALITY, imageCache.getCompressQuality());
    }

    @Test
    public void fullConfigTest() {
        // given
        final int workersNumber = 1;
        final int memoryCacheSize = 6 * 1024 * 1024;
        final String diskCachePath = File.separator + "cache";
        final long diskCacheSize = 10 * 1024 * 1024;
        final CompressFormat compressFormat = CompressFormat.JPEG;
        final int compressQuality = 80;
        final CacheConfig config = prepareConfig(workersNumber, memoryCacheSize, diskCachePath, diskCacheSize,
                compressFormat, compressQuality);

        // when
        imageCache = new ImageCache(config);

        // then
        assertEquals(workersNumber, imageCache.getWorkersNumber());
        assertEquals(memoryCacheSize, imageCache.getMemoryCacheMaxSize());
        assertEquals(diskCacheSize, imageCache.getDiskCacheMaxSize());
        assertEquals(diskCachePath, imageCache.getDiskCachePath());
        assertEquals(compressFormat, imageCache.getCompressFormat());
        assertEquals(compressQuality, imageCache.getCompressQuality());
    }

    @Test
    public void workersNumberDefaultLoadingTest() {
        // given
        final CacheConfig config = new CacheConfig();
        final int workersNumber = 2;
        config.setWorkersNumber(workersNumber);

        // when
        imageCache = new ImageCache(context, config);

        // then
        assertEquals(workersNumber, imageCache.getWorkersNumber());

        final int memoryClass = getMemoryClass();
        assertEquals(getDefaultMemorySize(memoryClass), imageCache.getMemoryCacheMaxSize());
        assertEquals(getDefaultDiskSize(memoryClass), imageCache.getDiskCacheMaxSize());
        assertEquals(getDefaultCachePath(), imageCache.getDiskCachePath());
        assertEquals(DEFAULT_COMPRESS_FORMAT, imageCache.getCompressFormat());
        assertEquals(DEFAULT_COMPRESS_QUALITY, imageCache.getCompressQuality());

    }

    @Test
    public void memorySizeDefaultLoadingTest() {
        // given
        final CacheConfig config = new CacheConfig();
        final int memoryCacheSize = 6 * 1024 * 1024;
        config.setMemoryCacheSize(memoryCacheSize);

        // when
        imageCache = new ImageCache(context, config);

        // then
        assertEquals(memoryCacheSize, imageCache.getMemoryCacheMaxSize());

        final int memoryClass = getMemoryClass();
        assertEquals(DEFAULT_WORKERS_NUMBER, imageCache.getWorkersNumber());
        assertEquals(getDefaultDiskSize(memoryClass), imageCache.getDiskCacheMaxSize());
        assertEquals(getDefaultCachePath(), imageCache.getDiskCachePath());
        assertEquals(DEFAULT_COMPRESS_FORMAT, imageCache.getCompressFormat());
        assertEquals(DEFAULT_COMPRESS_QUALITY, imageCache.getCompressQuality());
    }

    @Test
    public void pathDefaultLoadingTest() {
        // given
        final CacheConfig config = new CacheConfig();
        final String diskCachePath = File.separator + "cache";
        config.setDiskCachePath(diskCachePath);

        // when
        imageCache = new ImageCache(context, config);

        // then
        assertEquals(diskCachePath, imageCache.getDiskCachePath());

        final int memoryClass = getMemoryClass();
        assertEquals(DEFAULT_WORKERS_NUMBER, imageCache.getWorkersNumber());
        assertEquals(getDefaultMemorySize(memoryClass), imageCache.getMemoryCacheMaxSize());
        assertEquals(getDefaultDiskSize(memoryClass), imageCache.getDiskCacheMaxSize());
        assertEquals(DEFAULT_COMPRESS_FORMAT, imageCache.getCompressFormat());
        assertEquals(DEFAULT_COMPRESS_QUALITY, imageCache.getCompressQuality());
    }

    @Test
    public void diskSizeDefaultLoadingTest() {
        // given
        final CacheConfig config = new CacheConfig();
        final long diskCacheSize = (long) 10 * 1024 * 1024;
        config.setDiskCacheSize(diskCacheSize);

        // when
        imageCache = new ImageCache(context, config);

        // then
        assertEquals(diskCacheSize, imageCache.getDiskCacheMaxSize());

        final int memoryClass = getMemoryClass();
        assertEquals(DEFAULT_WORKERS_NUMBER, imageCache.getWorkersNumber());
        assertEquals(getDefaultMemorySize(memoryClass), imageCache.getMemoryCacheMaxSize());
        assertEquals(getDefaultCachePath(), imageCache.getDiskCachePath());
        assertEquals(DEFAULT_COMPRESS_FORMAT, imageCache.getCompressFormat());
        assertEquals(DEFAULT_COMPRESS_QUALITY, imageCache.getCompressQuality());
    }

    public void compressFormatDefaultLoadingTest() {
        // given
        final CacheConfig config = new CacheConfig();
        final CompressFormat compressFormat = CompressFormat.PNG;
        config.setCompressFormat(compressFormat);

        // when
        imageCache = new ImageCache(context, config);

        // then
        assertEquals(compressFormat, imageCache.getCompressFormat());

        final int memoryClass = getMemoryClass();
        assertEquals(DEFAULT_WORKERS_NUMBER, imageCache.getWorkersNumber());
        assertEquals(getDefaultMemorySize(memoryClass), imageCache.getMemoryCacheMaxSize());
        assertEquals(getDefaultDiskSize(memoryClass), imageCache.getDiskCacheMaxSize());
        assertEquals(getDefaultCachePath(), imageCache.getDiskCachePath());
        assertEquals(DEFAULT_COMPRESS_QUALITY, imageCache.getCompressQuality());
    }

    public void compressQualityDefaultLoadingTest() {
        // given
        final CacheConfig config = new CacheConfig();
        final int compressQuality = 75;
        config.setCompressQuality(compressQuality);

        // when
        imageCache = new ImageCache(context, config);

        // then
        assertEquals(compressQuality, imageCache.getCompressQuality());

        final int memoryClass = getMemoryClass();
        assertEquals(DEFAULT_WORKERS_NUMBER, imageCache.getWorkersNumber());
        assertEquals(getDefaultMemorySize(memoryClass), imageCache.getMemoryCacheMaxSize());
        assertEquals(getDefaultDiskSize(memoryClass), imageCache.getDiskCacheMaxSize());
        assertEquals(getDefaultCachePath(), imageCache.getDiskCachePath());
        assertEquals(DEFAULT_COMPRESS_FORMAT, imageCache.getCompressFormat());
    }

    @Test(expected = IllegalArgumentException.class)
    public void workersNumberFailTest() {
        // given
        final CacheConfig config = prepareConfig(null, 6 * 1024 * 1024, File.separator + "cache",
                (long) (10 * 1024 * 1024), CompressFormat.JPEG, 80);

        // when
        imageCache = new ImageCache(config);

        // then
        // see annotation param
    }

    @Test(expected = IllegalArgumentException.class)
    public void memorySizeFailTest() {
        // given
        final CacheConfig config = prepareConfig(1, null, File.separator + "cache", (long) (10 * 1024 * 1024),
                CompressFormat.JPEG, 80);

        // when
        imageCache = new ImageCache(config);

        // then
        // see annotation param
    }

    @Test(expected = IllegalArgumentException.class)
    public void pathFailTest() {
        // given
        final CacheConfig config = prepareConfig(1, 6 * 1024 * 1024, null, (long) (10 * 1024 * 1024),
                CompressFormat.JPEG, 80);

        // when
        imageCache = new ImageCache(config);

        // then
        // see annotation param
    }

    @Test(expected = IllegalArgumentException.class)
    public void diskSizeFileTest() {
        // given
        final CacheConfig config = prepareConfig(1, 6 * 1024 * 1024, File.separator + "cache", null,
                CompressFormat.JPEG, 80);

        // when
        imageCache = new ImageCache(config);

        // then
        // see annotation param
    }

    @Test(expected = IllegalArgumentException.class)
    public void compressFormatFailTest() {
        // given
        final CacheConfig config = prepareConfig(1, 6 * 1024 * 1024, File.separator + "cache",
                (long) (10 * 1024 * 1024), null, 80);

        // when
        imageCache = new ImageCache(config);

        // then
        // see annotation param
    }

    @Test(expected = IllegalArgumentException.class)
    public void compressQualityFailTest() {
        // given
        final CacheConfig config = prepareConfig(1, 6 * 1024 * 1024, File.separator + "cache",
                (long) (10 * 1024 * 1024), CompressFormat.JPEG, null);

        // when
        imageCache = new ImageCache(config);

        // then
        // see annotation param
    }

}
