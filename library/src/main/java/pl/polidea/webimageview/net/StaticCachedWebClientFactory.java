package pl.polidea.webimageview.net;

import android.content.Context;

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@gmail.com>
 */
public class StaticCachedWebClientFactory implements WebClientFactory {

    private static WebClient cachedWebClient;

    @Override
    public synchronized WebClient create(Context context) {
        if (cachedWebClient == null) {
            cachedWebClient = new WebClient(context);
        }
        return cachedWebClient;
    }
}
