/**
 *
 */
package pl.polidea.webimageview;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * @author Marek Multarzynski
 * 
 */
public class WebInterfaceImpl implements WebInterface {

    @Override
    public InputStream execute(final String path) throws ClientProtocolException, IOException {
        final DefaultHttpClient httpClient = new DefaultHttpClient();
        return httpClient.execute(new HttpGet(path)).getEntity().getContent();
    }

}
