package pl.polidea.webimageview.net;

import android.content.Context;

/**
 * @author Mateusz Grzechociński <mateusz.grzechocinski@gmail.com>
 */
public interface WebClientFactory {

    WebClient create(Context context);
}
