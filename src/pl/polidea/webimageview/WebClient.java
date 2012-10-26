/**
 *
 */
package pl.polidea.webimageview;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.apache.http.client.ClientProtocolException;

/**
 * @author Marek Multarzynski
 * 
 */
public class WebClient {

    Set<String> pathsWaitingForDownloading = new HashSet<String>();
    Set<String> downloadingPaths = new HashSet<String>();
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

        pathsWaitingForDownloading.add(path);

        try {
            httpClient.execute(path);
            clientResultListener.onWebHit(path, null);
        } catch (final ClientProtocolException e) {
            clientResultListener.onWebMiss(path);
        } catch (final IOException e) {
            clientResultListener.onWebMiss(path);
        }
        pathsWaitingForDownloading.remove(path);
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
