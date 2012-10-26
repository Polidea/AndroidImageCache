/**
 *
 */
package pl.polidea.webimageview;

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

    public void requestForImage(final String path, final WebCallback clientResultListener) {

        if (clientResultListener == null) {
            throw new IllegalArgumentException("clientResultListener cannot be null");
        }

        final boolean newTask = pendingTasks.addTask(path, clientResultListener);

        if (newTask) {
            taskExecutor.submit(new Runnable() {

                @Override
                public void run() {
                    try {
                        final InputStream stream = httpClient.execute(path);
                        Log.d("WebClient", "Downloading path: " + path);
                        final Bitmap bitmap = BitmapFactory.decodeStream(stream);
                        pendingTasks.performCallbacks(path, bitmap);
                    } catch (final ClientProtocolException e) {
                        pendingTasks.performMissCallbacks(path);
                    } catch (final IOException e) {
                        pendingTasks.performMissCallbacks(path);
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

}
