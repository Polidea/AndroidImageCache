/**
 *
 */
package pl.polidea.webimageview;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.client.ClientProtocolException;

/**
 * @author Marek Multarzynski
 * 
 */
public interface WebInterface {

    InputStream execute(String path) throws ClientProtocolException, IOException;
}
