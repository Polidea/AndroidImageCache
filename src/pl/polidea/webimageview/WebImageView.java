package pl.polidea.webimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import java.io.File;
import java.net.URL;
import pl.polidea.imagecache.ImageCache;
import pl.polidea.imagecache.OnCacheResultListener;
import pl.polidea.webimageview.net.WebCallback;
import pl.polidea.webimageview.net.WebClient;
import pl.polidea.webimageview.processor.BitmapProcessor;

/**
 * @author Marek Multarzynski
 */
public class WebImageView extends ImageView {

    private static ImageCache imageCache;

    private static WebClient webClient;

    private BitmapProcessor bitmapProcessor;

    private String path;

    private Handler handler;

    public WebImageView(final Context context) {
        this(context, null);
    }

    public WebImageView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    public static ImageCache getCache(final Context context) {
        return imageCache == null ? new ImageCache(context) : imageCache;
    }

    public static WebClient getWebClient(final Context context) {
        return webClient == null ? new WebClient(context) : webClient;
    }

    private synchronized void init(final Context context, final AttributeSet attrsSet) {
        // XXX: this is done in UI thread !
        imageCache = getCache(context);
        webClient = getWebClient(context);
        // XXX: this is done in UI thread, read from disc !
        bitmapProcessor = new DefaultBitmapProcessor(context, attrsSet);
        handler = new Handler(Looper.getMainLooper());
    }

    /**
     * Sets the content of this WebImageView to the specified path.
     *
     * @param path The path of an image
     */
    public void setImageURL(final String path) {
        setImageURL(path, null);
    }

    public void setImageURL(final String path, final WebImageListener webImageListener) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        if (path.equals(this.path)) {
            invalidate();
        }

        this.path = path;
        imageCache.get(path, new OnCacheResultListener() {

            @Override
            public void onCacheMiss(final String key) {
                if (webImageListener != null) {
                    webImageListener.imageFailed(WebImageView.this.path);
                }
                webClient.requestForImage(path, new WebCallback() {

                    @Override
                    public void onWebMiss(final String path) {

                    }

                    @Override
                    public void onWebHit(final String resource, final File file) {
                        if (resource.equals(WebImageView.this.path)) {
                            final Bitmap bmp;
                            try {
                                bmp = bitmapProcessor.process(file);
                                setBitmap(resource, bmp, webImageListener);
                                imageCache.put(resource, bmp);
                            } catch (BitmapDecodeException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            }
                        }
                    }
                });
            }

            @Override
            public void onCacheHit(final String key, final Bitmap bitmap) {
                setBitmap(key, bitmap, webImageListener);
            }
        });
    }

    private void setBitmap(final String key, final Bitmap bitmap, final WebImageListener webImageListener) {
        if (key.equals(path) && bitmap != null && !bitmap.isRecycled()) {
            handler.post(new RunnableImplementation(bitmap));
        }
        if (webImageListener != null) {
            webImageListener.imageSet(path);
        }
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

    public BitmapProcessor getBitmapProcessor() {
        return bitmapProcessor;
    }

    public void setBitmapProcessor(final BitmapProcessor bitmapProcessor) {
        this.bitmapProcessor = bitmapProcessor;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
    }

    public void disableBitmapProcessor() {
        bitmapProcessor = BitmapProcessor.DEFAULT;
    }

    public static interface WebImageListener {

        void imageSet(String url);

        void imageFailed(String url);
    }

    private final class RunnableImplementation implements Runnable {

        private final Bitmap bitmap;

        private RunnableImplementation(final Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        public void run() {
            setImageBitmap(bitmap);
        }
    }

}
