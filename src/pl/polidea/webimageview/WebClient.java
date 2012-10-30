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
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.apache.http.client.ClientProtocolException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * @author Marek Multarzynski
 * 
 */
public class WebClient {

    TaskContainer pendingTasks = new TaskContainer();
    WebInterface httpClient = new WebInterfaceImpl();
    ExecutorService taskExecutor = Executors.newFixedThreadPool(5, new ThreadFactory() {

        @Override
        public Thread newThread(final Runnable r) {
            return new Thread(r, "Image downloading thread");
        }
    });

    /**
     * Request for image. If Image under path url is downloading the current
     * time the listener is added to collection of callbacks. Otherwise new
     * download thread is run.
     * 
     * @param path
     *            the path
     * @param clientResultListener
     *            the client result listener
     */
    public void requestForImage(final String path, final WebCallback clientResultListener) {

        if (clientResultListener == null) {
            throw new IllegalArgumentException("clientResultListener cannot be null");
        }

        final boolean newTask = pendingTasks.addTask(path, clientResultListener);

        if (newTask) {
            taskExecutor.submit(new Runnable() {

                @Override
                public void run() {
                    File tempFile = null;
                    try {
                        tempFile = File.createTempFile("web", null);
                        final InputStream stream = httpClient.execute(path);
                        saveStreamToFile(stream, tempFile);
                        Log.d("WebClient", "Downloading path: " + path);
                        final Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath());

                        pendingTasks.performCallbacks(path, bitmap);
                    } catch (final ClientProtocolException e) {
                        pendingTasks.performMissCallbacks(path);
                    } catch (final IOException e) {
                        pendingTasks.performMissCallbacks(path);
                    } finally {
                        tempFile.delete();
                    }
                    pendingTasks.remove(path);
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
