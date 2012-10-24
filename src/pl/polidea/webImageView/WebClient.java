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
        clientResultListener.onWebMiss(url);
    }
}
