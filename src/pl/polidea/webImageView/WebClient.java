/**
 *
 */
package pl.polidea.webImageView;

import java.net.URL;

/**
 * @author Marek Multarzynski
 * 
 */
public class WebClient {

    public void requestForImage(final URL url, final OnWebClientResultListener clientResultListener) {

        if (clientResultListener == null) {
            throw new IllegalArgumentException("clientResultListener cannot be null");
        }

        clientResultListener.onWebMiss(url);
    }
}
