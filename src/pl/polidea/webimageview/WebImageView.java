package pl.polidea.webimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import java.io.File;
import java.net.URL;
import pl.polidea.imagecache.ImageCache;
import pl.polidea.imagecache.ImageCacheFactory;
import pl.polidea.imagecache.OnCacheResultListener;
import pl.polidea.imagecache.StaticCachedImageCacheFactory;
import pl.polidea.webimageview.net.StaticCachedWebClientFactory;
import pl.polidea.webimageview.net.WebCallback;
import pl.polidea.webimageview.net.WebClient;
import pl.polidea.webimageview.net.WebClientFactory;
import pl.polidea.webimageview.processor.BitmapProcessor;

/**
 * @author Marek Multarzynski
 */
public class WebImageView extends ImageView implements OnCacheResultListener {

    public static final ImageCacheFactory DEFAULT_IMAGE_CACHE_FACTORY = new StaticCachedImageCacheFactory();

    public static final WebClientFactory DEFAULT_WEB_CLIENT_FACTORY = new StaticCachedWebClientFactory();

    protected BitmapProcessor bitmapProcessor;

    protected String url;

    protected ImageCache imageCache;

    protected WebClient webClient;

    private ImageViewUpdater imageViewUpdater;

    private WebImageListener webImageListener;

    public WebImageView(final Context context) {
        this(context, null);
    }

    public WebImageView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init(attrs);
        }
    }

    public void init(AttributeSet attrsSet) {
        bitmapProcessor = new DefaultBitmapProcessor(getContext(), attrsSet);
        setFactories(DEFAULT_IMAGE_CACHE_FACTORY, DEFAULT_WEB_CLIENT_FACTORY);
        imageViewUpdater = new ImageViewUpdater(this);
    }

    /**
     * Sets the content of this WebImageView to the specified url.
     *
     * @param url The url of an image
     * @throws IllegalArgumentException when url is an empty string
     */
    public void setImageURL(final String url) {
        setImageURL(url, WebImageListener.NULL);
    }

    /**
     * Sets the content of this WebImageView to the specified url.
     *
     * @param url The url of an image
     * @param webImageListener option listener of image fetching state
     * @throws IllegalArgumentException when url is an empty string
     */
    public void setImageURL(final String url, final WebImageListener webImageListener) {
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("Image url cannot be empty");
        }
        this.webImageListener = webImageListener;

        if (url.equals(this.url)) {
            invalidate();
        }
        this.url = url;
        imageViewUpdater.setCurrentURL(url);
        imageCache.get(url, this);
    }

    /**
     * Sets the content of this WebImageView to the specified URL.
     *
     * @param url The Uri of an image
     */
    public void setImageURL(final URL url) {
        if (url == null) {
            return;
        }
        setImageURL(url.getPath());
    }

    public void disableBitmapProcessor() {
        bitmapProcessor = BitmapProcessor.DEFAULT;
    }

    @Override
    public void onCacheMiss(final String key) {
        webClient.requestForImage(url, new WebCallback() {
            @Override
            public void onWebMiss(final String url) {
                webImageListener.onImageFetchedFailed(WebImageView.this.url);
            }

            @Override
            public void onWebHit(final String resource, final File file) {
                if (resource.equals(WebImageView.this.url)) {
                    final Bitmap bmp;
                    try {
                        bmp = bitmapProcessor.process(file);
                        imageViewUpdater.setBitmap(resource, bmp, webImageListener);
                        imageCache.put(resource, bmp);
                    } catch (BitmapDecodeException e) {
                        webImageListener.onImageFetchedFailed(url);
                    }
                }
            }
        });
    }

    @Override
    public void onCacheHit(final String key, final Bitmap bitmap) {
        imageViewUpdater.setBitmap(key, bitmap, webImageListener);
    }

    public void setBitmapProcessor(final BitmapProcessor bitmapProcessor) {
        this.bitmapProcessor = bitmapProcessor;
    }

    public BitmapProcessor getBitmapProcessor() {
        return bitmapProcessor;
    }

    public void setImageCacheFactory(ImageCacheFactory imageCacheFactory) {
        setFactories(imageCacheFactory, DEFAULT_WEB_CLIENT_FACTORY);
    }

    public void setWebClientFactory(WebClientFactory webClientFactory) {
        setFactories(DEFAULT_IMAGE_CACHE_FACTORY, webClientFactory);
    }

    public void setFactories(ImageCacheFactory imageCacheFactory, WebClientFactory webClientFactory) {
        Context context = getContext();
        imageCache = imageCacheFactory.create(context);
        webClient = webClientFactory.create(context);
    }
}
