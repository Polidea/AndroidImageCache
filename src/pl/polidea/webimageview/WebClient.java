/**
 *
 */
package pl.polidea.webimageview;

/**
 * @author Marek Multarzynski
 * 
 */
public class WebClient {

    public void requestForImage(final String path, final OnWebClientResultListener clientResultListener) {

        if (clientResultListener == null) {
            throw new IllegalArgumentException("clientResultListener cannot be null");
        }

        clientResultListener.onWebMiss(path);
    }
}
