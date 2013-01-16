/**
 *
 */
package pl.polidea.webimageview;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.ClientProtocolException;

import android.content.Context;

/**
 * @author Marek Multarzynski
 * 
 */
public class WebClient {

    TaskContainer pendingTasks = new TaskContainer();
    WebInterface httpClient = new WebInterfaceImpl();
    ExecutorService taskExecutor = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS, new StackBlockingDeque(),
            new ThreadFactory() {

                @Override
                public Thread newThread(final Runnable r) {
                    final Thread thread = new Thread(r, "Image downloading thread");
                    thread.setPriority(Thread.MIN_PRIORITY);
                    return thread;
                }
            });

    private final File cacheDir;

    public WebClient(final Context context) {
        cacheDir = context.getCacheDir();
    }

    /**
     * Request for image. If Image under path url is downloading the current
     * time the listener is added to collection of callbacks. Otherwise new
     * download thread is run.
     * 
     * @param url
     *            the path
     * @param clientResultListener
     *            the client result listener
     */
    public void requestForImage(final String url, final WebCallback clientResultListener) {

        if (clientResultListener == null) {
            throw new IllegalArgumentException("clientResultListener cannot be null");
        }

        final boolean newTask = pendingTasks.addTask(url, clientResultListener);

        if (newTask) {
            taskExecutor.submit(new Runnable() {

                @Override
                public void run() {
                    File tempFile = null;
                    try {
                        tempFile = File.createTempFile("web", null, cacheDir);
                        final InputStream stream = httpClient.execute(url);
                        saveStreamToFile(stream, tempFile);

                        pendingTasks.performCallbacks(url, tempFile);
                    } catch (final ClientProtocolException e) {
                        pendingTasks.performMissCallbacks(url);
                    } catch (final IOException e) {
                        pendingTasks.performMissCallbacks(url);
                    } finally {
                        tempFile.delete();
                    }
                    pendingTasks.remove(url);
                }
            });
        }
    }

    /**
     * @param httpClient
     */
    public void setWebInterface(final WebInterface httpClient) {
        if (httpClient != null) {
            this.httpClient = httpClient;
        }
    }

    public void setTaskExecutor(final ExecutorService taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void saveStreamToFile(final InputStream is, final File file) throws IOException {
        if (is == null) {
            throw new IOException("Empty stream");
        }
        final BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file));
        final byte[] data = new byte[16384];
        int n;
        while ((n = is.read(data, 0, data.length)) != -1) {
            os.write(data, 0, n);
        }
        os.flush();
        os.close();
    }

}
