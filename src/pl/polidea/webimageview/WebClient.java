/**
 *
 */
package pl.polidea.webimageview;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;

/**
 * @author Marek Multarzynski
 * 
 */
public class WebClient {

    Set<String> pathsWaitingForDownloading = new HashSet<String>();
    Set<String> downloadingPaths = new HashSet<String>();
    WebInterface httpClient = new WebInterfaceImpl();

    public void requestForImage(final String path, final OnWebClientResultListener clientResultListener) {

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
}
