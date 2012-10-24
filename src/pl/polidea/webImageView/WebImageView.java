package pl.polidea.webImageView;

import java.net.URL;

import pl.polidea.imagecache.ImageCache;
import pl.polidea.imagecache.OnCacheResultListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 
 * @author Marek Multarzynski
 * 
 */
public class WebImageView extends ImageView {

    private static ImageCache imageCache;
    private static WebClient webClient;

    public WebImageView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        imageCache = getCache(context);
        webClient = getWebClient();
    }

    public WebImageView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        imageCache = getCache(context);
    }

    public WebImageView(final Context context) {
        super(context);
        imageCache = getCache(context);
    }

    private ImageCache getCache(final Context context) {
        return imageCache == null ? new ImageCache(context) : imageCache;
    }

    private WebClient getWebClient() {
        return webClient == null ? new WebClient() : webClient;
    }

    public void setImageURL(final URL url) {
        if (url == null) {
            return;
        }
        final String pathIeKey = url.getPath();
        if (pathIeKey == null) {
            return;
        }
        imageCache.get(pathIeKey, new OnCacheResultListener() {

            @Override
            public void onCacheMiss(final String key) {

                webClient.requestForImage(url, new OnWebClientResultListener() {

                    @Override
                    public void onWebMiss(final URL url) {
                        return;
                    }

                    @Override
                    public void onWebHit(final URL url, final Bitmap bitmap) {
                        imageCache.put(url.getPath(), bitmap);
                        setImageBitmap(bitmap);
                    }
                });
            }

            @Override
            public void onCacheHit(final String key, final Bitmap bitmap) {
                setImageBitmap(bitmap);
            }
        });
    }
}
