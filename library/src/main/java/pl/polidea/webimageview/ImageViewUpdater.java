package pl.polidea.webimageview;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@gmail.com>
 */
public class ImageViewUpdater {

    private final Handler handler;

    private final ImageView imageView;

    private String currentURL;

    public ImageViewUpdater(ImageView imageView) {
        this.imageView = imageView;
        handler = new Handler(Looper.getMainLooper());
    }

    public synchronized void setCurrentURL(String url) {
        currentURL = url;
    }

    public synchronized void setBitmap(final String key, final Bitmap bitmap, final WebImageListener webImageListener) {
        if (key.equals(currentURL) && bitmap != null && !bitmap.isRecycled()) {
            handler.post(new RunnableImplementation(bitmap));
        }
        webImageListener.onImageFetchedSuccessfully(currentURL);
    }

    private final class RunnableImplementation implements Runnable {

        private final Bitmap bitmap;

        private RunnableImplementation(final Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        public void run() {
            imageView.setImageBitmap(bitmap);
        }
    }
}
