/**
 *
 */
package pl.polidea.webimageview.net;

import android.content.Context;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;

import pl.polidea.utils.StackPoolExecutor;
import pl.polidea.utils.TempFile;

/**
 * @author Przemysław Jakubczyk <przemyslaw.jakubczyk@polidea.pl>
 */
public class WebClient {

	final File cacheDir;

	WebInterface webInterface = new WebInterfaceImpl();

	ExecutorService taskExecutor = new StackPoolExecutor(10, 30);

	WebClient(final Context context) {
		cacheDir = context.getCacheDir();
	}

	/**
	 * Request for image. If Image under path url is downloading the current
	 * time the listener is added to collection of callbacks. Otherwise new
	 * download thread is run.
	 * 
	 * @param url
	 *            the path
	 * @param webCallback
	 *            the client result listener
	 */
	public void requestForImage(final String url, final WebCallback webCallback) {

		if (webCallback == null) {
			throw new IllegalArgumentException("webCallback cannot be null");
		}

		taskExecutor.submit(buildTask(url, webCallback));
	}

	/**
	 * @param httpClient
	 */
	public void setWebInterface(final WebInterface httpClient) {
		if (httpClient != null) {
			this.webInterface = httpClient;
		}
	}

	public void setTaskExecutor(final ExecutorService taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	DownloadTask buildTask(String url, WebCallback webCallback) {
		return new DownloadTask(url, webCallback);
	}

	class DownloadTask implements Runnable {

		private final String url;
		private WebCallback webCallback;

		public DownloadTask(String url, WebCallback webCallback) {
			this.url = url;
			this.webCallback = webCallback;
		}

		@Override
		public void run() {
			TempFile tempFile = TempFile.nullObject();
			try {
				tempFile = TempFile.createInDir(cacheDir);
				final InputStream stream = webInterface.execute(url);
				saveStreamToFile(stream, tempFile);
				webCallback.onWebHit(url, tempFile.asJavaFile());
			} catch (final IOException e) {
				webCallback.onWebMiss(url);
			} finally {
				tempFile.delete();
			}
		}

		public void saveStreamToFile(final InputStream is, final TempFile file)
				throws IOException {
			if (is == null) {
				throw new IOException("Empty stream");
			}
			final BufferedOutputStream os = new BufferedOutputStream(
					new FileOutputStream(file.asJavaFile()));
			final byte[] data = new byte[16384];
			int n;
			while ((n = is.read(data, 0, data.length)) != -1) {
				os.write(data, 0, n);
			}
			os.flush();
			os.close();
		}
	}

}
