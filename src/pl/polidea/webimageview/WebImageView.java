package pl.polidea.webimageview;

import java.io.*;
import java.net.*;

import pl.polidea.imagecache.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.util.*;
import android.widget.*;

/**
 * 
 * @author Marek Multarzynski
 * 
 */
public class WebImageView extends ImageView {

	private static ImageCache imageCache;
	private static WebClient webClient;
	private BitmapProcessor bitmapProcessor;
	private String path;
	AttributeSet attrs;

	public WebImageView(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public WebImageView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public WebImageView(final Context context) {
		super(context);
		init(context, null);
	}

	private synchronized void init(final Context context, final AttributeSet attrs) {
		this.attrs = attrs;
		imageCache = getCache(context);
		webClient = getWebClient();
		bitmapProcessor = new DefaultBitmapProcessor(this);
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
		this.path = path;
		if (path == null) {
			return;
		}
		imageCache.get(path, new OnCacheResultListener() {

			@Override
			public void onCacheMiss(final String key) {

				webClient.requestForImage(path, new WebCallback() {

					@Override
					public void onWebMiss(final String path) {
						// N/A
					}

					@Override
					public void onWebHit(final String path, final File file) {
						final Bitmap bmp = bitmapProcessor.process(file);
						imageCache.put(path, bmp);
						setBitmap(path, bmp);
					}
				});
			}

			@Override
			public void onCacheHit(final String key, final Bitmap bitmap) {
				setBitmap(key, bitmap);
			}
		});
	}

	private void setBitmap(final String key, final Bitmap bitmap) {
		if (key.equals(path)) {
			new Handler(Looper.getMainLooper()).post(new Runnable() {

				@Override
				public void run() {
					setImageBitmap(bitmap);
				}
			});
		}
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

	public BitmapProcessor getBitmapProcessor() {
		return bitmapProcessor;
	}

	public void setBitmapProcessor(final BitmapProcessor bitmapProcessor) {
		this.bitmapProcessor = bitmapProcessor;
	}

	public static interface BitmapProcessor {
		Bitmap process(File pathToBitmap);
	}
}
