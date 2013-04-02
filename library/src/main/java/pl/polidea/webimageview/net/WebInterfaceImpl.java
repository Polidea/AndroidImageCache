/**
 *
 */
package pl.polidea.webimageview.net;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * @author Przemysław Jakubczyk <przemyslaw.jakubczyk@pl.polidea.pl>
 */
public class WebInterfaceImpl implements WebInterface {

    @Override
    public InputStream execute(final String path) throws IOException {
        return new DefaultHttpClient().execute(new HttpGet(path)).getEntity().getContent();
    }

}
