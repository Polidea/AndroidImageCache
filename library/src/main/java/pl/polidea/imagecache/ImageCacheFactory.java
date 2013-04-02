package pl.polidea.imagecache;

import android.content.Context;

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@gmail.com>
 */
public interface ImageCacheFactory {

    ImageCache create(Context context);

    ImageCache create(Context context, CacheConfig config);

    ImageCache create(CacheConfig cacheConfig);
}
