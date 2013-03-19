package pl.polidea.imagecache;

import android.content.Context;

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@gmail.com>
 */
public class StaticCachedImageCacheFactory implements ImageCacheFactory {

    private static ImageCache imageCache;

    @Override
    public synchronized ImageCache create(final Context context) {
        return create(CacheConfig.buildDefault(context));
    }

    @Override
    public synchronized ImageCache create(final Context context, final CacheConfig config) {
        return create(CacheConfig.buildDefault(context, config));
    }

    @Override
    public synchronized ImageCache create(CacheConfig cacheConfig){
        if(imageCache == null){
            imageCache = new ImageCache(cacheConfig);
        }
        return imageCache;
    }

}
