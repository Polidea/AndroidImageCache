package pl.polidea.webimageview;

import java.net.URL;

import pl.polidea.imagecache.ImageCache;
import pl.polidea.imagecache.OnCacheResultListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
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
        webClient = getWebClient();
    }

    public WebImageView(final Context context) {
        super(context);
        imageCache = getCache(context);
        webClient = getWebClient();
    }

    private ImageCache getCache(final Context context) {
        return imageCache == null ? new ImageCache(context) : imageCache;
    }

    private WebClient getWebClient() {
        return webClient == null ? new WebClient() : webClient;
    }

    /**
     * Sets the content of this WebImageView to the specified path.
     * 
     * @param path
     *            The path of an image
     */
    public void setImageURL(final String path) {
        if (path == null) {
            return;
        }
        imageCache.get(path, new OnCacheResultListener() {

            @Override
            public void onCacheMiss(final String key) {

                webClient.requestForImage(path, new WebCallback() {

                    @Override
                    public void onWebMiss(final String path) {
                        return;
                    }

                    @Override
                    public void onWebHit(final String path, final Bitmap bitmap) {
                        imageCache.put(path, bitmap);
                        setBitmap(bitmap);
                    }
                });
            }

            @Override
            public void onCacheHit(final String key, final Bitmap bitmap) {
                setBitmap(bitmap);
            }
        });
    }

    private void setBitmap(final Bitmap bitmap) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                setImageBitmap(bitmap);
            }
        });
    }

    /**
     * Sets the content of this WebImageView to the specified URL.
     * 
     * @param url
     *            The Uri of an image
     */
    public void setImageURL(final URL url) {
        if (url == null) {
            return;
        }
        setImageURL(url.getPath());
    }
}
