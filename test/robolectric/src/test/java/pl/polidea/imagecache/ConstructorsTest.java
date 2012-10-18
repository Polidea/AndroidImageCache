/**
 * 
 */
package pl.polidea.imagecache;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.graphics.Bitmap.CompressFormat;

import com.xtremelabs.robolectric.Robolectric;

/**
 * @author Wojciech Piwonski
 * 
 */
@RunWith(ImageCacheTestRunner.class)
public class ConstructorsTest {

    private Context context;
    private CacheConfig config;
    private ImageCache imageCache;

    @Before
    public void setup() {
        context = Robolectric.application;
        imageCache = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullConfigFailTest() {
        imageCache = new ImageCache(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullConfigContextFailTest() {
        imageCache = new ImageCache(context, config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyConfigFailTest() {
        config = new CacheConfig();
        imageCache = new ImageCache(config);
    }

    @Test
    public void defaultValuesTest() {
        imageCache = new ImageCache(context);
    }

    @Test
    public void fullConfigTest() {
        config = prepareConfig(1, 6 * 1024 * 1024, "/cache", (long) (10 * 1024 * 1024), CompressFormat.JPEG, 80);
        imageCache = new ImageCache(config);
    }

    @Test
    public void workersNumberDefaultLoadingTest() {
        config = new CacheConfig();
        config.setWorkersNumber(2);
        imageCache = new ImageCache(context, config);
    }

    @Test
    public void memorySizeDefaultLoadingTest() {
        config = new CacheConfig();
        config.setMemoryCacheSize(6 * 1024 * 1024);
        imageCache = new ImageCache(context, config);
    }

    @Test
    public void pathDefaultLoadingTest() {
        config = new CacheConfig();
        config.setDiskCachePath("/cache");
        imageCache = new ImageCache(context, config);
    }

    @Test
    public void diskSizeDefaultLoadingTest() {
        config = new CacheConfig();
        config.setDiskCacheSize((long) 10 * 1024 * 1024);
        imageCache = new ImageCache(context, config);
    }

    public void compressFormatDefaultLoadingTest() {
        config = new CacheConfig();
        config.setCompressFormat(CompressFormat.PNG);
        imageCache = new ImageCache(context, config);
    }

    public void compressQualityDefaultLoadingTest() {
        config = new CacheConfig();
        config.setCompressQuality(75);
        imageCache = new ImageCache(context, config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void workersNumberFailTest() {
        config = prepareConfig(null, 6 * 1024 * 1024, "/cache", (long) (10 * 1024 * 1024), CompressFormat.JPEG, 80);
        imageCache = new ImageCache(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void memorySizeFailTest() {
        config = prepareConfig(1, null, "/cache", (long) (10 * 1024 * 1024), CompressFormat.JPEG, 80);
        imageCache = new ImageCache(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void pathFailTest() {
        config = prepareConfig(1, 6 * 1024 * 1024, null, (long) (10 * 1024 * 1024), CompressFormat.JPEG, 80);
        imageCache = new ImageCache(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void diskSizeFileTest() {
        config = prepareConfig(1, 6 * 1024 * 1024, "/cache", null, CompressFormat.JPEG, 80);
        imageCache = new ImageCache(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void compressFormatFailTest() {
        config = prepareConfig(1, 6 * 1024 * 1024, "/cache", (long) (10 * 1024 * 1024), null, 80);
        imageCache = new ImageCache(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void compressQualityFailTest() {
        config = prepareConfig(1, 6 * 1024 * 1024, "/cache", (long) (10 * 1024 * 1024), CompressFormat.JPEG, null);
        imageCache = new ImageCache(config);
    }

    private CacheConfig prepareConfig(final Integer workersNumber, final Integer memoryCacheSize,
            final String diskCachePath, final Long diskCacheSize, final CompressFormat compressFormat,
            final Integer compressQuality) {
        final CacheConfig config = new CacheConfig();
        if (workersNumber != null) {
            config.setWorkersNumber(workersNumber);
        }
        if (memoryCacheSize != null) {
            config.setMemoryCacheSize(memoryCacheSize);
        }
        if (diskCacheSize != null) {
            config.setDiskCacheSize(diskCacheSize);
        }
        if (diskCachePath != null) {
            config.setDiskCachePath(diskCachePath);
        }
        if (compressFormat != null) {
            config.setCompressFormat(compressFormat);
        }
        if (compressQuality != null) {
            config.setCompressQuality(compressQuality);
        }
        return config;
    }

}
