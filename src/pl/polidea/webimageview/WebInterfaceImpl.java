/**
 *
 */
package pl.polidea.webimageview;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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
        final HttpGet httpGet = new HttpGet(path);
        final HttpResponse execute = httpClient.execute(httpGet);
        final HttpEntity entity = execute.getEntity();
        final InputStream content = entity.getContent();
        return content;
    }

}
