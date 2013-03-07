/**
 *
 */
package pl.polidea.webimageview.net;

import android.content.Context;
import org.apache.http.client.ClientProtocolException;
import pl.polidea.utils.StackPoolExecutor;

import java.io.*;
import java.util.concurrent.ExecutorService;

/**
 * @author Marek Multarzynski
 */
public class WebClient {

    final File cacheDir;
    TaskContainer pendingTasks = new TaskContainer();
    WebInterface webInterface = new WebInterfaceImpl();
    ExecutorService taskExecutor = new StackPoolExecutor(5);

    public WebClient(final Context context) {
        cacheDir = context.getCacheDir();
    }

    /**
     * Request for image. If Image under path url is downloading the current
     * time the listener is added to collection of callbacks. Otherwise new
     * download thread is run.
     *
     * @param url         the path
     * @param webCallback the client result listener
     */
    public void requestForImage(final String url, final WebCallback webCallback) {

        if (webCallback == null) {
            throw new IllegalArgumentException("webCallback cannot be null");
        }

        final boolean newTask = pendingTasks.addTask(url, webCallback);

        if (newTask) {
            taskExecutor.submit(buildTask(url));
        }
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

    DownloadTask buildTask(String url){
        return new DownloadTask(url);
    }

    class DownloadTask implements Runnable {

        private final String url;

        public DownloadTask(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            File tempFile = null;
            try {
                tempFile = File.createTempFile("web", null, cacheDir);
                final InputStream stream = webInterface.execute(url);
                saveStreamToFile(stream, tempFile);

                pendingTasks.performCallbacks(url, tempFile);
            } catch (final IOException e) {
                pendingTasks.performMissCallbacks(url);
            } finally {
                tempFile.delete();
            }
            pendingTasks.remove(url);
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

}
